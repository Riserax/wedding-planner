package pl.com.weddingPlanner.view.subcontractors;

import android.os.Bundle;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.view.BaseActivity;

public class SubcontractorsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcontractors);
        setToolbarContentWithBackIcon(R.string.header_title_subcontractors, this, R.id.navigation_more);
    }
}