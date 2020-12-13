package pl.com.weddingPlanner.view.tasks.dialog;

import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.DialogTaskDateBinding;
import pl.com.weddingPlanner.view.CustomAlertDialog;
import pl.com.weddingPlanner.view.tasks.NewTaskActivity;

public class TaskDateDialog extends CustomAlertDialog {

    private DialogTaskDateBinding binding;

    public TaskDateDialog(NewTaskActivity activity) {
        super(activity, R.layout.dialog_task_date);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_task_date, null, false);

        setPositiveButton(R.string.dialog_pick, (dialog, which) -> activity.setFieldText(getSelectedDate(), activity.findViewById(R.id.task_date)));
        setNegativeButton(R.string.dialog_back, (dialog, which) -> {});

        setView(binding.getRoot()).setCancelable(true);
    }

    private String getSelectedDate() {
        String dayOfMonth = String.valueOf(binding.taskDatePicker.getDayOfMonth());
        String month = String.valueOf(binding.taskDatePicker.getMonth());
        String year = String.valueOf(binding.taskDatePicker.getYear());

        return dayOfMonth + "-" + month + "-" + year;
    }

    public void showDialog() {
        super.showTwoButtonDialog();
    }
}
