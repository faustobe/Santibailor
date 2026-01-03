// MainActivity.java
package it.faustobe.santibailor.presentation.features.main;

import android.Manifest;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import dagger.hilt.android.AndroidEntryPoint;
import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.ActivityMainBinding;
import it.faustobe.santibailor.util.LanguageManager;
import it.faustobe.santibailor.util.SearchPreferences;
import it.faustobe.santibailor.presentation.features.home.HomeViewModel;
import it.faustobe.santibailor.presentation.common.viewmodels.RicorrenzaViewModel;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private HomeViewModel homeViewModel;
    private RicorrenzaViewModel ricorrenzaViewModel;
    private DrawerLayout drawer;
    private NavController navController;
    private BottomNavigationView bottomNav;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable showBottomNavRunnable;
    private boolean isBottomNavVisible = true;
    private static final long SHOW_ANIMATION_DURATION = 300; // 300ms per la riapparizione
    private static final long HIDE_ANIMATION_DURATION = 0; // 0ms per la scomparsa immediata
    private static final long SHOW_DELAY = 300; // 300ms di ritardo prima di mostrare il menu
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Installa lo splash screen PRIMA di super.onCreate()
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        // Applica la lingua salvata
        LanguageManager.applyLanguage(this);

        verifyStoragePermissions(this);
        Log.d("MainActivity", "onCreate called");
        //if (isDebugBuild()) {setupStrictMode();}
        if (savedInstanceState == null) {
            clearSearchState();
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        bottomNav = binding.bottomNavView;
        showBottomNavRunnable = this::showBottomNav;

        // Impedisci al bottom nav di rispondere agli insets
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(bottomNav, (v, insets) -> {
            // Non applicare nessun inset, restituisci gli insets non consumati
            return insets;
        });

        // Nascondi la navigation bar di sistema dopo che la view è pronta
        binding.getRoot().post(this::hideSystemUI);

        setupNavigation();
        setupBackPressedDispatcher();
    }

    private void hideSystemUI() {
        // Permetti all'app di disegnare dietro le barre di sistema
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // Imposta i colori delle barre di sistema a completamente trasparente
        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        getWindow().setNavigationBarColor(android.graphics.Color.TRANSPARENT);

        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if (controller != null) {
            // Nascondi sia status bar che navigation bar
            controller.hide(WindowInsetsCompat.Type.systemBars());
            // Imposta il comportamento per quando l'utente fa swipe
            controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // Quando la finestra riprende il focus, ri-nascondi le barre
            hideSystemUI();
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            BottomNavigationView navView = binding.bottomNavView;

            // Setup del BottomNavigationView
            NavigationUI.setupWithNavController(navView, navController);

            navView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home ||
                        itemId == R.id.navigation_add ||
                        itemId == R.id.navigation_dashboard ||
                        itemId == R.id.navigation_settings) {
                    if (itemId == R.id.navigation_add) {
                        // Mostra dialog per scegliere tra Ricorrenza e Impegno
                        showAddItemDialog();
                    } else {
                        navController.popBackStack(itemId, false);
                        navController.navigate(itemId);
                    }
                    return true;
                }
                return false;
            });

            // Setup del DrawerLayout e NavigationView, se presenti
            DrawerLayout drawer = binding.drawerLayout;
            NavigationView navigationView = binding.navView;
            if (drawer != null && navigationView != null) {
                AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_settings)
                        .setOpenableLayout(drawer)
                        .build();
                NavigationUI.setupWithNavController(navigationView, navController);
                navigationView.setNavigationItemSelectedListener(this);
            }

            // Aggiunta del listener per il cambio di destinazione
            navController.addOnDestinationChangedListener(this::onDestinationChanged);
        } else {
            Log.e("MainActivity", "NavHostFragment not found");
        }
    }

    private void openAddItemFragment(String itemType) {
        // Se necessario, puoi passare un tipo di item predefinito
        Bundle args = new Bundle();
        args.putString("itemType", itemType);
        navController.navigate(R.id.action_global_to_add_item, args);
    }

    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.aggiungi_nuovo_elemento);

        String[] items = {getString(R.string.ricorrenza), getString(R.string.impegno), "Lista Spesa", "Nota"};
        builder.setItems(items, (dialog, which) -> {
            if (which == 0) {
                // Ricorrenza
                openAddItemFragment("ricorrenza");
            } else if (which == 1) {
                // Impegno
                openEditImpegnoFragment(-1);
            } else if (which == 2) {
                // Lista Spesa
                openListeSpesaFragment();
            } else {
                // Nota
                openNoteListFragment();
            }
        });

        builder.setNegativeButton(R.string.annulla, null);
        builder.show();
    }

    private void openListeSpesaFragment() {
        navController.navigate(R.id.action_global_to_liste_spesa);
    }

    private void openNoteListFragment() {
        navController.navigate(R.id.action_global_to_note);
    }

    private void openEditImpegnoFragment(int impegnoId) {
        Bundle args = new Bundle();
        args.putInt("impegnoId", impegnoId);
        navController.navigate(R.id.action_global_to_edit_impegno, args);
    }

    private void setHomeAddButtonListener() {
        View homeView = navController.getCurrentDestination().getId() == R.id.navigation_home ?
                findViewById(R.id.addItemFragment) : null;
        if (homeView != null) {
            View addButton = homeView.findViewById(R.id.btn_add_ricorrenza); // Assicurati che questo ID sia corretto
            if (addButton != null) {
                addButton.setOnClickListener(v -> openAddItemFragment("ricorrenza"));
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    private void onDestinationChanged(NavController controller, NavDestination destination, Bundle arguments) {
        Log.d("MainActivity", "Destination changed to: " + destination.getLabel());
        invalidateOptionsMenu();
        updateBottomNavVisibility(destination);
        updateBottomNavSelection(destination.getId());
        supportInvalidateOptionsMenu();
        logMenuState();
        if (destination.getId() == R.id.navigation_home) {
            homeViewModel.refreshComponentsState();
        }
    }

    public void handleScroll() {
        handler.removeCallbacks(showBottomNavRunnable);
        hideBottomNav();
        handler.postDelayed(showBottomNavRunnable, SHOW_DELAY);
    }

    public void showBottomNav() {
        if (bottomNav == null) return;

        // Cancella qualsiasi animazione in corso
        bottomNav.animate().cancel();

        // Imposta visibilità e posizione immediatamente
        bottomNav.setVisibility(View.VISIBLE);
        bottomNav.setTranslationY(0);
        isBottomNavVisible = true;

        Log.d("MainActivity", "Bottom nav shown - visibility: " + bottomNav.getVisibility() + ", translationY: " + bottomNav.getTranslationY());
    }

    public void hideBottomNav() {
        if (bottomNav == null) return;

        // Cancella qualsiasi animazione in corso
        bottomNav.animate().cancel();

        // Nascondi con animazione
        bottomNav.animate()
                .translationY(bottomNav.getHeight())
                .setDuration(HIDE_ANIMATION_DURATION)
                .setInterpolator(new AccelerateInterpolator())
                .withEndAction(() -> {
                    bottomNav.setVisibility(View.GONE);
                    isBottomNavVisible = false;
                    Log.d("MainActivity", "Bottom nav hidden");
                })
                .start();
    }

    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home ||
                    itemId == R.id.navigation_dashboard ||
                    itemId == R.id.navigation_notifications ||
                    itemId == R.id.navigation_settings) {
                navController.navigate(itemId);
                return true;
            }
            return false;
        });
    }

    private void updateBottomNavVisibility(NavDestination destination) {
        int destId = destination.getId();
        Log.d("MainActivity", "updateBottomNavVisibility called for destination: " + destination.getLabel());
        if (destId == R.id.editRicorrenzaFragment || destId == R.id.addItemFragment) {
            hideBottomNav();
            resetBottomNavSelection();
        } else {
            showBottomNav();
            logMenuState();
        }
    }

    public void updateBottomNavSelection(int destinationId) {
        Log.d("MainActivity", "updateBottomNavSelection called with destinationId: " + destinationId);
        if (bottomNav != null) {
            Menu menu = bottomNav.getMenu();
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                if (item.getItemId() == destinationId) {
                    item.setChecked(true);
                    break;
                }
            }
        } else {
            Log.e("MainActivity", "bottomNav is null in updateBottomNavSelection");
        }
    }

    private void logMenuState() {
        if (bottomNav != null) {
            Menu menu = bottomNav.getMenu();
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                Log.d("MainActivity", "Menu item " + item.getTitle() + " checked: " + item.isChecked());
            }
        }
    }

    public void resetBottomNavSelection() {
        if (bottomNav != null) {
            Menu menu = bottomNav.getMenu();
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setChecked(false);
            }
        }
    }

    private void setupBackPressedDispatcher() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    // Se il drawer non è aperto, permettiamo il comportamento di default
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    private boolean isDebugBuild() {
        return (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    private void setupStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }

    private void clearSearchState() {
        SearchPreferences.clearSearchParams(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Log.d("MainActivity", "onNavigationItemSelected called with id: " + id);
        if (id == R.id.navigation_home || id == R.id.navigation_dashboard ||
                id == R.id.navigation_notifications || id == R.id.navigation_settings) {
            navController.navigate(id);
            logMenuState();
            return true;
        }
        return false;
    }

    public void updateComponentsVisibility(boolean allVisible) {
        if (allVisible) {
            showBottomNav();
        }
    }

    // ========== EXPLORATION MODE ==========

    /**
     * Passa alla modalità ESPLORAZIONE: mostra toolbar fissa, nasconde bottom nav
     */
    public void showExplorationMode() {
        Log.d("MainActivity", "============ showExplorationMode() ============");
        Log.d("MainActivity", "Toolbar visibility PRIMA: " + (binding.appBarLayout != null ? binding.appBarLayout.getVisibility() : "null"));

        // Mostra la toolbar di esplorazione
        if (binding.appBarLayout != null) {
            binding.appBarLayout.setVisibility(View.VISIBLE);
            setupExplorationToolbar();
            Log.d("MainActivity", "Toolbar visibility DOPO: " + binding.appBarLayout.getVisibility());
        }

        // Nascondi il bottom navigation
        hideBottomNav();
    }

    /**
     * Torna alla modalità PROMEMORIA: nasconde toolbar, mostra bottom nav
     * @param navigateToHome se true, naviga alla Home; se false, rimane nella schermata corrente
     */
    public void hideExplorationMode(boolean navigateToHome) {
        Log.d("MainActivity", "Ritorno a modalità PROMEMORIA (navigate=" + navigateToHome + ")");

        // Nascondi la toolbar
        if (binding.appBarLayout != null) {
            binding.appBarLayout.setVisibility(View.GONE);
        }

        // Mostra il bottom navigation
        showBottomNav();

        // Naviga alla Home solo se richiesto
        if (navigateToHome && navController != null) {
            navController.navigate(R.id.navigation_home);
        }
    }

    /**
     * Torna alla modalità PROMEMORIA (versione semplice che naviga sempre alla Home)
     */
    public void hideExplorationMode() {
        hideExplorationMode(true);
    }

    /**
     * Imposta la toolbar di esplorazione con data corrente e button listeners
     */
    private void setupExplorationToolbar() {
        // Aggiorna la data
        updateToolbarDate();

        // Click sulla data: torna a Home mantenendo modalità ESPLORAZIONE
        if (binding.toolbarCalendarCard != null) {
            binding.toolbarCalendarCard.setOnClickListener(v -> {
                if (navController != null) {
                    navController.navigate(R.id.navigation_home);
                }
            });
        }

        // Click sui pulsanti: naviga alle funzionalità
        if (binding.toolbarBtnScrivi != null) {
            binding.toolbarBtnScrivi.setOnClickListener(v ->
                navController.navigate(R.id.action_global_to_note));
        }

        if (binding.toolbarBtnOrganizza != null) {
            binding.toolbarBtnOrganizza.setOnClickListener(v ->
                navController.navigate(R.id.action_global_to_organizza));
        }

        if (binding.toolbarBtnRicerca != null) {
            binding.toolbarBtnRicerca.setOnClickListener(v ->
                navController.navigate(R.id.searchFragment));
        }

        if (binding.toolbarBtnRiepilogo != null) {
            binding.toolbarBtnRiepilogo.setOnClickListener(v ->
                navController.navigate(R.id.action_global_to_riepilogo));
        }
    }

    /**
     * Aggiorna la data nella toolbar con la data corrente
     */
    private void updateToolbarDate() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        java.text.SimpleDateFormat weekdayFormat = new java.text.SimpleDateFormat("EEEE", java.util.Locale.ITALIAN);
        java.text.SimpleDateFormat monthFormat = new java.text.SimpleDateFormat("MMMM", java.util.Locale.ITALIAN);

        if (binding.toolbarTvWeekday != null) {
            binding.toolbarTvWeekday.setText(weekdayFormat.format(calendar.getTime()));
        }

        if (binding.toolbarTvDay != null) {
            binding.toolbarTvDay.setText(String.valueOf(calendar.get(java.util.Calendar.DAY_OF_MONTH)));
        }

        if (binding.toolbarTvMonth != null) {
            binding.toolbarTvMonth.setText(monthFormat.format(calendar.getTime()));
        }
    }

    /**
     * Verifica se siamo in modalità esplorazione (toolbar visibile)
     */
    public boolean isExplorationModeActive() {
        return binding.appBarLayout != null && binding.appBarLayout.getVisibility() == View.VISIBLE;
    }

    public RicorrenzaViewModel getRicorrenzaViewModel() {
        return ricorrenzaViewModel;
    }
}