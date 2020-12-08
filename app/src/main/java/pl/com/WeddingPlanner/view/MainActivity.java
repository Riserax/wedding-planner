package pl.com.WeddingPlanner.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        goToActivity();
    }

    private void goToActivity() {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }
}