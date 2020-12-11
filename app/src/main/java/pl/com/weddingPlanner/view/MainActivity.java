package pl.com.weddingPlanner.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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