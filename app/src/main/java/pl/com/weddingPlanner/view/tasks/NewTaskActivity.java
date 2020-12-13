package pl.com.weddingPlanner.view.tasks;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import java.util.List;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityNewTaskBinding;
import pl.com.weddingPlanner.util.DebouncedOnClickListener;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.tasks.dialog.TaskBookmarksDialog;
import pl.com.weddingPlanner.view.tasks.dialog.TaskCategoriesDialog;

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
                new TaskCategoriesDialog(NewTaskActivity.this, NewTaskActivity.this).showDialog();
            }
        });

        binding.taskBookmarksLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                new TaskBookmarksDialog(NewTaskActivity.this, NewTaskActivity.this).showDialog();
            }
        });
    }

    public void setCategory(String category) {
        binding.taskCategoryName.setText(category);
    }

    public void setBookmarks(List<String> bookmarks) {
        StringBuilder bookmarksBuilder = new StringBuilder();
        for (int i = 0; i < bookmarks.size(); i++) {
            if (i == bookmarks.size() - 1) {
                bookmarksBuilder.append(bookmarks.get(i));
            } else {
                bookmarksBuilder.append(bookmarks.get(i)).append(" | ");
            }
        }

        if (bookmarksBuilder.length() > 0) {
            binding.taskBookmarksName.setText(bookmarksBuilder.toString());
        } else {
            binding.taskBookmarksName.setText(getResources().getText(R.string.task_field_bookmarks));
        }
    }
}
