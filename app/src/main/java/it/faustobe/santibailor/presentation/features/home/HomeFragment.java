package it.faustobe.santibailor.presentation.features.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.animation.ObjectAnimator;
import android.widget.Toast;

import android.view.ViewTreeObserver;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.storage.FirebaseStorage;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import it.faustobe.santibailor.domain.model.Ricorrenza;
import it.faustobe.santibailor.domain.model.TipoRicorrenza;
import it.faustobe.santibailor.presentation.common.ricorrenze.RicorrenzaAdapter;
import it.faustobe.santibailor.presentation.features.main.MainActivity;
import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentHomeBinding;
import it.faustobe.santibailor.util.DateUtils;
import it.faustobe.santibailor.presentation.common.viewmodels.RicorrenzaViewModel;
import it.faustobe.santibailor.util.ImageHandler;
//import it.faustobe.santibailor.util.ImageMigrationService;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    ViewModelProvider.Factory viewModelFactory;

    private FragmentHomeBinding binding;
    private RicorrenzaViewModel ricorrenzaViewModel;
    //private ImageMigrationService imageMigrationService;
    private boolean isInitialized = false;
    private RicorrenzaAdapter ricorrenzaAdapter;
    private boolean isPersonalInfoExpanded = false;
    private boolean isSaintsListExpanded = false;
    private boolean isCalendarExpanded = true;
    private HomeViewModel homeViewModel;
    private View expandPersonalInfoIcon;
    private static final String TAG = "HomeFragment";
    private NestedScrollView nestedScrollView;
    private ImageHandler imageHandler;
    private MotionLayout motionLayout;
    private Handler collapseHandler = new Handler(Looper.getMainLooper());
    private static final long COLLAPSE_DELAY = 5000; // 5 secondi
    private ValueAnimator calendarAnimator;
    private ConstraintSet expandedSet = new ConstraintSet();
    private ConstraintSet collapsedSet = new ConstraintSet();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Usa questo metodo per ottenere il ViewModel
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        ricorrenzaViewModel = new ViewModelProvider(requireActivity()).get(RicorrenzaViewModel.class);

        // Inizializza ImageMigrationService solo se ricorrenzaViewModel è disponibile
        if (ricorrenzaViewModel != null && ricorrenzaViewModel.getRepository() != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            imageHandler = ImageHandler.getInstance(requireContext());
        } else {
            Log.e("HomeFragment", "RicorrenzaViewModel o il suo repo è null");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        Log.d(TAG, "onCreateView: Vista creata");

        motionLayout = binding.motionLayout;
        if (motionLayout == null) {
            Log.e(TAG, "onCreateView: MotionLayout non trovato nel binding");
        } else {
            Log.d(TAG, "onCreateView: MotionLayout trovato e inizializzato");
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("HomeFragment", "onViewCreated: Inizializzazione della vista");

        //motionLayout = binding.motionLayout;
        //Log.d(TAG, "onViewCreated: MotionLayout inizializzato");

        ricorrenzaViewModel = new ViewModelProvider(requireActivity()).get(RicorrenzaViewModel.class);

        if (ricorrenzaAdapter == null) {
            ricorrenzaAdapter = new RicorrenzaAdapter(this::navigateToRicorrenzaDetail, ricorrenzaViewModel);
            binding.recyclerViewSaints.setAdapter(ricorrenzaAdapter);
            binding.recyclerViewSaints.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        // Osserva le ricorrenze del giorno
        ricorrenzaViewModel.getRicorrenzeDelGiorno().observe(getViewLifecycleOwner(), this::updateRicorrenzeList);

        // Osserva il santo del giorno
        ricorrenzaViewModel.getCurrentSaint().observe(getViewLifecycleOwner(), this::updateSaintOfDay);

        if (!isInitialized) {
            ricorrenzaViewModel.loadRicorrenzeForCurrentDate();
            isInitialized = true;
        }

        // Imposta il listener per il pulsante di ricarica del santo
        binding.reloadSaintButton.setOnClickListener(v -> {
            Log.d("HomeFragment", "Refresh button clicked");
            reloadSaintOfDay();
        });

        ricorrenzaViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                // Mostra un indicatore di caricamento
            } else {
                // Nascondi l'indicatore di caricamento
            }
        });

        nestedScrollView = binding.nestedScrollView;

        setupScrollListener();

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                updateComponentsState();
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        setupDateDisplay();
        loadBackgroundImage();
        setupPersonalInfo();
        setupSaintsList();
        updateComponentsState();
        setupPersonalInfoCard();
        updateInitialVisibility();
        setInitialSaintsListState();
        observeRicorrenze();
        setupCalendar();
        //setupBottomNavigation();
        setupMigrationButton();
        setupCalendarCollapse();
        setupCalendarAnimation();
    }

    private void setupCalendar() {
        MotionLayout motionLayout = binding.motionLayout;

        motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
                // Nascondi temporaneamente le altre card durante l'animazione
                binding.saintCard.setVisibility(View.INVISIBLE);
                binding.cardPersonalInfo.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {
                updateCalendarTextSize(progress);
            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                isCalendarExpanded = (currentId == R.id.expanded);
                // Mostra nuovamente le altre card
                binding.saintCard.setVisibility(View.VISIBLE);
                binding.cardPersonalInfo.setVisibility(View.VISIBLE);
                if (isCalendarExpanded) {
                    scheduleCalendarCollapse();
                }
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {}
        });

        binding.calendarCard.setOnClickListener(v -> toggleCalendarState());
    }

    private void toggleCalendarState() {
        MotionLayout motionLayout = binding.motionLayout;
        if (motionLayout.getCurrentState() == R.id.expanded) {
            motionLayout.transitionToState(R.id.collapsed);
        } else {
            motionLayout.transitionToState(R.id.expanded);
        }

        isCalendarExpanded = !isCalendarExpanded;
        if (isCalendarExpanded) {
            scheduleCalendarCollapse();
        } else {
            collapseHandler.removeCallbacksAndMessages(null);
        }
    }

    private void setupCalendarAnimation() {
        calendarAnimator = ValueAnimator.ofFloat(0f, 1f);
        calendarAnimator.setDuration(300); // Durata dell'animazione in ms
        calendarAnimator.addUpdateListener(animation -> {
            float progress = (float) animation.getAnimatedValue();
            updateCalendarState(progress);
        });

        binding.calendarCard.setOnClickListener(v -> toggleCalendarState());
    }

    private void updateCalendarState(float progress) {
        // Aggiorna le dimensioni del testo
        updateCalendarTextSize(progress);

        // Aggiorna la larghezza del calendario
        ViewGroup.LayoutParams params = binding.calendarCard.getLayoutParams();
        if (params instanceof ConstraintLayout.LayoutParams) {
            ConstraintLayout.LayoutParams constraintParams = (ConstraintLayout.LayoutParams) params;
            int startWidth = getResources().getDisplayMetrics().widthPixels - 32; // Larghezza massima meno i margini
            int endWidth = getResources().getDimensionPixelSize(R.dimen.calendar_collapsed_width);
            constraintParams.width = (int) (startWidth + (endWidth - startWidth) * progress);
            binding.calendarCard.setLayoutParams(constraintParams);
        }

        // Aggiorna i margini
        int expandedMargin = getResources().getDimensionPixelSize(R.dimen.calendar_expanded_margin);
        int collapsedMargin = getResources().getDimensionPixelSize(R.dimen.calendar_collapsed_margin);
        int currentMargin = (int) (expandedMargin + (collapsedMargin - expandedMargin) * progress);
        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) binding.calendarCard.getLayoutParams();
        marginParams.setMargins(currentMargin, currentMargin, currentMargin, currentMargin);
        binding.calendarCard.setLayoutParams(marginParams);

        // Gestione dell'elevazione
        float expandedElevation = 8f;
        float collapsedElevation = 2f;
        float currentElevation = expandedElevation + (collapsedElevation - expandedElevation) * progress;
        binding.calendarCard.setCardElevation(currentElevation);
    }

    private void updateCalendarTextSize(float progress) {
        float expandedWeekdaySize = getResources().getDimension(R.dimen.calendar_expanded_weekday_text_size);
        float collapsedWeekdaySize = getResources().getDimension(R.dimen.calendar_collapsed_weekday_text_size);
        float expandedDaySize = getResources().getDimension(R.dimen.calendar_expanded_day_text_size);
        float collapsedDaySize = getResources().getDimension(R.dimen.calendar_collapsed_day_text_size);
        float expandedMonthSize = getResources().getDimension(R.dimen.calendar_expanded_month_text_size);
        float collapsedMonthSize = getResources().getDimension(R.dimen.calendar_collapsed_month_text_size);

        float weekdaySize = expandedWeekdaySize + (collapsedWeekdaySize - expandedWeekdaySize) * progress;
        float daySize = expandedDaySize + (collapsedDaySize - expandedDaySize) * progress;
        float monthSize = expandedMonthSize + (collapsedMonthSize - expandedMonthSize) * progress;

        binding.tvWeekday.setTextSize(TypedValue.COMPLEX_UNIT_PX, weekdaySize);
        binding.tvDay.setTextSize(TypedValue.COMPLEX_UNIT_PX, daySize);
        binding.tvMonth.setTextSize(TypedValue.COMPLEX_UNIT_PX, monthSize);

        // Gestione dell'alpha
        float alpha = 1f - (0.3f * progress);
        binding.tvWeekday.setAlpha(alpha);
        binding.tvMonth.setAlpha(alpha);
    }

    private void scheduleCalendarCollapse() {
        collapseHandler.removeCallbacksAndMessages(null);
        collapseHandler.postDelayed(() -> {
            if (isAdded() && binding != null && isCalendarExpanded) {
                binding.motionLayout.transitionToEnd();
                isCalendarExpanded = false;
            }
        }, COLLAPSE_DELAY);
    }

    private void setupCalendarCollapse() {
        Log.d(TAG, "setupCalendarCollapse: Configurazione del collasso del calendario");

        if (motionLayout == null) {
            Log.e(TAG, "setupCalendarCollapse: MotionLayout è null");
            return;
        }

        motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
                Log.d(TAG, "onTransitionStarted: Transizione iniziata da " + startId + " a " + endId);
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {
                Log.d(TAG, "onTransitionChange: Progresso della transizione: " + progress);
                updateCalendarTextSize(progress);
            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                Log.d(TAG, "onTransitionCompleted: Transizione completata. Stato corrente: " + currentId);
                if (currentId == R.id.expanded) {
                    scheduleCalendarCollapse();
                }
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {
                Log.d(TAG, "onTransitionTrigger: Trigger " + triggerId + " attivato");
            }
        });

        binding.calendarCard.setOnClickListener(v -> {
            Log.d(TAG, "Calendario cliccato");
            if (motionLayout.getCurrentState() == R.id.collapsed) {
                Log.d(TAG, "Tentativo di espandere il calendario");
                motionLayout.transitionToState(R.id.expanded);
            } else {
                Log.d(TAG, "Tentativo di collassare il calendario");
                motionLayout.transitionToState(R.id.collapsed);
            }
        });
    }

    private void setupMigrationButton() {
        binding.btnMigrateImages.setOnClickListener(v -> {
            startImageMigration();
        });
    }

    private void startImageMigration() {
        Log.d("HomeFragment", "Starting image migration");
        new Thread(() -> {
            imageHandler.migrateImages(ricorrenzaViewModel.getRepository());
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Migrazione immagini completata", Toast.LENGTH_SHORT).show();
                Log.d("HomeFragment", "Image migration completed");
            });
        }).start();
    }

    private void observeRicorrenze() {
        ricorrenzaViewModel.getRicorrenzeDelGiorno().observe(getViewLifecycleOwner(), this::updateRicorrenzeList);
        //ricorrenzaViewModel.getRicorrenzeReligiose().observe(getViewLifecycleOwner(), this::updateRicorrenzeReligiose);
        //ricorrenzaViewModel.getRicorrenzeLaiche().observe(getViewLifecycleOwner(), this::updateRicorrenzeLaiche);
    }

    private void updateRicorrenze(List<Ricorrenza> ricorrenze) {
        if (ricorrenze == null) return;
        Log.d("HomeFragment", "updateRicorrenze chiamato con " + ricorrenze.size() + " ricorrenze");
        updateRicorrenzeList(ricorrenze);
    }

    private void updateRicorrenzeList(List<Ricorrenza> ricorrenze) {
        if (ricorrenze != null && !ricorrenze.isEmpty()) {
            if (ricorrenze.size() > 1) {
                binding.expandCollapseSaintsIcon.setVisibility(View.VISIBLE);
                ricorrenzaAdapter.setRicorrenze(ricorrenze.subList(1, ricorrenze.size()));
            } else {
                binding.expandCollapseSaintsIcon.setVisibility(View.GONE);
                binding.recyclerViewSaints.setVisibility(View.GONE);
            }
        } else {
            binding.expandCollapseSaintsIcon.setVisibility(View.GONE);
            binding.recyclerViewSaints.setVisibility(View.GONE);
        }
        updateComponentsState();
    }

    private void updateSaintOfDay(Ricorrenza saint) {
        if (saint == null) return;
        Log.d("HomeFragment", "Updated saint of day: " + saint.getPrefix() + " " + saint.getNome());
        String saintText = saint.getPrefix() + " " + saint.getNome();
        binding.tvSaintOfDay.setText(saintText);
        binding.tvSaintOfDay.setOnClickListener(v -> navigateToRicorrenzaDetail(saint.getId()));
    }

    private void updateRicorrenzeReligiose(List<Ricorrenza> ricorrenze) {
        Log.d("HomeFragment", "Ricorrenze religiose: " + ricorrenze.size());
        // Aggiorna l'UI per le ricorrenze religiose
    }

    private void updateRicorrenzeLaiche(List<Ricorrenza> ricorrenze) {
        Log.d("HomeFragment", "Ricorrenze laiche: " + ricorrenze.size());
        // Aggiorna l'UI per le ricorrenze laiche
    }

    private void updateInitialVisibility() {
        View personalInfoContent = binding.cardPersonalInfo.findViewById(R.id.personal_info_content);
        if (personalInfoContent != null) {
            personalInfoContent.setVisibility(View.GONE);
        }

        if (binding.recyclerViewSaints != null) {
            binding.recyclerViewSaints.setVisibility(View.GONE);
        }

        updatePersonalInfoIcon();
        updateSaintsListIcon();
    }

    private void loadBackgroundImage() {
        Glide.with(this)
                .load(R.drawable.background_saint)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(binding.backgroundImage);
    }

    private void navigateToRicorrenzaDetail(int ricorrenzaId) {
        if (ricorrenzaId > 0) {
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToRicorrenzaDetailFragment(ricorrenzaId);
            Navigation.findNavController(requireView()).navigate(action);
        } else {
            Toast.makeText(requireContext(), "ID ricorrenza non valido", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupSaintsList() {
        ricorrenzaAdapter = new RicorrenzaAdapter(this::navigateToRicorrenzaDetail, ricorrenzaViewModel);
        binding.recyclerViewSaints.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewSaints.setAdapter(ricorrenzaAdapter);

        binding.expandCollapseSaintsIcon.setOnClickListener(v -> toggleSaintsListExpansion());

        ricorrenzaViewModel.getRicorrenzeDelGiorno().observe(getViewLifecycleOwner(), this::updateRicorrenzeList);
    }

    private void reloadSaintOfDay() {
        Log.d("HomeFragment", "reloadSaintOfDay chiamata");
        ricorrenzaViewModel.refreshRandomSaint();
    }
/*
    private void setupSaintOfDay() {
        binding.reloadSaintButton.setOnClickListener(v -> {
            Log.d("HomeFragment", "Refresh button clicked");
            ricorrenzaViewModel.refreshRandomSaint();
        });

        ricorrenzaViewModel.getCurrentSaint().observe(getViewLifecycleOwner(), this::updateSaintOfDay);
    }

 */

    private void toggleSaintsListExpansion() {
        isSaintsListExpanded = !isSaintsListExpanded;
        binding.recyclerViewSaints.setVisibility(isSaintsListExpanded ? View.VISIBLE : View.GONE);
        binding.expandCollapseSaintsIcon.setImageResource(isSaintsListExpanded ? R.drawable.ic_expand_less : R.drawable.ic_expand_more);
        updateComponentsState();
    }

    private void updateSaintsListIcon() {
        if (binding.expandCollapseSaintsIcon != null) {
            binding.expandCollapseSaintsIcon.setImageResource(isSaintsListExpanded ? R.drawable.ic_expand_less : R.drawable.ic_expand_more);
        }
    }
    /*
    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_overview || itemId == R.id.navigation_help || itemId == R.id.navigation_add) {
                navigateToAddItemFragment();
                return true;
            }
            return false;
        });
    }
 */
     private void navigateToAddItemFragment() {
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToAddItemFragment();
        Navigation.findNavController(requireView()).navigate(action);
    }

    private void setInitialSaintsListState() {
        isSaintsListExpanded = false;
        if (binding.recyclerViewSaints != null) {
            binding.recyclerViewSaints.setVisibility(View.GONE);
        }
        if (ricorrenzaAdapter != null) {
            ricorrenzaAdapter.setCollapsedView(true);
        }
        updateSaintsListIcon();
    }

    private void navigateToEditFragment(int ricorrenzaId) {
        if (ricorrenzaId > 0) {
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToRicorrenzaDetailFragment(ricorrenzaId);
            Navigation.findNavController(requireView()).navigate(action);
        } else {
            Toast.makeText(requireContext(), "ID ricorrenza non valido", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupDateDisplay() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat weekdayFormat = new SimpleDateFormat("EEEE", Locale.ITALIAN);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ITALIAN);

        binding.tvWeekday.setText(weekdayFormat.format(calendar.getTime()));
        binding.tvDay.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        binding.tvMonth.setText(DateUtils.getCurrentMonthNameFull());
    }

    private void showDeleteConfirmationDialog(Ricorrenza ricorrenza) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Conferma cancellazione")
                .setMessage("Sei sicuro di voler cancellare questa ricorrenza?")
                .setPositiveButton("Sì", (dialog, which) -> deleteRicorrenza(ricorrenza))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteRicorrenza(Ricorrenza ricorrenza) {
        ricorrenzaViewModel.deleteRicorrenza(ricorrenza);
    }

    private void observeDeleteResult() {
        ricorrenzaViewModel.getDeleteResult().observe(getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess) {
                Toast.makeText(requireContext(), "Ricorrenza cancellata con successo", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Errore durante la cancellazione", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupPersonalInfo() {
        binding.tvPersonalEvent.setText("compleanno di zia Rosina");

        String todoList = """
                - paga bolletta luce
                - cambia filtro aria della cappa
                - compra regalo per Denise - compleanno fra 5 gg
                - finisci rapporto AZS - consegna fra 3 gg""";
        binding.tvTodoList.setText(todoList);

        String todayEvents = """
                oggi:
                * gym
                * ape con Chicca alle h 18
                + la ricotta è in frigo da 4 giorni""";
        binding.tvTodayEvents.setText(todayEvents);
    }

    private void setupPersonalInfoCard() {
        View header = binding.cardPersonalInfo.findViewById(R.id.tv_personal_info_header);
        if (header != null) {
            header.setOnClickListener(v -> togglePersonalInfoExpansion());
        }
        expandPersonalInfoIcon = binding.cardPersonalInfo.findViewById(R.id.expand_collapse_saints_icon);
    }

    private void togglePersonalInfoExpansion() {
        isPersonalInfoExpanded = !isPersonalInfoExpanded;
        View contentView = binding.cardPersonalInfo.findViewById(R.id.personal_info_content);
        if (contentView != null) {
            if (isPersonalInfoExpanded) {
                // Espansione
                contentView.setVisibility(View.VISIBLE);
                contentView.setAlpha(0f);
                contentView.animate()
                        .alpha(1f)
                        .setDuration(200)
                        .setListener(null);
            } else {
                // Collasso
                contentView.animate()
                        .alpha(0f)
                        .setDuration(200)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                contentView.setVisibility(View.GONE);
                                updateBottomMenuVisibility();
                            }
                        });
            }
        }
        updatePersonalInfoIcon();
        updateComponentsState();
    }

    private void updatePersonalInfoIcon() {
        if (expandPersonalInfoIcon != null) {
            expandPersonalInfoIcon.setRotation(isPersonalInfoExpanded ? 180f : 0f);
        }
    }

    private void setupScrollListener() {
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).handleScroll();
                }
            }
        });
    }

    private boolean isScrolledToTop() {
        return nestedScrollView.getScrollY() == 0;
    }

    private void updateBottomMenuVisibility() {
        boolean allCollapsed = !isPersonalInfoExpanded && !isSaintsListExpanded;
        if (allCollapsed && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateComponentsVisibility(true);
        }
    }

    private void updateComponentsState() {
        boolean allCollapsed = !isCalendarExpanded && !isPersonalInfoExpanded && !isSaintsListExpanded && isScrolledToTop();
        homeViewModel.setAllComponentsCollapsed(allCollapsed);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showBottomNav();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (calendarAnimator != null) {
            calendarAnimator.cancel();
        }
        collapseHandler.removeCallbacksAndMessages(null);
        binding = null;
    }
}