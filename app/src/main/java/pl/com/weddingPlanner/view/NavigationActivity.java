package pl.com.weddingPlanner.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pl.com.weddingPlanner.R;

public class NavigationActivity extends BaseActivity {

    public static final String FRAGMENT_TO_LOAD_ID = "fragmentToLoad";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNavigation();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initNavigation() {
        setContentView(R.layout.activity_navigation);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        navController.navigate(R.id.navigation_dashboard);

        loadAppropriateFragment(navController);
    }

    private void loadAppropriateFragment(NavController navController) {
        try {
            int id = getIntent().getExtras().getInt(FRAGMENT_TO_LOAD_ID);
            if (id == 0) return;

            navController.navigate(id);
        } catch (NullPointerException ignored) {}
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return onKeyDownLogoutDialog() || super.onKeyDown(keyCode, event);

        return super.onKeyDown(keyCode, event);
    }

    private boolean onKeyDownLogoutDialog() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        if (navController.getCurrentDestination() != null
                && (navController.getCurrentDestination().getId() == R.id.navigation_dashboard)) {

            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);

            return true;
        }

        return false;
    }
}
