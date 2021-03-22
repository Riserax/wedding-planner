package pl.com.weddingPlanner.view.tasks.dialog;

import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.StringJoiner;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.DialogTaskNewSubtaskBinding;
import pl.com.weddingPlanner.persistence.entity.SubTask;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.dialog.CustomAlertDialog;
import pl.com.weddingPlanner.view.tasks.TaskDetailsActivity;

public class NewSubTaskDialog extends CustomAlertDialog {

    private final DialogTaskNewSubtaskBinding binding;

    public NewSubTaskDialog(TaskDetailsActivity activity, int taskId) {
        super(activity, R.layout.dialog_task_new_subtask);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_task_new_subtask, null, false);

        setPositiveButton(R.string.dialog_add, (dialog, which) -> {
            addSubTask(activity, taskId);
            activity.setDetails();
        });
        setNegativeButton(R.string.dialog_back, (dialog, which) -> {});

        setView(binding.getRoot()).setCancelable(true);
    }

    private void addSubTask(TaskDetailsActivity activity, int taskId) {
        String name = binding.name.getText().toString();

        if (StringUtils.isNotBlank(name)) {
            SubTask subTask = SubTask.builder()
                    .taskId(taskId)
                    .name(binding.name.getText().toString())
                    .build();

            DAOUtil.insertSubTask(activity, subTask);
            updateTask(activity, taskId);
        }
    }

    private void updateTask(TaskDetailsActivity activity, int taskId) {
        String updatedSubTasksIds = getUpdatedSubTasksIds(activity, taskId);
        DAOUtil.updateTaskSubTasks(activity, updatedSubTasksIds, taskId);
    }

    private String getUpdatedSubTasksIds(TaskDetailsActivity activity, int taskId) {
        String taskSubTasks = DAOUtil.getTaskById(activity, taskId).getSubTasks();
        Integer lastSubTaskId = getLastSubTaskId(activity);

        StringJoiner stringJoiner = new StringJoiner(",");
        stringJoiner.add(taskSubTasks).add(String.valueOf(lastSubTaskId));

        return stringJoiner.toString();
    }

    private Integer getLastSubTaskId(TaskDetailsActivity activity) {
        List<SubTask> allSubTasks = DAOUtil.getAllSubTasks(activity);
        allSubTasks.sort((subTask1, subTask2) -> subTask1.getId().compareTo(subTask2.getId()));

        return allSubTasks.get(allSubTasks.size() - 1).getId();
    }

    @Override
    public void showDialog() {
        super.showTwoButtonDialog();
    }
}
