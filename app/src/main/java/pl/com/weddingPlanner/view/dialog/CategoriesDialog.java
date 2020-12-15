package pl.com.weddingPlanner.view.dialog;

import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.DialogCategoriesBinding;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.CustomAlertDialog;

public class CategoriesDialog extends CustomAlertDialog {

    private DialogCategoriesBinding binding;
    private Map<Integer, String> categories;

    public CategoriesDialog(BaseActivity activity) {
        super(activity, R.layout.dialog_categories);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_categories, null, false);

        setAllCategories();
        initListView();
        setListeners(activity);

        setNeutralButton(R.string.dialog_back, (dialog, which) -> {});

        setView(binding.getRoot()).setCancelable(true);
    }

    private void setAllCategories() {
        Map<Integer, String> categories = new LinkedHashMap<>();
        categories.put(0, "Ceremonia");
        categories.put(1, "Muzyka");
        categories.put(2, "Foto & Wideo");
        categories.put(3, "Strój");
        categories.put(4, "Dekoracje");

        this.categories = categories;
    }

    private void initListView() {
        List<String> categories = new ArrayList<>();
        for (int i = 0; i < this.categories.size(); i++) {
            categories.add(this.categories.get(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_row, categories);
        binding.categoriesList.setAdapter(adapter);
    }

    private void setListeners(BaseActivity activity) {
        binding.categoriesList.setOnItemClickListener((adapterView, view, position, id) -> {
            activity.setFieldText(getSelectedCategory(position), activity.findViewById(R.id.category_name));
            super.hideDialog();
        });
    }

    private String getSelectedCategory(int position) {
        for (Map.Entry<Integer, String> entry : categories.entrySet()) {
            int key = entry.getKey();
            if (position == key)
                return categories.get(key);
        }
        return "";
    }

    @Override
    public void showDialog() {
        super.showOneButtonDialog();
    }
}
