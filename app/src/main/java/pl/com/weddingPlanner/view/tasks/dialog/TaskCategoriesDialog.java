package pl.com.weddingPlanner.view.tasks.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;
import java.util.List;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.DialogTaskCategoriesBinding;
import pl.com.weddingPlanner.view.CustomAlertDialog;
import pl.com.weddingPlanner.view.tasks.NewTaskActivity;

public class TaskCategoriesDialog extends CustomAlertDialog {

    private DialogTaskCategoriesBinding binding;

    public TaskCategoriesDialog(final Context context, NewTaskActivity activity) {
        super(context, R.layout.dialog_task_categories);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_task_categories, null, false);

        initListView();
        setListeners(activity);

        setNeutralButton(R.string.dialog_back, (dialog, which) -> {});

        setView(binding.getRoot()).setCancelable(true);
    }

    private void initListView() {
        List<String> categories = new ArrayList<>();
        categories.add("Ceremonia");
        categories.add("Muzyka");
        categories.add("Foto & Wideo");
        categories.add("Strój");
        categories.add("Dekoracje");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_row, categories);
        binding.taskCategories.setAdapter(adapter);
    }

    private void setListeners(NewTaskActivity activity) {
        binding.taskCategories.setOnItemClickListener((adapterView, view, position, id) -> {
            if (position == 0) {
                activity.setCategory("Ceremonia");
            }
            super.hideDialog();
        });
    }

    public void showDialog() {
        super.showOneButtonDialog();
    }
}
