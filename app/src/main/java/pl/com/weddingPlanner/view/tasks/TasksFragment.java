package pl.com.weddingPlanner.view.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.FragmentTasksBinding;
import pl.com.weddingPlanner.view.NavigationActivity;
import pl.com.weddingPlanner.view.model.MainViewModel;

public class TasksFragment extends Fragment {

    private MainViewModel mViewModel;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FragmentTasksBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
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
                        break;
                }
            }).attach();
    }

    private void setListeners() {
        binding.tasksFloatingButton.setOnClickListener(view -> {
            Intent intent;
            switch (viewPager.getCurrentItem()) {
                case 0:
                    //TODO do nowej kategorii
                    intent = new Intent(requireActivity(), NewTaskActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(requireActivity(), NewTaskActivity.class);
                    startActivity(intent);
                    break;
            }
        });
    }
}