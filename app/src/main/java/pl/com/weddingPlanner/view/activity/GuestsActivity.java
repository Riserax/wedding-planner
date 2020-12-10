package pl.com.weddingPlanner.view.activity;

import android.os.Bundle;

import pl.com.weddingPlanner.R;

public class GuestsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guests);
        setToolbarContentWithBackIcon(R.string.header_title_guests, this, R.id.navigation_more);
    }
}
