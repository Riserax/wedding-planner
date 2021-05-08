package pl.com.weddingPlanner.view.tasks.dialog;

import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.DialogTimeBinding;
import pl.com.weddingPlanner.model.PickedTime;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.dialog.CustomAlertDialog;
import pl.com.weddingPlanner.view.tasks.NewTaskActivity;

public class TaskTimeDialog extends CustomAlertDialog {

    private final DialogTimeBinding binding;

    public TaskTimeDialog(BaseActivity activity, PickedTime pickedTime) {
        super(activity, R.layout.dialog_time);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_time, null, false);

        setPositiveButton(R.string.dialog_pick, (dialog, which) -> {
            activity.setPickedTime(getPickedTime());
            activity.setFieldText(getSelectedTime(), getTextView(activity));
        });
        setNegativeButton(R.string.dialog_back, (dialog, which) -> {});

        setView(binding.getRoot()).setCancelable(true);

        setTimePicker(pickedTime);
    }

    private TextView getTextView(BaseActivity activity) {
        TextView textView;

        if (NewTaskActivity.class == activity.getClass()) {
            textView = activity.findViewById(R.id.task_time);
        } else {
            textView = activity.findViewById(R.id.wedding_time);
        }

        return textView;
    }

    private void setTimePicker(PickedTime pickedTime) {
        binding.timePicker.setIs24HourView(true);

        if (pickedTime != null) {
            binding.timePicker.setHour(pickedTime.getHour());
            binding.timePicker.setMinute(pickedTime.getMinute());
        }
    }

    private String getSelectedTime() {
        int hour = binding.timePicker.getHour();
        int minute = binding.timePicker.getMinute();

        String hourString = hour < 10 ? ("0" + hour) : String.valueOf(hour);
        String minuteString = minute < 10 ? ("0" + minute) : String.valueOf(minute);

        return hourString + ":" + minuteString;
    }

    private PickedTime getPickedTime() {
        return PickedTime.builder()
                .hour(binding.timePicker.getHour())
                .minute(binding.timePicker.getMinute())
                .build();
    }

    @Override
    public void showDialog() {
        super.showTwoButtonDialog();
    }
}
