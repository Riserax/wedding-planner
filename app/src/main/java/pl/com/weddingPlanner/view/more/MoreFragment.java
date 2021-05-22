package pl.com.weddingPlanner.view.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.FragmentMoreBinding;
import pl.com.weddingPlanner.view.NavigationActivity;
import pl.com.weddingPlanner.enums.MenuMore;

import static pl.com.weddingPlanner.view.util.SideBySideListUtil.renderButtons;

public class MoreFragment extends Fragment {

    private FragmentMoreBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);

        ((NavigationActivity) requireActivity()).setFragmentToolbar(R.string.header_title_more);

        renderButtons(
                this,
                getMenuItems(),
                binding.sideBySideMenu.leftColumn,
                binding.sideBySideMenu.rightColumn);

        return binding.getRoot();
    }

    private List<MenuMore> getMenuItems() {
        return new ArrayList<>(Arrays.asList(MenuMore.values()));
    }
}