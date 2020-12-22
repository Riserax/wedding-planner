package pl.com.weddingPlanner.view.tasks;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityTaskDetailsBinding;
import pl.com.weddingPlanner.view.BaseActivity;

public class TaskDetailsActivity extends BaseActivity {

    private ActivityTaskDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_details);

        setActivityToolbarContentWithBackIcon(R.string.header_title_tasks_details);
    }
}
