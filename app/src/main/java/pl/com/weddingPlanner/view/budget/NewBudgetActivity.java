package pl.com.weddingPlanner.view.budget;

import android.os.Bundle;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.view.BaseActivity;

public class NewBudgetActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_budget);
        setActivityToolbarContentWithBackIcon(R.string.header_title_budget_new);
    }
}
