package pl.com.weddingPlanner.view.tasks;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityTaskDetailsBinding;
import pl.com.weddingPlanner.enums.BookmarksLocationEnum;
import pl.com.weddingPlanner.enums.CategoryTypeEnum;
import pl.com.weddingPlanner.enums.TaskStatusEnum;
import pl.com.weddingPlanner.model.Assignees;
import pl.com.weddingPlanner.model.Bookmarks;
import pl.com.weddingPlanner.persistence.entity.Bookmark;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.persistence.entity.SubTask;
import pl.com.weddingPlanner.persistence.entity.Task;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.NavigationActivity;
import pl.com.weddingPlanner.view.dialog.QuestionDialog;
import pl.com.weddingPlanner.view.util.ResourceUtil;
import pl.com.weddingPlanner.view.util.TasksUtil;

import static pl.com.weddingPlanner.view.NavigationActivity.FRAGMENT_TO_LOAD_ID;
import static pl.com.weddingPlanner.view.util.ComponentsUtil.getIcon;
import static pl.com.weddingPlanner.view.util.ExtraUtil.ACTIVITY_TITLE_EXTRA;
import static pl.com.weddingPlanner.view.util.ExtraUtil.TASK_ID_EXTRA;

public class TaskDetailsActivity extends BaseActivity {

    private ActivityTaskDetailsBinding binding;

    private Task taskDetails;
    private Category categoryDetails;

