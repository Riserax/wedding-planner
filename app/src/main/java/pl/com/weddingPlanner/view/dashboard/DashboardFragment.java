package pl.com.weddingPlanner.view.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.view.NavigationActivity;

public class DashboardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((NavigationActivity) requireActivity()).setFragmentWithoutToolbar();

        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }
}