package pl.com.weddingPlanner.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.view.activity.NavigationActivity;
import pl.com.weddingPlanner.view.model.MainViewModel;

public class TasksFragment extends Fragment {

    private MainViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        ((NavigationActivity) requireActivity()).setFragmentToolbar(R.string.header_title_tasks);

        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

}