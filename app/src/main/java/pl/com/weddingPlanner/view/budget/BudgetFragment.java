package pl.com.weddingPlanner.view.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.FragmentBudgetBinding;
import pl.com.weddingPlanner.view.NavigationActivity;
import pl.com.weddingPlanner.view.category.NewCategoryActivity;

public class BudgetFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FragmentBudgetBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_budget, container, false);

        ((NavigationActivity) requireActivity()).setFragmentToolbar(R.string.header_title_budget);

        initComponents();
        setViewPager();
        attachTabLayoutMediator();
        setListeners();

        return binding.getRoot();
    }

    private void initComponents() {
        viewPager = binding.budgetViewPager;
        tabLayout = binding.budgetTabs;
    }

    private void setViewPager() {
        viewPager.setAdapter(new BudgetAdapter(requireActivity()));
        viewPager.setOffscreenPageLimit(1);
    }

    private void attachTabLayoutMediator() {
        new TabLayoutMediator(tabLayout, viewPager,
            (tab, position) -> {
                switch (position) {
                    case 0:
                        tab.setText(getString(R.string.tab_title_budget_categories));
                        break;
                    case 1:
                        tab.setText(getString(R.string.tab_title_budget_descending));
                        break;
                }
            }).attach();
    }

    private void setListeners() {
        binding.budgetFloatingButton.setOnClickListener(view -> {
            Intent intent;
            switch (viewPager.getCurrentItem()) {
                case 0:
                    intent = new Intent(requireActivity(), NewCategoryActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(requireActivity(), NewBudgetActivity.class);
                    startActivity(intent);
            }
        });
    }
}