package pl.com.weddingPlanner.view.tasks;

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
import pl.com.weddingPlanner.databinding.FragmentCategoriesBinding;
import pl.com.weddingPlanner.view.enums.CategoryEnum;

import static pl.com.weddingPlanner.view.util.SideBySideListUtil.renderCategoriesButtons;

public class TasksCategoriesFragment extends Fragment {

    private FragmentCategoriesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_categories, container, false);

        renderCategoriesButtons(getContext(), getCategories(), binding.categoriesLeftColumn, binding.categoriesRightColumn);

        return binding.getRoot();
    }

    private List<CategoryEnum> getCategories() {
        return new ArrayList<>(Arrays.asList(CategoryEnum.values()));
    }
}