package pl.com.weddingPlanner.view.tasks.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;
import java.util.List;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.DialogTaskManageSubtasksBinding;
import pl.com.weddingPlanner.persistence.entity.SubTask;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.dialog.CustomAlertDialog;
import pl.com.weddingPlanner.view.tasks.TaskDetailsActivity;
import pl.com.weddingPlanner.view.util.ComponentsUtil;

public class TaskManageSubTasksDialog extends CustomAlertDialog {

    private final DialogTaskManageSubtasksBinding binding;

    public TaskManageSubTasksDialog(TaskDetailsActivity activity, List<SubTask> subTasksList) {
        super(activity, R.layout.dialog_task_manage_subtasks);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_task_manage_subtasks, null, false);

        setPositiveButton(R.string.dialog_delete, (dialog, which) -> {
            removeCheckedSubTasks(activity);
            activity.loadView();
        });
        setNegativeButton(R.string.dialog_back, (dialog, which) -> {});

        setView(binding.getRoot()).setCancelable(true);

        createCheckboxes(subTasksList, activity);
    }

    private void createCheckboxes(List<SubTask> subTasksList, TaskDetailsActivity activity) {
        if (!subTasksList.isEmpty()) {
            for (SubTask subTask : subTasksList) {
                CheckBox subTaskCheckBox = ComponentsUtil.createSubTaskCheckbox(activity, subTask, false);
                binding.checkboxesLayout.addView(subTaskCheckBox);
            }
        } else {
            binding.checkboxesLayout.setVisibility(View.VISIBLE);
        }
    }

    private void removeCheckedSubTasks(TaskDetailsActivity activity) {
        for (Integer idToDelete : getCheckedSubTasksIds()) {
            DAOUtil.deleteSubTaskById(activity, idToDelete);
        }
    }

    private List<Integer> getCheckedSubTasksIds() {
        List<Integer> checkedSubTasksIds = new ArrayList<>();
        int childCount = binding.checkboxesLayout.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View childView = binding.checkboxesLayout.getChildAt(i);
            if (childView instanceof CheckBox) {
                CheckBox subTaskCheckbox = (CheckBox) childView;
                if (subTaskCheckbox.isChecked()) {
                    checkedSubTasksIds.add(subTaskCheckbox.getId());
                }
            }
        }

        return checkedSubTasksIds;
    }

    @Override
    public void showDialog() {
        super.showTwoButtonDialog();
    }
}
