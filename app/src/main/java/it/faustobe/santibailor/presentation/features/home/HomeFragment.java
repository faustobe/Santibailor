package it.faustobe.santibailor.presentation.features.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import it.faustobe.santibailor.domain.model.Impegno;
import it.faustobe.santibailor.domain.model.Ricorrenza;
import it.faustobe.santibailor.domain.model.TipoRicorrenza;
import it.faustobe.santibailor.presentation.common.ricorrenze.RicorrenzaAdapter;
import it.faustobe.santibailor.presentation.features.main.MainActivity;
import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentHomeBinding;
import it.faustobe.santibailor.util.DateUtils;
import it.faustobe.santibailor.presentation.common.viewmodels.RicorrenzaViewModel;
import it.faustobe.santibailor.presentation.features.impegni.ImpegniViewModel;
import it.faustobe.santibailor.presentation.features.listespesa.ListeSpesaViewModel;
import it.faustobe.santibailor.presentation.features.listespesa.ListeSpesaAdapter;
import it.faustobe.santibailor.domain.model.ListaSpesa;
import it.faustobe.santibailor.domain.model.OverviewItem;
import it.faustobe.santibailor.domain.model.Nota;
import it.faustobe.santibailor.presentation.features.note.NoteViewModel;
import it.faustobe.santibailor.util.ImageHandler;
//import it.faustobe.santibailor.util.ImageMigrationService;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    ViewModelProvider.Factory viewModelFactory;

    private FragmentHomeBinding binding;
    private RicorrenzaViewModel ricorrenzaViewModel;
    private ImpegniViewModel impegniViewModel;
    private ListeSpesaViewModel listeSpesaViewModel;
    //private ImageMigrationService imageMigrationService;
    private boolean isInitialized = false;
    private RicorrenzaAdapter ricorrenzaAdapter;
    private RicorrenzaAdapter eventiLaiciAdapter;
    private RicorrenzaAdapter eventiPersonaliAdapter;
    private ListeSpesaAdapter listeSpesaAdapter;
    private OverviewAdapter overviewAdapter;
    private NoteViewModel noteViewModel;
    private boolean isPersonalInfoExpanded = false;
    private boolean isSaintsListExpanded = false;
    private boolean isCalendarExpanded; // Inizializzato in setupCalendar() leggendo lo stato salvato
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
    private String currentNavSection = "santi"; // sezione corrente del nav bar
    private SharedPreferences calendarPrefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inizializza SharedPreferences per salvare lo stato del calendario
        calendarPrefs = requireContext().getSharedPreferences("calendar_state", Context.MODE_PRIVATE);

        // Usa questo metodo per ottenere il ViewModel
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        ricorrenzaViewModel = new ViewModelProvider(requireActivity()).get(RicorrenzaViewModel.class);
        impegniViewModel = new ViewModelProvider(this).get(ImpegniViewModel.class);
        listeSpesaViewModel = new ViewModelProvider(this).get(ListeSpesaViewModel.class);
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

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
        }
        // IMPORTANTE: Ri-assegna l'adapter ogni volta che la view viene ricreata
        binding.recyclerViewSaints.setAdapter(ricorrenzaAdapter);
        binding.recyclerViewSaints.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inizializza adapter per Eventi Laici Generali
        if (eventiLaiciAdapter == null) {
            eventiLaiciAdapter = new RicorrenzaAdapter(this::navigateToRicorrenzaDetail, ricorrenzaViewModel);
        }
        // IMPORTANTE: Ri-assegna l'adapter ogni volta che la view viene ricreata
        binding.recyclerViewEventiLaici.setAdapter(eventiLaiciAdapter);
        binding.recyclerViewEventiLaici.setLayoutManager(new LinearLayoutManager(getContext()));

        /* RIMOSSO - eventi personali ora in card_overview
        // Inizializza adapter per Eventi Personali
        if (eventiPersonaliAdapter == null) {
            eventiPersonaliAdapter = new RicorrenzaAdapter(this::navigateToRicorrenzaDetail, ricorrenzaViewModel);
            binding.recyclerViewEventiPersonali.setAdapter(eventiPersonaliAdapter);
            binding.recyclerViewEventiPersonali.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        */

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
        // setupPersonalInfo(); // RIMOSSO - ora usiamo card_overview
        setupSaintsList();
        updateComponentsState();
        // setupPersonalInfoCard(); // RIMOSSO - ora usiamo card_overview
        updateInitialVisibility();
        setInitialSaintsListState();
        observeRicorrenze();
        setupCalendar();
        //setupBottomNavigation();
        setupCalendarCollapse();
        // setupCalendarAnimation(); // RIMOSSO - causava conflitti con setupCalendarCollapse()
        // setupNavBar(); // Rimosso - nav bar verticale eliminato
        // setupTaccuino(); // Rimosso - ora usiamo i pulsanti azioni rapide
        // setupListeSpesa(); // RIMOSSO - ora usiamo la card overview unificata
        setupOverview();
        setupQuickActions();
    }

    /* RIMOSSO - Nav bar verticale eliminato
    private void setupNavBar() {
        View navBar = binding.navBarVertical.getRoot();

        // Ottieni i bottoni dal nav bar
        com.google.android.material.button.MaterialButton btnSanti = navBar.findViewById(R.id.btn_nav_santi);
        com.google.android.material.button.MaterialButton btnEventi = navBar.findViewById(R.id.btn_nav_eventi);
        com.google.android.material.button.MaterialButton btnImpegni = navBar.findViewById(R.id.btn_nav_impegni);
        // com.google.android.material.button.MaterialButton btnTaccuino = navBar.findViewById(R.id.btn_nav_taccuino); // RIMOSSO

        btnSanti.setOnClickListener(v -> showSection("santi"));
        btnEventi.setOnClickListener(v -> showSection("eventi"));
        btnImpegni.setOnClickListener(v -> showSection("impegni"));
        // btnTaccuino.setOnClickListener(v -> showSection("taccuino")); // RIMOSSO
    }
    */

    /* RIMOSSO - Taccuino sostituito con pulsanti azioni rapide
    private void setupTaccuino() {
        // ... codice rimosso ...
    }
    */

    private void showSection(String section) {
        currentNavSection = section;

        // Quando il calendario è collassato, tutte le card sono visibili
        // Il nav bar serve per scrollare alla sezione
        /* RIMOSSO - Taccuino
        if (section.equals("taccuino")) {
            // ... codice rimosso ...
        } else */
        {
            // Scroll alla sezione selezionata
            View targetView = null;
            switch (section) {
                case "santi":
                    targetView = binding.saintCard;
                    break;
                case "eventi":
                    targetView = binding.cardEventiLaici;
                    break;
                /* RIMOSSO - card_personal_info eliminata
                case "impegni":
                    targetView = binding.cardPersonalInfo;
                    // Espandi automaticamente la card
                    if (!isPersonalInfoExpanded) {
                        togglePersonalInfoExpansion();
                    }
                    break;
                */
            }

            if (targetView != null) {
                final View finalView = targetView;
                binding.nestedScrollView.post(() -> {
                    binding.nestedScrollView.smoothScrollTo(0, finalView.getTop());
                });
            }
        }
    }

    private void setupCalendar() {
        MotionLayout motionLayout = binding.motionLayout;

        // Ripristina lo stato salvato del calendario dopo che il layout è pronto
        // Usa jumpToState() per impostare lo stato senza animazione
        motionLayout.post(() -> {
            // Controlla se esiste uno stato salvato
            if (calendarPrefs.contains("is_expanded")) {
                // Usa lo stato salvato
                isCalendarExpanded = calendarPrefs.getBoolean("is_expanded", true);
                Log.d(TAG, "Stato SALVATO trovato: " + (isCalendarExpanded ? "espanso" : "collassato"));
            } else {
                // Prima apertura: default ESPANSO
                isCalendarExpanded = true;
                Log.d(TAG, "Prima apertura: imposto ESPANSO");
            }

            if (isCalendarExpanded) {
                motionLayout.jumpToState(R.id.expanded);
                // Aggiorna le dimensioni del testo per lo stato espanso (progress = 0.0)
                updateCalendarTextSize(0.0f);
            } else {
                motionLayout.jumpToState(R.id.collapsed);
                // Aggiorna le dimensioni del testo per lo stato collassato (progress = 1.0)
                updateCalendarTextSize(1.0f);
            }

            // Forza l'aggiornamento della visibilità dei pulsanti
            updateQuickActionsVisibility();
            Log.d(TAG, "Calendario ripristinato: " + (isCalendarExpanded ? "espanso" : "collassato"));
        });

        // RIMOSSO - Click listener gestito in setupCalendarCollapse() per evitare conflitti
    }

    private void showAllSections() {
        // Mostra tutte le card quando il calendario è espanso
        binding.saintCard.setVisibility(View.VISIBLE);
        // binding.cardEventiLaici.setVisibility(View.VISIBLE); // Gestita da updateEventiLaiciGenerali()
        // binding.cardPersonalInfo.setVisibility(View.VISIBLE); // RIMOSSO - ora usiamo card_overview
        // binding.cardTaccuino.setVisibility(View.GONE); // RIMOSSO - Taccuino sostituito con pulsanti azioni rapide
    }

    // METODO OBSOLETO - causava conflitti con setupCalendarCollapse()
    // Modificava manualmente isCalendarExpanded senza sincronizzare con onTransitionCompleted
    /*
    private void toggleCalendarState() {
        MotionLayout motionLayout = binding.motionLayout;
        if (motionLayout.getCurrentState() == R.id.expanded) {
            motionLayout.transitionToState(R.id.collapsed);
        } else {
            motionLayout.transitionToState(R.id.expanded);
        }

        isCalendarExpanded = !isCalendarExpanded;
        // Rimosso scheduleCalendarCollapse() - il calendario rimane nello stato scelto dall'utente
    }
    */

    // METODO OBSOLETO - non più utilizzato, MotionLayout gestisce l'animazione
    /*
    private void setupCalendarAnimation() {
        calendarAnimator = ValueAnimator.ofFloat(0f, 1f);
        calendarAnimator.setDuration(300); // Durata dell'animazione in ms
        calendarAnimator.addUpdateListener(animation -> {
            float progress = (float) animation.getAnimatedValue();
            updateCalendarState(progress);
        });

        binding.calendarCard.setOnClickListener(v -> toggleCalendarState());
    }
    */

    // METODO OBSOLETO - MotionLayout gestisce automaticamente lo stato
    /*
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
    */

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
                isCalendarExpanded = (currentId == R.id.expanded);

                // Salva lo stato
                calendarPrefs.edit().putBoolean("is_expanded", isCalendarExpanded).apply();
                Log.d(TAG, "Stato salvato: " + (isCalendarExpanded ? "ESPANSO" : "COLLASSATO"));
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

    private void observeRicorrenze() {
        // Osserva il Santo del Giorno (selezionato casualmente)
        ricorrenzaViewModel.getCurrentSaint().observe(getViewLifecycleOwner(), this::updateSaintOfDay);

        // Osserva le ricorrenze religiose (Santi) separatamente dalle laiche
        ricorrenzaViewModel.getRicorrenzeReligiose().observe(getViewLifecycleOwner(), this::updateRicorrenzeReligiose);
        ricorrenzaViewModel.getRicorrenzeLaiche().observe(getViewLifecycleOwner(), this::updateRicorrenzeLaiche);

        // Osserva gli impegni di oggi - RIMOSSO, ora gestito in setupOverview()
        // impegniViewModel.getImpegniOggi().observe(getViewLifecycleOwner(), this::updateImpegniOggi);
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
        if (saint == null || binding == null || !isAdded()) return;
        Log.d("HomeFragment", "Updated saint of day: " + saint.getPrefix() + " " + saint.getNome());
        String saintText = saint.getPrefix() + " " + saint.getNome();
        binding.tvSaintOfDay.setText(saintText);
        binding.tvSaintOfDay.setOnClickListener(v -> navigateToRicorrenzaDetail(saint.getId()));
    }

    private void updateRicorrenzeReligiose(List<Ricorrenza> ricorrenze) {
        if (binding == null || !isAdded()) return;
        Log.d("HomeFragment", "Ricorrenze religiose (Santi): " + (ricorrenze != null ? ricorrenze.size() : 0));
        if (ricorrenze == null || ricorrenze.isEmpty()) {
            binding.expandCollapseSaintsIcon.setVisibility(View.GONE);
            binding.recyclerViewSaints.setVisibility(View.GONE);
            binding.tvSaintOfDay.setText(R.string.no_saints_today);
            return;
        }

        // Ottieni il santo correntemente mostrato come "Santo del Giorno"
        Ricorrenza currentSaint = ricorrenzaViewModel.getCurrentSaint().getValue();

        // Filtra la lista per ESCLUDERE il santo già mostrato come "Santo del Giorno"
        List<Ricorrenza> filteredList = new java.util.ArrayList<>();
        for (Ricorrenza ricorrenza : ricorrenze) {
            if (currentSaint == null || ricorrenza.getId() != currentSaint.getId()) {
                filteredList.add(ricorrenza);
            }
        }

        // Mostra TUTTI gli altri santi nella lista espandibile (escludendo quello già mostrato sopra)
        if (!filteredList.isEmpty()) {
            binding.expandCollapseSaintsIcon.setVisibility(View.VISIBLE);
            ricorrenzaAdapter.setRicorrenze(filteredList);
            Log.d("HomeFragment", "Showing " + filteredList.size() + " saints in expandable list (excluding current saint)");
        } else {
            binding.expandCollapseSaintsIcon.setVisibility(View.GONE);
            binding.recyclerViewSaints.setVisibility(View.GONE);
        }

        updateComponentsState();
    }

    private void updateRicorrenzeLaiche(List<Ricorrenza> ricorrenze) {
        if (binding == null || !isAdded()) return;
        Log.d("HomeFragment", "Ricorrenze laiche/personali: " + (ricorrenze != null ? ricorrenze.size() : 0));
        if (ricorrenze == null || ricorrenze.isEmpty()) {
            updateEventiLaiciGenerali(new java.util.ArrayList<>());
            // updateEventiPersonali(new java.util.ArrayList<>()); // RIMOSSO - card eliminata
            return;
        }

        // Separa eventi laici generali da personali
        // Criterio: SOLO il tipo di ricorrenza (non l'URL dell'immagine)
        List<Ricorrenza> eventiGenerali = new java.util.ArrayList<>();
        List<Ricorrenza> eventiPersonali = new java.util.ArrayList<>();

        for (Ricorrenza ric : ricorrenze) {
            if (ric.getTipoRicorrenzaId() == it.faustobe.santibailor.domain.model.TipoRicorrenza.PERSONALE) {
                eventiPersonali.add(ric);
            } else {
                eventiGenerali.add(ric);
            }
        }

        Log.d("HomeFragment", "Eventi laici generali: " + eventiGenerali.size() + ", personali: " + eventiPersonali.size());
        updateEventiLaiciGenerali(eventiGenerali);
        // updateEventiPersonali(eventiPersonali); // RIMOSSO - card eliminata
    }

    private void updateEventiLaiciGenerali(List<Ricorrenza> eventi) {
        if (binding == null || !isAdded()) return;
        Log.d("HomeFragment", "Eventi laici generali: " + eventi.size());
        if (eventi == null || eventi.isEmpty()) {
            Log.d("HomeFragment", "Setting card_eventi_laici to GONE (empty list)");
            binding.cardEventiLaici.setVisibility(View.GONE);
            return;
        }

        Log.d("HomeFragment", "Setting card_eventi_laici to VISIBLE");
        binding.cardEventiLaici.setVisibility(View.VISIBLE);
        binding.cardEventiLaici.setAlpha(1f); // Assicura che sia completamente visibile
        eventiLaiciAdapter.setRicorrenze(eventi);

        // Forza il layout del RecyclerView
        binding.recyclerViewEventiLaici.requestLayout();

        // Forza l'aggiornamento del layout del MotionLayout
        if (binding.motionLayout != null) {
            binding.motionLayout.requestLayout();
        }

        // Verifica la visibilità dopo un breve delay
        binding.cardEventiLaici.postDelayed(() -> {
            if (binding != null && binding.cardEventiLaici != null) {
                int visibility = binding.cardEventiLaici.getVisibility();
                String visStr = visibility == View.VISIBLE ? "VISIBLE" : (visibility == View.GONE ? "GONE" : "INVISIBLE");
                float alpha = binding.cardEventiLaici.getAlpha();
                Log.d("HomeFragment", "card_eventi_laici check: visibility=" + visStr + ", alpha=" + alpha);

                // Se è diventata GONE o invisibile, forza di nuovo VISIBLE
                if (visibility != View.VISIBLE || alpha < 1f) {
                    Log.d("HomeFragment", "FORCING card_eventi_laici to VISIBLE again!");
                    binding.cardEventiLaici.setVisibility(View.VISIBLE);
                    binding.cardEventiLaici.setAlpha(1f);
                }
            }
        }, 100);

        Log.d("HomeFragment", "Displaying " + eventi.size() + " eventi laici generali");
        for (Ricorrenza e : eventi) {
            Log.d("HomeFragment", "  - " + e.getNomeCompleto());
        }
    }

    /* RIMOSSO - card_personal_info eliminata, eventi/impegni ora in card_overview
    private void updateEventiPersonali(List<Ricorrenza> eventi) {
        if (binding == null || !isAdded()) return;
        Log.d("HomeFragment", "Eventi personali: " + eventi.size());
        if (eventi == null || eventi.isEmpty()) {
            binding.tvEventiPersonaliLabel.setVisibility(View.GONE);
            binding.recyclerViewEventiPersonali.setVisibility(View.GONE);
            return;
        }

        binding.tvEventiPersonaliLabel.setVisibility(View.VISIBLE);
        binding.recyclerViewEventiPersonali.setVisibility(View.VISIBLE);
        eventiPersonaliAdapter.setRicorrenze(eventi);
        Log.d("HomeFragment", "Displaying " + eventi.size() + " eventi personali in Info Personali card");
        for (Ricorrenza e : eventi) {
            Log.d("HomeFragment", "  - " + e.getNomeCompleto());
        }
    }

    private void updateImpegniOggi(List<Impegno> impegni) {
        if (binding == null || !isAdded()) return;
        Log.d("HomeFragment", "Impegni di oggi: " + (impegni != null ? impegni.size() : 0));
        if (impegni == null || impegni.isEmpty()) {
            binding.tvImpegniOggiLabel.setVisibility(View.GONE);
            binding.tvImpegniOggi.setVisibility(View.GONE);
            return;
        }

        // Visualizza in card Info Personali
        binding.tvImpegniOggiLabel.setVisibility(View.VISIBLE);
        binding.tvImpegniOggi.setVisibility(View.VISIBLE);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < impegni.size(); i++) {
            Impegno imp = impegni.get(i);
            sb.append("• ").append(imp.getTitolo());
            if (imp.getPriorita() != null) {
                sb.append(" [").append(imp.getPriorita()).append("]");
            }
            if (i < impegni.size() - 1) {
                sb.append("\n");
            }
        }
        binding.tvImpegniOggi.setText(sb.toString());
        Log.d("HomeFragment", "Displaying " + impegni.size() + " impegni in Info Personali card");
    }
    */

    private void updateInitialVisibility() {
        /* RIMOSSO - card_personal_info eliminata
        View personalInfoContent = binding.cardPersonalInfo.findViewById(R.id.personal_info_content);
        if (personalInfoContent != null) {
            personalInfoContent.setVisibility(View.GONE);
        }
        */

        if (binding.recyclerViewSaints != null) {
            binding.recyclerViewSaints.setVisibility(View.GONE);
        }

        // updatePersonalInfoIcon(); // RIMOSSO - card_personal_info eliminata
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

    /* RIMOSSO - card_personal_info eliminata
    private void setupPersonalInfo() {
        // Osserva impegni di oggi
        impegniViewModel.getImpegniOggi().observe(getViewLifecycleOwner(), impegniOggi -> {
            if (impegniOggi != null && !impegniOggi.isEmpty()) {
                binding.tvPersonalEvent.setText(impegniOggi.size() + " impegni oggi");

                StringBuilder sb = new StringBuilder("Oggi:\n");
                for (int i = 0; i < Math.min(3, impegniOggi.size()); i++) {
                    sb.append("• ").append(impegniOggi.get(i).getTitolo()).append("\n");
                }
                binding.tvTodayEvents.setText(sb.toString());
            } else {
                binding.tvPersonalEvent.setText("Nessun impegno per oggi");
                binding.tvTodayEvents.setText("Goditi la giornata! 😊");
            }
        });

        // Osserva prossimi impegni futuri
        impegniViewModel.getImpegniFuturi(5).observe(getViewLifecycleOwner(), impegniFuturi -> {
            if (impegniFuturi != null && !impegniFuturi.isEmpty()) {
                StringBuilder sb = new StringBuilder("Prossimi impegni:\n");
                for (int i = 0; i < Math.min(5, impegniFuturi.size()); i++) {
                    sb.append("• ").append(impegniFuturi.get(i).getTitolo()).append("\n");
                }
                binding.tvTodoList.setText(sb.toString());
            } else {
                binding.tvTodoList.setText("Nessun impegno programmato.\n\nAggiungi un nuovo impegno con il pulsante '+ Impegno' qui sotto!");
            }
        });
    }
    */

    /* RIMOSSO - card_personal_info eliminata
    private void setupPersonalInfoCard() {
        View header = binding.cardPersonalInfo.findViewById(R.id.tv_personal_info_header);
        if (header != null) {
            header.setOnClickListener(v -> togglePersonalInfoExpansion());
        }
        expandPersonalInfoIcon = binding.cardPersonalInfo.findViewById(R.id.expand_collapse_saints_icon);

        // Long press per aprire la lista completa degli impegni
        binding.cardPersonalInfo.setOnLongClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_global_to_impegni);
            return true;
        });
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
    */

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
        return nestedScrollView != null && nestedScrollView.getScrollY() == 0;
    }

    private void updateBottomMenuVisibility() {
        if (!isAdded()) return;
        boolean allCollapsed = !isSaintsListExpanded; // MODIFICATO - rimosso isPersonalInfoExpanded
        if (allCollapsed && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateComponentsVisibility(true);
        }
    }

    private void updateComponentsState() {
        if (binding == null || !isAdded()) return;
        boolean allCollapsed = !isCalendarExpanded && !isSaintsListExpanded && isScrolledToTop(); // MODIFICATO - rimosso isPersonalInfoExpanded
        homeViewModel.setAllComponentsCollapsed(allCollapsed);
    }

    /* RIMOSSO - card_liste_spesa eliminata, ora usiamo card_overview
    private void setupListeSpesa() {
        // Inizializza adapter con listener per navigare al dettaglio
        listeSpesaAdapter = new ListeSpesaAdapter(new ListeSpesaAdapter.OnListaClickListener() {
            @Override
            public void onListaClick(ListaSpesa lista) {
                // Naviga al dettaglio della lista
                Bundle args = new Bundle();
                args.putInt("listaId", lista.getId());
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_homeFragment_to_dettaglioListaSpesaFragment, args);
            }

            @Override
            public void onDeleteClick(ListaSpesa lista) {
                // Non implementato nella home - solo navigazione
                onListaClick(lista);
            }
        });

        binding.recyclerViewListeSpesa.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewListeSpesa.setAdapter(listeSpesaAdapter);

        // Osserva le liste attive
        listeSpesaViewModel.getListeAttive().observe(getViewLifecycleOwner(), liste -> {
            if (liste != null && !liste.isEmpty()) {
                binding.cardListeSpesa.setVisibility(View.VISIBLE);
                binding.recyclerViewListeSpesa.setVisibility(View.VISIBLE);
                binding.tvNoListe.setVisibility(View.GONE);
                listeSpesaAdapter.setListe(liste);
                Log.d("HomeFragment", "Displaying " + liste.size() + " liste spesa attive");
            } else {
                binding.cardListeSpesa.setVisibility(View.GONE);
                Log.d("HomeFragment", "No active liste spesa found");
            }
        });
    }
    */

    private void setupOverview() {
        // Inizializza adapter per la card overview unificata
        overviewAdapter = new OverviewAdapter(this::onOverviewItemClick);
        binding.recyclerViewOverview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewOverview.setAdapter(overviewAdapter);

        // Cache locali per i dati
        final List<Impegno>[] impegniCache = new List[]{new java.util.ArrayList<>()};
        final List<ListaSpesa>[] listeCache = new List[]{new java.util.ArrayList<>()};
        final List<Nota>[] noteCache = new List[]{new java.util.ArrayList<>()};

        // Observer per impegni
        impegniViewModel.getImpegniOggi().observe(getViewLifecycleOwner(), impegni -> {
            impegniCache[0] = impegni != null ? impegni : new java.util.ArrayList<>();
            updateOverviewCard(impegniCache[0], listeCache[0], noteCache[0]);
        });

        // Observer per liste spesa
        listeSpesaViewModel.getListeAttive().observe(getViewLifecycleOwner(), liste -> {
            listeCache[0] = liste != null ? liste : new java.util.ArrayList<>();
            updateOverviewCard(impegniCache[0], listeCache[0], noteCache[0]);
        });

        // Observer per note
        noteViewModel.getAllNote().observe(getViewLifecycleOwner(), note -> {
            noteCache[0] = note != null ? note : new java.util.ArrayList<>();
            updateOverviewCard(impegniCache[0], listeCache[0], noteCache[0]);
        });
    }

    private void updateOverviewCard(List<Impegno> impegni, List<ListaSpesa> liste, List<Nota> note) {
        if (binding == null || !isAdded()) return;

        List<OverviewItem> items = new java.util.ArrayList<>();

        // Aggiungi impegni di oggi
        for (Impegno impegno : impegni) {
            items.add(new OverviewItem(
                OverviewItem.Type.IMPEGNO,
                impegno.getTitolo(),
                impegno.getId()
            ));
        }

        // Aggiungi liste spesa attive
        for (ListaSpesa lista : liste) {
            items.add(new OverviewItem(
                OverviewItem.Type.LISTA_SPESA,
                lista.getNome(),
                lista.getId()
            ));
        }

        // Aggiungi note
        for (Nota nota : note) {
            String text = nota.getTitolo();
            if (text == null || text.isEmpty()) {
                text = nota.getContenuto() != null && nota.getContenuto().length() > 50
                    ? nota.getContenuto().substring(0, 50) + "..."
                    : nota.getContenuto();
            }
            items.add(new OverviewItem(
                OverviewItem.Type.NOTA,
                text,
                nota.getId()
            ));
        }

        // Aggiorna adapter
        overviewAdapter.setItems(items);

        // Gestisci visibilità della card
        if (items.isEmpty()) {
            binding.cardOverview.setVisibility(View.GONE);
        } else {
            binding.cardOverview.setVisibility(View.VISIBLE);
        }

        Log.d("HomeFragment", "Overview updated: " + impegni.size() + " impegni, " +
              liste.size() + " liste, " + note.size() + " note = " + items.size() + " total");
    }

    private void onOverviewItemClick(OverviewItem item) {
        Bundle args = new Bundle();

        switch (item.getType()) {
            case IMPEGNO:
                // Naviga al dettaglio impegno (edit)
                args.putInt("impegnoId", item.getId());
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_global_to_edit_impegno, args);
                break;

            case LISTA_SPESA:
                // Naviga al dettaglio lista spesa
                args.putInt("listaId", item.getId());
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_homeFragment_to_dettaglioListaSpesaFragment, args);
                break;

            case NOTA:
                // Naviga al dettaglio nota
                args.putInt("notaId", item.getId());
                Navigation.findNavController(requireView())
                    .navigate(R.id.noteDetailFragment, args);
                break;
        }
    }

    private void setupQuickActions() {
        android.util.Log.d("HomeFragment", "setupQuickActions called");

        if (binding == null) {
            android.util.Log.e("HomeFragment", "binding is null!");
            return;
        }
        if (binding.layoutQuickActions == null) {
            android.util.Log.e("HomeFragment", "binding.layoutQuickActions is null!");
            return;
        }
        if (binding.btnScrivi == null) {
            android.util.Log.e("HomeFragment", "binding.btnScrivi is null!");
            return;
        }

        android.util.Log.d("HomeFragment", "All quick action bindings are valid");

        // Pulsante Scrivi - per note/post-it
        binding.btnScrivi.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_global_to_note);
        });

        // Pulsante Organizza - per aggiungere più impegni rapidamente
        binding.btnOrganizza.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_global_to_organizza);
        });

        // Pulsante Ricerca - ricerca globale
        binding.btnRicerca.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.searchFragment);
        });

        // Pulsante Riepilogo - vista settimanale/mensile
        binding.btnRiepilogo.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_global_to_riepilogo);
        });

        // Controlla visibilità dei pulsanti in base allo stato del calendario
        // I pulsanti sono visibili quando il calendario è collassato
        // Inizialmente il calendario è espanso, quindi nascondi i pulsanti
        updateQuickActionsVisibility();
    }

    private void updateQuickActionsVisibility() {
        if (binding != null && binding.layoutQuickActions != null) {
            // I pulsanti sono visibili SOLO quando il calendario è collassato
            // L'alpha è gestito dal MotionLayout (0 quando espanso, 1 quando collassato)
            binding.layoutQuickActions.setVisibility(View.VISIBLE);
            android.util.Log.d("HomeFragment", "Quick actions visibility. Calendar expanded: " + isCalendarExpanded);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume() chiamato");

        // Ricarica i dati SOLO se non è il primo caricamento
        if (isInitialized) {
            ricorrenzaViewModel.forceReloadRicorrenze();
        } else {
            isInitialized = true;
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