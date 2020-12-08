package pl.com.WeddingPlanner.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pl.com.WeddingPlanner.R;

public class NavigationActivity extends AppCompatActivity {

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
    }
}
