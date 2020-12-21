package pl.com.weddingPlanner.view.category;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityCategoryBinding;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.budget.NewBudgetActivity;
import pl.com.weddingPlanner.view.tasks.NewTaskActivity;

import static pl.com.weddingPlanner.view.util.SideBySideListUtil.CATEGORY_NAME_EXTRA;
import static pl.com.weddingPlanner.view.util.SideBySideListUtil.FRAGMENT_SOURCE_EXTRA;

public class CategoryActivity extends BaseActivity {

    private final String TASKS_CATEGORIES_FRAGMENT = "class pl.com.weddingPlanner.view.tasks.TasksCategoriesFragment";
    private final String BUDGET_CATEGORIES_FRAGMENT = "class pl.com.weddingPlanner.view.budget.BudgetCategoriesFragment";

    private ActivityCategoryBinding binding;

    private int categoryNameId = R.string.header_title_category;
    private String fragmentClass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category);

        getExtrasAndSetVariables();
        setActivityToolbarContentWithBackIcon(categoryNameId);

        setField();
        setListeners();
    }

    private void getExtrasAndSetVariables() {
        categoryNameId = getIntent().getExtras().getInt(CATEGORY_NAME_EXTRA, categoryNameId);
        fragmentClass = getIntent().getExtras().getString(FRAGMENT_SOURCE_EXTRA, fragmentClass);
    }

    private void setField() {
        String text = binding.message.getText() + " " + getResources().getString(categoryNameId);
        binding.message.setText(text);
    }

    private void setListeners() {
        binding.categoryFloatingButton.setOnClickListener(view -> {
            Intent intent;
            if (TASKS_CATEGORIES_FRAGMENT.equals(fragmentClass)) {
                intent = new Intent(this, NewTaskActivity.class);
                startActivity(intent);
            } else if (BUDGET_CATEGORIES_FRAGMENT.equals(fragmentClass)) {
                intent = new Intent(this, NewBudgetActivity.class);
                startActivity(intent);
            }
        });
    }
}