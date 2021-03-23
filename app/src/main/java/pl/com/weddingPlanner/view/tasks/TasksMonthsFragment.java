package pl.com.weddingPlanner.view.tasks;

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

import java.util.Map;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.FragmentTasksMonthsBinding;
import pl.com.weddingPlanner.view.util.TasksUtil;

public class TasksMonthsFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FragmentTasksMonthsBinding binding;

    private Map<Integer, String> months;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks_months, container, false);

        setMonths();
        initComponents();
        setViewPager();
        attachTabLayoutMediator();

        return binding.getRoot();
    }

    private void setMonths() {
        this.months = TasksUtil.getMonthsMap(getContext());
    }

    private void initComponents() {
        viewPager = binding.tasksMonthsViewPager;
        tabLayout = binding.tasksMonthsTabs;
    }

    private void setViewPager() {
        viewPager.setAdapter(new TasksMonthsAdapter(requireActivity(), months));
        viewPager.setOffscreenPageLimit(1);
    }

    private void attachTabLayoutMediator() {
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(months.get(position))).attach();

        int tabId = TasksUtil.getCurrentMonthYearTab(getContext(), months);
        selectTab(tabId);
    }

    private void selectTab(int tabId) {
        TabLayout.Tab tab = tabLayout.getTabAt(tabId);
        tab.select();
    }
}