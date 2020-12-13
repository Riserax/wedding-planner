package pl.com.weddingPlanner.view.tasks;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityNewTaskBinding;
import pl.com.weddingPlanner.util.DebouncedOnClickListener;
import pl.com.weddingPlanner.view.BaseActivity;

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
    }

    public void setCategory(String category) {
        binding.taskCategoryName.setText(category);
    }
}
