package pl.com.weddingPlanner.view.category;

import android.os.Bundle;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.view.BaseActivity;

public class NewCategoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);
        setActivityToolbarContentWithBackIcon(R.string.header_title_category_new);
    }
}