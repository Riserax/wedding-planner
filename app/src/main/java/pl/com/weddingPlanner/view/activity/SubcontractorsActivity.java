package pl.com.weddingPlanner.view.activity;

import android.os.Bundle;

import pl.com.weddingPlanner.R;

public class SubcontractorsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcontractors);
        setToolbarContentWithBackIcon(R.string.header_title_subcontractors, this, R.id.navigation_more);
    }
}