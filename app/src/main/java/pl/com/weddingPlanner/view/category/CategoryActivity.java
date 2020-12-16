package pl.com.weddingPlanner.view.category;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityCategoryBinding;
import pl.com.weddingPlanner.view.BaseActivity;

import static pl.com.weddingPlanner.view.util.SideBySideListUtil.CATEGORY_NAME_EXTRA;

public class CategoryActivity extends BaseActivity {

    private ActivityCategoryBinding binding;

    private int categoryNameId = R.string.header_title_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category);

        setCategoryName();
        setActivityToolbarContentWithBackIcon(categoryNameId);

        setField();
    }

    private void setCategoryName() {
        categoryNameId = getIntent().getExtras().getInt(CATEGORY_NAME_EXTRA, categoryNameId);
    }

    private void setField() {
        String text = binding.message.getText() + " " + getResources().getString(categoryNameId);
        binding.message.setText(text);
    }
}