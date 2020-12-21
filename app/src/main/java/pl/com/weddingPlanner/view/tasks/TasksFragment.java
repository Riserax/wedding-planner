package pl.com.weddingPlanner.view.tasks;

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
import pl.com.weddingPlanner.databinding.FragmentTasksBinding;
import pl.com.weddingPlanner.view.NavigationActivity;
import pl.com.weddingPlanner.view.category.NewCategoryActivity;

public class TasksFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FragmentTasksBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks, container, false);

        ((NavigationActivity) requireActivity()).setFragmentToolbar(R.string.header_title_tasks);

        initComponents();
        setViewPager();
        attachTabLayoutMediator();
        setListeners();

        return binding.getRoot();
    }

    private void initComponents() {
        viewPager = binding.tasksViewPager;
        tabLayout = binding.tasksTabs;
    }

    private void setViewPager() {
        viewPager.setAdapter(new TasksAdapter(requireActivity()));
        viewPager.setOffscreenPageLimit(1);
    }

    private void attachTabLayoutMediator() {
        new TabLayoutMediator(tabLayout, viewPager,
            (tab, position) -> {
                switch (position) {
                    case 0:
                        tab.setText(getString(R.string.tab_title_tasks_categories));
                        break;
                    case 1:
                        tab.setText(getString(R.string.tab_title_tasks_months));
                }
            }).attach();
    }

    private void setListeners() {
        binding.tasksFloatingButton.setOnClickListener(view -> {
            Intent intent;
            switch (viewPager.getCurrentItem()) {
                case 0:
                    intent = new Intent(requireActivity(), NewCategoryActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(requireActivity(), NewTaskActivity.class);
                    startActivity(intent);
            }
        });
    }
}