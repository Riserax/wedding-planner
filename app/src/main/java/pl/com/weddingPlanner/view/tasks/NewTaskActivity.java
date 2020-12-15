package pl.com.weddingPlanner.view.tasks;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import java.util.List;

import lombok.Setter;
import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityNewTaskBinding;
import pl.com.weddingPlanner.model.PickedDate;
import pl.com.weddingPlanner.model.PickedTime;
import pl.com.weddingPlanner.util.DebouncedOnClickListener;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.tasks.dialog.TaskBookmarksDialog;
import pl.com.weddingPlanner.view.tasks.dialog.TaskCategoriesDialog;
import pl.com.weddingPlanner.view.tasks.dialog.TaskDateDialog;
import pl.com.weddingPlanner.view.tasks.dialog.TaskPeopleDialog;
import pl.com.weddingPlanner.view.tasks.dialog.TaskTimeDialog;
import pl.com.weddingPlanner.view.util.ComponentsUtil;

public class NewTaskActivity extends BaseActivity {

    private ActivityNewTaskBinding binding;

    @Setter
    private List<Integer> selectedBookmarksKeys;
    @Setter
    private List<Integer> selectedPeopleKeys;
    @Setter
    private PickedDate pickedDate;
    @Setter
    private PickedTime pickedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_task);
        setActivityToolbarContentWithBackIcon(R.string.header_title_tasks_new);

        setListeners();
    }

    private void setListeners() {
        binding.taskCategoryLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                new TaskCategoriesDialog(NewTaskActivity.this).showDialog();
            }
        });

        binding.taskBookmarksLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                new TaskBookmarksDialog(NewTaskActivity.this, selectedBookmarksKeys).showDialog();
            }
        });

        binding.taskPeopleLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                new TaskPeopleDialog(NewTaskActivity.this, selectedPeopleKeys).showDialog();
            }
        });

        binding.taskDateLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                new TaskDateDialog(NewTaskActivity.this, pickedDate).showDialog();
            }
        });

        binding.taskTimeLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                new TaskTimeDialog(NewTaskActivity.this, pickedTime).showDialog();
            }
        });

        initRootScrollViewListener();
        setOnFocusChangeListener();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initRootScrollViewListener() {
        binding.tasksRootScrollView.setOnTouchListener((v, event) -> {
            clearFocusAndHideKeyboard();
            return false;
        });
    }

    private void clearFocusAndHideKeyboard() {
        View focused = getCurrentFocus();
        if (focused instanceof EditText) {
            focused.clearFocus();
            ComponentsUtil.hideKeyboard(this, focused);
        }
    }

    private void setOnFocusChangeListener() {
        final View.OnFocusChangeListener listener = (v, hasFocus) -> {
            if (!hasFocus) {
                ComponentsUtil.hideKeyboard(this, v);
            }
        };

        binding.taskName.setOnFocusChangeListener(listener);
        binding.taskDescriptionName.setOnFocusChangeListener(listener);
    }

    public void setFieldText(String text, TextView textView) {
        if (!text.isEmpty()) {
            textView.setText(text);
            setTitleVisibility(textView, true);
        } else {
            setDefaultFieldName(textView);
            setTitleVisibility(textView, false);
        }
    }

    private void setTitleVisibility(TextView view, boolean visible) {
        switch (view.getId()) {
            case R.id.task_category_name:
                binding.taskCategoryTitle.setVisibility(visible ? View.VISIBLE : View.GONE);
                break;
            case R.id.task_bookmarks_name:
                binding.taskBookmarksTitle.setVisibility(visible ? View.VISIBLE : View.GONE);
                break;
            case R.id.task_people_name:
                binding.taskPeopleTitle.setVisibility(visible ? View.VISIBLE : View.GONE);
                break;
        }
    }

    private void setDefaultFieldName(TextView view) {
        switch (view.getId()) {
            case R.id.task_category_name:
                view.setText(getResources().getString(R.string.task_field_category));
                break;
            case R.id.task_bookmarks_name:
                view.setText(getResources().getString(R.string.task_field_bookmarks));
                break;
            case R.id.task_people_name:
                view.setText(getResources().getString(R.string.task_field_people));
                break;
        }
    }
}
