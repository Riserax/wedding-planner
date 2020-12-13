package pl.com.weddingPlanner.view.tasks;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import java.util.List;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityNewTaskBinding;
import pl.com.weddingPlanner.util.DebouncedOnClickListener;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.tasks.dialog.TaskBookmarksDialog;
import pl.com.weddingPlanner.view.tasks.dialog.TaskCategoriesDialog;
import pl.com.weddingPlanner.view.tasks.dialog.TaskDateDialog;
import pl.com.weddingPlanner.view.tasks.dialog.TaskPeopleDialog;

public class NewTaskActivity extends BaseActivity {

    private ActivityNewTaskBinding binding;

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
                new TaskCategoriesDialog(NewTaskActivity.this).showDialog();
            }
        });

        binding.taskBookmarksLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                new TaskBookmarksDialog(NewTaskActivity.this).showDialog();
            }
        });

        binding.taskPeopleLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                new TaskPeopleDialog(NewTaskActivity.this).showDialog();
            }
        });

        binding.taskDateLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                new TaskDateDialog(NewTaskActivity.this).showDialog();
            }
        });
    }

    public void setFieldText(String text, TextView textView) {
        if (!text.isEmpty()) {
            textView.setText(text);
            setTitleVisibility(textView, true);
        }
    }

    public void setFieldText(List<String> list, TextView textView) {
        StringBuilder bookmarksBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                bookmarksBuilder.append(list.get(i));
            } else {
                bookmarksBuilder.append(list.get(i)).append(" | ");
            }
        }

        if (bookmarksBuilder.length() > 0) {
            textView.setText(bookmarksBuilder.toString());
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
