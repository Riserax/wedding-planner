package pl.com.weddingPlanner.view.tasks;

import android.os.Bundle;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.view.BaseActivity;

public class NewTaskActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        setActivityToolbarContentWithBackIcon(R.string.header_title_tasks_new);
    }
}
