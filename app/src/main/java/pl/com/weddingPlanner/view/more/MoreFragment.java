package pl.com.weddingPlanner.view.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.FragmentMoreBinding;
import pl.com.weddingPlanner.view.NavigationActivity;
import pl.com.weddingPlanner.view.settings.SettingsActivity;
import pl.com.weddingPlanner.view.subcontractors.SubcontractorsActivity;
import pl.com.weddingPlanner.view.model.MainViewModel;
import pl.com.weddingPlanner.util.DebouncedOnClickListener;
import pl.com.weddingPlanner.view.guests.GuestsActivity;

public class MoreFragment extends Fragment {

    private MainViewModel mViewModel;

    private FragmentMoreBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);

        ((NavigationActivity) requireActivity()).setFragmentToolbar(R.string.header_title_more);

        setListeners();

        return binding.getRoot();
    }

    private void setListeners() {
        binding.buttonGuests.setOnClickListener(new DebouncedOnClickListener(1000) {
            @Override
            public void onDebouncedClick(View v) {
                Intent intent = new Intent(requireActivity(), GuestsActivity.class);
                requireActivity().startActivity(intent);
            }
        });

        binding.buttonSubcontractors.setOnClickListener(new DebouncedOnClickListener(1000) {
            @Override
            public void onDebouncedClick(View v) {
                Intent intent = new Intent(requireActivity(), SubcontractorsActivity.class);
                requireActivity().startActivity(intent);
            }
        });

        binding.buttonSettings.setOnClickListener(new DebouncedOnClickListener(1000) {
            @Override
            public void onDebouncedClick(View v) {
                Intent intent = new Intent(requireActivity(), SettingsActivity.class);
                requireActivity().startActivity(intent);
            }
        });
    }

}