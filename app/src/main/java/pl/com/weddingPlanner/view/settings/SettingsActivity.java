package pl.com.weddingPlanner.view.settings;

import android.os.Bundle;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.view.BaseActivity;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setToolbarContentWithBackIcon(R.string.header_title_settings, this, R.id.navigation_more);
    }
}