package pl.com.weddingPlanner.view.subcontractors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.FragmentSubcontractorsCategoriesBinding;
import pl.com.weddingPlanner.enums.CategoryType;
import pl.com.weddingPlanner.util.DAOUtil;

import static pl.com.weddingPlanner.view.util.SideBySideListUtil.renderCategoriesButtons;

public class SubcontractorsCategoriesFragment extends Fragment {

    private FragmentSubcontractorsCategoriesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subcontractors_categories, container, false);

        renderCategoriesButtons(
                this,
                DAOUtil.getAllCategoriesByType(requireContext(), CategoryType.SUBCONTRACTORS.name()),
                binding.sideBySideMenu.leftColumn,
                binding.sideBySideMenu.rightColumn);

        return binding.getRoot();
    }
}
