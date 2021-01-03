package pl.com.weddingPlanner.view.budget;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;
import java.util.List;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityNewBudgetBinding;
import pl.com.weddingPlanner.util.DebouncedOnClickListener;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.dialog.CategoriesDialog;
import pl.com.weddingPlanner.view.dialog.PeopleDialog;
import pl.com.weddingPlanner.view.enums.CategoryTypeEnum;
import pl.com.weddingPlanner.view.util.ComponentsUtil;

public class NewBudgetActivity extends BaseActivity {

    private ActivityNewBudgetBinding binding;

    private List<Integer> selectedPeopleKeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_budget);
        setActivityToolbarContentWithBackIcon(R.string.header_title_budget_new);

        setListeners();
    }

    private void setListeners() {
        setCategoryOnClickListener();
        setPeopleOnClickListener();
        initRootScrollViewListener();
        setOnFocusChangeListener();
    }

    private void setCategoryOnClickListener() {
        binding.categoryLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                new CategoriesDialog(NewBudgetActivity.this, CategoryTypeEnum.BUDGET.name()).showDialog();
            }
        });
    }

    private void setPeopleOnClickListener() {
        binding.peopleLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                new PeopleDialog(NewBudgetActivity.this, selectedPeopleKeys).showDialog();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initRootScrollViewListener() {
        binding.budgetRootScrollView.setOnTouchListener((v, event) -> {
            clearFocusAndHideKeyboard();
            return false;
        });
    }

    private void clearFocusAndHideKeyboard() {
        View focused = getCurrentFocus();
        if (focused instanceof EditText) {
            focused.clearFocus();
            ComponentsUtil.hideKeyboard(this, focused);
        }
    }

    private void setOnFocusChangeListener() {
        final View.OnFocusChangeListener listener = (v, hasFocus) -> {
            if (!hasFocus) {
                ComponentsUtil.hideKeyboard(this, v);
            }
        };

        binding.budgetName.setOnFocusChangeListener(listener);
        binding.budgetCommentName.setOnFocusChangeListener(listener);
    }

    @Override
    public void setDefaultFieldName(TextView view) {
        if (R.id.people_name == view.getId()) {
            view.setText(getResources().getString(R.string.budget_field_payer));
        }
    }

    @Override
    public void setSelectedPeopleKeys(List<Integer> selectedPeopleKeys) {
        this.selectedPeopleKeys = selectedPeopleKeys;
    }
}
