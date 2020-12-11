package pl.com.weddingPlanner.view.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.view.model.MainViewModel;

public class TasksCategoriesFragment extends Fragment {

    private MainViewModel mViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        return inflater.inflate(R.layout.fragment_tasks_categories, container, false);
    }
}