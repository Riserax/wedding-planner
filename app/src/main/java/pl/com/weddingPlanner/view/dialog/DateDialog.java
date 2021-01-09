package pl.com.weddingPlanner.view.dialog;

import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.DialogDateBinding;
import pl.com.weddingPlanner.model.PickedDate;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.tasks.NewTaskActivity;

public class DateDialog extends CustomAlertDialog {

    private DialogDateBinding binding;

    public DateDialog(BaseActivity activity, PickedDate pickedDate) {
        super(activity, R.layout.dialog_date);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_date, null, false);

        setPositiveButton(R.string.dialog_pick, (dialog, which) -> {
            activity.setPickedDate(getPickedDate());
            activity.setFieldText(getSelectedDate(), getTextView(activity));
        });
        setNegativeButton(R.string.dialog_back, (dialog, which) -> {});

        setView(binding.getRoot()).setCancelable(true);

        setDatePicker(pickedDate);
    }

    private TextView getTextView(BaseActivity activity) {
        TextView textView;

        if (NewTaskActivity.class == activity.getClass()) {
            textView = activity.findViewById(R.id.task_date);
        } else {
            textView = activity.findViewById(R.id.payment_date);
        }

        return textView;
    }

    private void setDatePicker(PickedDate pickedDate) {
        if (pickedDate != null)
            binding.taskDatePicker.updateDate(pickedDate.getYear(), pickedDate.getMonth(), pickedDate.getDayOfMonth());
    }

    private String getSelectedDate() {
        String year = String.valueOf(binding.taskDatePicker.getYear());
        int month = binding.taskDatePicker.getMonth() + 1;
        int dayOfMonth = binding.taskDatePicker.getDayOfMonth();

        String monthString = month < 10 ? ("0" + month) : String.valueOf(month);
        String dayString = dayOfMonth < 10 ? ("0" + dayOfMonth) : String.valueOf(dayOfMonth);

        return year + "-" + monthString + "-" + dayString;
    }

    private PickedDate getPickedDate() {
        return PickedDate.builder()
                .year(binding.taskDatePicker.getYear())
                .month(binding.taskDatePicker.getMonth())
                .dayOfMonth(binding.taskDatePicker.getDayOfMonth())
                .build();
    }

    @Override
    public void showDialog() {
        super.showTwoButtonDialog();
    }
}
