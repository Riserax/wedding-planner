package pl.com.weddingPlanner.view.guests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.view.model.MainViewModel;

public class GuestsListFragment extends Fragment {

    private MainViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        return inflater.inflate(R.layout.fragment_guests_list, container, false);
    }

}