    private List<SubTask> subTasksList = new ArrayList<>();
    private List<Bookmark> bookmarksList = new ArrayList<>();
    private List<Person> assigneesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_details);

        setActivityToolbarContentWithBackIcon(R.string.header_title_task_details);

        getAndSetData();
        setComponents();
        setListeners();
    }

    private void getAndSetData() {
        int taskId = getIntent().getExtras().getInt(TASK_ID_EXTRA, 0);

        taskDetails = DAOUtil.getTaskById(this, taskId);
        categoryDetails = DAOUtil.getCategoryByNameAndType(this, taskDetails.getCategory(), CategoryTypeEnum.TASKS.name());

        bookmarksList = TasksUtil.getBookmarks(taskDetails, this);
        assigneesList = TasksUtil.getAssignees(taskDetails, this);
        subTasksList = TasksUtil.getSubTasks(taskDetails, this);
    }

    private void setComponents() {
        binding.title.setText(taskDetails.getTitle());

        setBookmarks();
        setAssignees();
        setDescription();
        setCategory();
        setStatus();
        setSubTasks();
        setProgressBar();
        setDateTime();
        setMarkAsOption();
    }

    private void setBookmarks() {
        if (StringUtils.isNotBlank(taskDetails.getBookmarks())) {
            Bookmarks bookmarks = new Bookmarks(this, bookmarksList, BookmarksLocationEnum.DETAILS);
            binding.bookmarksLayout.addView(bookmarks.getBookmarksContainer());
        } else {
            binding.noBookmarks.setVisibility(View.VISIBLE);
        }
    }

    private void setAssignees() {
        if (StringUtils.isNotBlank(taskDetails.getAssignees())) {
            Assignees assignees = new Assignees(this, assigneesList);
            binding.assigneesLayout.addView(assignees.getAssigneesContainer());
        } else {
            binding.noAssignees.setVisibility(View.VISIBLE);
        }
    }

    private void setDescription() {
        if (StringUtils.isNotBlank(taskDetails.getDescription())) {
            binding.description.setText(taskDetails.getDescription());
        } else {
            binding.description.setText(getResources().getString(R.string.no_description));
        }
    }

    private void setProgressBar() {
        binding.progressBar.setMax(subTasksList.size());

        setProgressAndText(getCheckedSubTasks());
    }

    private void setDateTime() {
        String dateTime = StringUtils.isNotBlank(taskDetails.getTime())
                ? taskDetails.getDate() + ", " + taskDetails.getTime()
                : taskDetails.getDate();
        binding.dateText.setText(dateTime);
    }

    private void setMarkAsOption() {
        if (TaskStatusEnum.DONE.name().equals(taskDetails.getStatus())) {
            binding.markAsText.setText(getString(R.string.task_details_mark_in_progress));
        } else {
            binding.markAsText.setText(getString(R.string.task_details_mark_done));
        }
    }

    private void setSubTasks() {
        if (!subTasksList.isEmpty()) {
            for (SubTask subTask : subTasksList) {
                binding.subTasksLayout.addView(createSubTask(subTask));
            }
        } else {
            binding.noSubTasks.setVisibility(View.VISIBLE);
        }
    }

    private CheckBox createSubTask(SubTask subTask) {
        CheckBox checkBox = new CheckBox(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        int marginStart = Math.round(getResources().getDimension(R.dimen.checkbox_margin_start));
        int marginBottom = Math.round(getResources().getDimension(R.dimen.checkbox_margin_bottom));
        layoutParams.setMargins(marginStart, 0, 0, marginBottom);

        checkBox.setLayoutParams(layoutParams);
        checkBox.setId(subTask.getId());
        checkBox.setText(subTask.getName());
        checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        checkBox.setTextColor(ContextCompat.getColor(this, R.color.gray_949494));
        checkBox.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimaryDark)));
        checkBox.setChecked(subTask.getDone() != null && (subTask.getDone().equals("true")));

        return checkBox;
    }

    private int getCheckedSubTasks() {
        int checkedSubTasks = 0;
        int childCount = binding.subTasksLayout.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View childView = binding.subTasksLayout.getChildAt(i);
            if (childView instanceof CheckBox) {
                CheckBox subTaskCheckbox = (CheckBox) childView;
                if (subTaskCheckbox.isChecked())
                    checkedSubTasks++;
            }
        }

        return checkedSubTasks;
    }

    private void setProgressAndText(int checkedSubTasks) {
        binding.progressBar.setProgress(checkedSubTasks, true);

        String progress = checkedSubTasks + "/" + subTasksList.size();
        binding.txtProgress.setText(progress);
    }

    private void setCategory() {
        binding.categoryIcon.setImageDrawable(getIcon(this, ResourceUtil.getResId(categoryDetails.getIconId(), R.drawable.class)));
        binding.categoryName.setText(categoryDetails.getName());
    }

    private void setStatus() {
        binding.status.setText(getString(TaskStatusEnum.valueOf(taskDetails.getStatus()).getNameResId()));
    }

    private void setListeners() {
        setTaskFloatingButtonListener();
        setCheckBoxesListener();
        setMarkAsListener();
        setDeleteTaskListener();
        setEditTaskListener();
    }

    private void setTaskFloatingButtonListener() {
        binding.tasksFloatingButton.setOnClickListener(v -> {
            if (binding.markAsLayout.getVisibility() == View.GONE
                    &&binding.deleteLayout.getVisibility() == View.GONE
                    && binding.addSubTasksLayout.getVisibility() == View.GONE
                    && binding.editLayout.getVisibility() == View.GONE) {
                showFloatingMenu();
            } else {
                hideFloatingMenu();
            }
        });
    }

    private void setCheckBoxesListener() {
        int childCount = binding.subTasksLayout.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View childView = binding.subTasksLayout.getChildAt(i);
            if (childView instanceof CheckBox) {
                CheckBox subTaskCheckbox = (CheckBox) childView;
                subTaskCheckbox.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (compoundButton.isChecked()) {
                        setProgressAndText(binding.progressBar.getProgress() + 1);
                        DAOUtil.setSubTaskDone(this, "true", compoundButton.getId());
                    } else {
                        setProgressAndText(binding.progressBar.getProgress() -1);
                        DAOUtil.setSubTaskDone(this, "false", compoundButton.getId());
                    }
                });
            }
        }
    }

    private void setMarkAsListener() {
        binding.markAsLayout.setOnClickListener(v -> {
            setTaskNewStatus();
            DAOUtil.mergeTask(getApplicationContext(), taskDetails);
            setStatus();
            hideFloatingMenu();
            setMarkAsOption();
        });
    }

    private void setTaskNewStatus() {
        switch (TaskStatusEnum.valueOf(taskDetails.getStatus())) {
            case NEW:
            case IN_PROGRESS:
                taskDetails.setStatus(TaskStatusEnum.DONE.name());
                break;
            case DONE:
                taskDetails.setStatus(TaskStatusEnum.IN_PROGRESS.name());
        }
    }

    private void setDeleteTaskListener() {
        binding.deleteLayout.setOnClickListener(v -> {
            new QuestionDialog(TaskDetailsActivity.this, getResources().getString(R.string.task_details_delete_question)).showDialog();
            hideFloatingMenu();
        });
    }

    private void setEditTaskListener() {
        binding.editLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), NewTaskActivity.class);
            intent.putExtra(TASK_ID_EXTRA, taskDetails.getId());
            intent.putExtra(ACTIVITY_TITLE_EXTRA, R.string.header_title_task_edit);
            startActivity(intent);

            hideFloatingMenu();
        });
    }

    private void showFloatingMenu() {
        binding.markAsLayout.setVisibility(View.VISIBLE);
        binding.deleteLayout.setVisibility(View.VISIBLE);
        binding.addSubTasksLayout.setVisibility(View.VISIBLE);
        binding.editLayout.setVisibility(View.VISIBLE);
        binding.backgroundFade.setVisibility(View.VISIBLE);
    }

    private void hideFloatingMenu() {
        binding.markAsLayout.setVisibility(View.GONE);
        binding.deleteLayout.setVisibility(View.GONE);
        binding.addSubTasksLayout.setVisibility(View.GONE);
        binding.editLayout.setVisibility(View.GONE);
        binding.backgroundFade.setVisibility(View.GONE);
    }

    @Override
    public void executeQuestionDialog() {
        DAOUtil.deleteTaskById(this, taskDetails.getId());

        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra(FRAGMENT_TO_LOAD_ID, R.id.navigation_tasks);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
