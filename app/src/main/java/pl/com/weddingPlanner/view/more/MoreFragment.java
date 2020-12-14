package pl.com.weddingPlanner.view.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.FragmentMoreBinding;
import pl.com.weddingPlanner.util.DebouncedOnClickListener;
import pl.com.weddingPlanner.view.NavigationActivity;
import pl.com.weddingPlanner.view.guests.GuestsActivity;
import pl.com.weddingPlanner.view.settings.SettingsActivity;
import pl.com.weddingPlanner.view.subcontractors.SubcontractorsActivity;

public class MoreFragment extends Fragment {

    private FragmentMoreBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);

        ((NavigationActivity) requireActivity()).setFragmentToolbar(R.string.header_title_more);

        setListeners();

        return binding.getRoot();
    }

    private void setListeners() {
        binding.buttonGuests.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                Intent intent = new Intent(requireActivity(), GuestsActivity.class);
                requireActivity().startActivity(intent);
            }
        });

        binding.buttonSubcontractors.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                Intent intent = new Intent(requireActivity(), SubcontractorsActivity.class);
                requireActivity().startActivity(intent);
            }
        });

        binding.buttonSettings.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                Intent intent = new Intent(requireActivity(), SettingsActivity.class);
                requireActivity().startActivity(intent);
            }
        });
    }

}