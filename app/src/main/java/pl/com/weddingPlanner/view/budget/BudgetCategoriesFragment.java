package pl.com.weddingPlanner.view.budget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.FragmentBudgetCategoriesBinding;
import pl.com.weddingPlanner.view.enums.CategoryEnum;

import static pl.com.weddingPlanner.view.util.SideBySideListUtil.renderCategoriesButtons;

public class BudgetCategoriesFragment extends Fragment {

    private FragmentBudgetCategoriesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_budget_categories, container, false);

        renderCategoriesButtons(this, getCategories(), binding.sideBySideMenu.leftColumn, binding.sideBySideMenu.rightColumn);

        return binding.getRoot();
    }

    private List<CategoryEnum> getCategories() {
        return new ArrayList<>(Arrays.asList(CategoryEnum.values()));
    }
}