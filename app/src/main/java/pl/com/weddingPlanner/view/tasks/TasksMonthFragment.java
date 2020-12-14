package pl.com.weddingPlanner.view.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.FragmentTasksMonthBinding;

public class TasksMonthFragment extends Fragment {

    private FragmentTasksMonthBinding binding;

    private final String month;

    public TasksMonthFragment(String month) {
        this.month = month;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks_month, container, false);

        setMonthText();

        return binding.getRoot();
    }

    private void setMonthText() {
        binding.month.setText(month);
    }
}