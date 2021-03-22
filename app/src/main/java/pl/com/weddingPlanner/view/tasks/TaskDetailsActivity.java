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
import pl.com.weddingPlanner.enums.CategoryTypeEnum;
import pl.com.weddingPlanner.enums.LocationEnum;
import pl.com.weddingPlanner.enums.TaskStatusEnum;
import pl.com.weddingPlanner.persistence.entity.Bookmark;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.persistence.entity.SubTask;
import pl.com.weddingPlanner.persistence.entity.Task;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.NavigationActivity;
import pl.com.weddingPlanner.view.component.Assignees;
import pl.com.weddingPlanner.view.component.Bookmarks;
import pl.com.weddingPlanner.view.dialog.QuestionDialog;
import pl.com.weddingPlanner.view.tasks.dialog.NewSubTaskDialog;
import pl.com.weddingPlanner.view.util.ResourceUtil;
import pl.com.weddingPlanner.view.util.TasksUtil;

import static pl.com.weddingPlanner.view.NavigationActivity.FRAGMENT_TO_LOAD_ID;
import static pl.com.weddingPlanner.view.util.ComponentsUtil.getIcon;
import static pl.com.weddingPlanner.view.util.ExtraUtil.ACTIVITY_TITLE_EXTRA;
import static pl.com.weddingPlanner.view.util.ExtraUtil.TASK_ID_EXTRA;

public class TaskDetailsActivity extends BaseActivity {

    private ActivityTaskDetailsBinding binding;

    private int taskId;

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

        taskId = getIntent().getExtras().getInt(TASK_ID_EXTRA, 0);

        setDetails();
        setListeners();
    }

    public void setDetails() {
        getAndSetData();
        setComponents();
    }

    private void getAndSetData() {
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
            Bookmarks bookmarks = new Bookmarks(this, bookmarksList, LocationEnum.DETAILS);

            if (binding.bookmarksLayout.getChildCount() > 1) {
                binding.bookmarksLayout.removeViewAt(1);
            }

            binding.bookmarksLayout.addView(bookmarks.getBookmarksContainer());
        } else {
            binding.noBookmarks.setVisibility(View.VISIBLE);
        }
    }

    private void setAssignees() {
        if (StringUtils.isNotBlank(taskDetails.getAssignees())) {
            Assignees assignees = new Assignees(this, assigneesList, LocationEnum.DETAILS);

            if (binding.assigneesLayout.getChildCount() > 1) {
                binding.assigneesLayout.removeViewAt(1);
            }

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
            binding.markAsButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_progress));
        } else {
            binding.markAsText.setText(getString(R.string.task_details_mark_done));
            binding.markAsButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_done));
        }
    }

    private void setSubTasks() {
        if (!subTasksList.isEmpty()) {
            if (binding.subTasksLayout.getChildCount() > 2) {
                binding.subTasksLayout.removeViews(2, subTasksList.size() - 1);
            }

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
        int iconResId = TaskStatusEnum.valueOf(taskDetails.getStatus()).getDrawableResId();
        binding.statusIcon.setImageDrawable(ContextCompat.getDrawable(this, iconResId));
        binding.status.setText(getString(TaskStatusEnum.valueOf(taskDetails.getStatus()).getTextResId()));
    }

    private void setListeners() {
        setAddSubTaskClickListener();
        setTaskFloatingButtonListener();
        setCheckBoxesListener();
        setMarkAsListener();
        setDeleteTaskListener();
        setEditTaskListener();
    }

    private void setAddSubTaskClickListener() {
        binding.addIcon.setOnClickListener(v -> {
            new NewSubTaskDialog(TaskDetailsActivity.this, taskDetails.getId()).showDialog();
        });
    }

    private void setTaskFloatingButtonListener() {
        binding.floatingButton.setOnClickListener(v -> {
            if (binding.markAsLayout.getVisibility() == View.GONE
                    && binding.deleteLayout.getVisibility() == View.GONE
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
                        markAsAfterCheck();
                    } else {
                        setProgressAndText(binding.progressBar.getProgress() - 1);
                        DAOUtil.setSubTaskDone(this, "false", compoundButton.getId());
                        markAsAfterUncheck();
                    }
                });
            }
        }
    }

    private void markAsAfterCheck() {
        int progress = binding.progressBar.getProgress();
        int max = binding.progressBar.getMax();

        if (progress == max) {
            taskDetails.setStatus(TaskStatusEnum.DONE.name());
        } else {
            taskDetails.setStatus(TaskStatusEnum.IN_PROGRESS.name());
        }

        DAOUtil.mergeTask(getApplicationContext(), taskDetails);
        setStatus();
        setMarkAsOption();
    }

    private void markAsAfterUncheck() {
        taskDetails.setStatus(TaskStatusEnum.IN_PROGRESS.name());
        DAOUtil.mergeTask(getApplicationContext(), taskDetails);
        setStatus();
        setMarkAsOption();
    }

    private void setMarkAsListener() {
        binding.markAsLayout.setOnClickListener(v -> {
            setTaskStatus();
            DAOUtil.mergeTask(getApplicationContext(), taskDetails);
            setStatus();
            hideFloatingMenu();
            setMarkAsOption();
        });
    }

    private void setTaskStatus() {
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
        binding.editLayout.setVisibility(View.VISIBLE);
//        binding.backgroundFade.setVisibility(View.VISIBLE);
    }

    private void hideFloatingMenu() {
        binding.markAsLayout.setVisibility(View.GONE);
        binding.deleteLayout.setVisibility(View.GONE);
        binding.editLayout.setVisibility(View.GONE);
//        binding.backgroundFade.setVisibility(View.GONE);
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
