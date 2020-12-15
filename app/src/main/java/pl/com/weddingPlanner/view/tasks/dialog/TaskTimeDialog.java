package pl.com.weddingPlanner.view.tasks.dialog;

import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.DialogTaskTimeBinding;
import pl.com.weddingPlanner.model.PickedTime;
import pl.com.weddingPlanner.view.CustomAlertDialog;
import pl.com.weddingPlanner.view.tasks.NewTaskActivity;

public class TaskTimeDialog extends CustomAlertDialog {

    private DialogTaskTimeBinding binding;

    public TaskTimeDialog(NewTaskActivity activity, PickedTime pickedTime) {
        super(activity, R.layout.dialog_task_time);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_task_time, null, false);

        setPositiveButton(R.string.dialog_pick, (dialog, which) -> {
            activity.setPickedTime(getPickedTime());
            activity.setFieldText(getSelectedTime(), activity.findViewById(R.id.task_time));
        });
        setNegativeButton(R.string.dialog_back, (dialog, which) -> {});

        setView(binding.getRoot()).setCancelable(true);

        setTimePicker(pickedTime);
    }

    private void setTimePicker(PickedTime pickedTime) {
        binding.taskTimePicker.setIs24HourView(true);

        if (pickedTime != null) {
            binding.taskTimePicker.setHour(pickedTime.getHour());
            binding.taskTimePicker.setMinute(pickedTime.getMinute());
        }
    }

    private String getSelectedTime() {
        int hour = binding.taskTimePicker.getHour();
        int minute = binding.taskTimePicker.getMinute();

        String hourString = hour < 10 ? ("0" + hour) : String.valueOf(hour);
        String minuteString = minute < 10 ? ("0" + minute) : String.valueOf(minute);

        return hourString + ":" + minuteString;
    }

    private PickedTime getPickedTime() {
        return PickedTime.builder()
                .hour(binding.taskTimePicker.getHour())
                .minute(binding.taskTimePicker.getMinute())
                .build();
    }

    @Override
    public void showDialog() {
        super.showTwoButtonDialog();
    }
}
