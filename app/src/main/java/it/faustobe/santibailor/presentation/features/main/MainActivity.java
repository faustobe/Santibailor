// MainActivity.java
package it.faustobe.santibailor.presentation.features.main;

import android.content.pm.ApplicationInfo;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.ActivityMainBinding;
import it.faustobe.santibailor.util.SearchPreferences;
import it.faustobe.santibailor.presentation.features.home.HomeViewModel;
import it.faustobe.santibailor.presentation.common.viewmodels.RicorrenzaViewModel;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private HomeViewModel homeViewModel;
    private RicorrenzaViewModel ricorrenzaViewModel;
    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout drawer;
    private NavController navController;
    private BottomNavigationView bottomNav;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable showBottomNavRunnable;
    private boolean isBottomNavVisible = true;
    private static final long SHOW_ANIMATION_DURATION = 300; // 300ms per la riapparizione
    private static final long HIDE_ANIMATION_DURATION = 0; // 0ms per la scomparsa immediata
    private static final long SHOW_DELAY = 300; // 300ms di ritardo prima di mostrare il menu


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate called");
        if (isDebugBuild()) {
            setupStrictMode();
        }
        if (savedInstanceState == null) {
            clearSearchState();
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        bottomNav = binding.bottomNavView;
        showBottomNavRunnable = this::showBottomNav;

        setupNavigation();
        setupBackPressedDispatcher();
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            setupBottomNavigation();
        }
        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_settings)
                .setOpenableLayout(drawer)
                .build();
        NavigationUI.setupWithNavController(bottomNav, navController);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
        navController.addOnDestinationChangedListener(this::onDestinationChanged);
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
        if (!isBottomNavVisible) {
            bottomNav.setVisibility(View.VISIBLE);
            bottomNav.animate()
                    .translationY(0)
                    .setDuration(SHOW_ANIMATION_DURATION)
                    .setInterpolator(new DecelerateInterpolator())
                    .withEndAction(() -> isBottomNavVisible = true)
                    .start();
        }
    }

    private void hideBottomNav() {
        if (isBottomNavVisible) {
            bottomNav.animate()
                    .translationY(bottomNav.getHeight())
                    .setDuration(HIDE_ANIMATION_DURATION)
                    .setInterpolator(new AccelerateInterpolator())
                    .withEndAction(() -> {
                        bottomNav.setVisibility(View.GONE);
                        isBottomNavVisible = false;
                    })
                    .start();
        }
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
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    if (!navController.popBackStack()) {
                        finish();
                    }
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

    public RicorrenzaViewModel getRicorrenzaViewModel() {
        return ricorrenzaViewModel;
    }
}