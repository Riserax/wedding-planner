package pl.com.weddingPlanner.view.tasks;

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

import java.util.LinkedHashMap;
import java.util.Map;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.FragmentTasksMonthsBinding;
import pl.com.weddingPlanner.view.model.MainViewModel;

public class TasksMonthsFragment extends Fragment {

    private MainViewModel mViewModel;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FragmentTasksMonthsBinding binding;

    private Map<Integer, String> months;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks_months, container, false);

        setMonths();
        initComponents();
        setViewPager();
        attachTabLayoutMediator();

        return binding.getRoot();
    }

    private void setMonths() {
        Map<Integer, String> months = new LinkedHashMap<>();
        months.put(0, getResources().getString(R.string.month_january));
        months.put(1, getResources().getString(R.string.month_february));
        months.put(2, getResources().getString(R.string.month_march));
        months.put(3, getResources().getString(R.string.month_april));
        months.put(4, getResources().getString(R.string.month_may));
        months.put(5, getResources().getString(R.string.month_june));
        months.put(6, getResources().getString(R.string.month_july));
        months.put(7, getResources().getString(R.string.month_august));
        months.put(8, getResources().getString(R.string.month_september));
        months.put(9, getResources().getString(R.string.month_october));
        months.put(10, getResources().getString(R.string.month_november));
        months.put(11, getResources().getString(R.string.month_december));

        this.months = months;
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
    }
}