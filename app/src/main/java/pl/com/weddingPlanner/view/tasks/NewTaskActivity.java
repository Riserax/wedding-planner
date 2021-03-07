package pl.com.weddingPlanner.view.tasks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityNewTaskBinding;
import pl.com.weddingPlanner.enums.CategoryTypeEnum;
import pl.com.weddingPlanner.model.PickedDate;
import pl.com.weddingPlanner.model.PickedTime;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Task;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.util.DebouncedOnClickListener;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.NavigationActivity;
import pl.com.weddingPlanner.view.dialog.DateDialog;
import pl.com.weddingPlanner.view.dialog.PeopleDialog;
import pl.com.weddingPlanner.view.dialog.SingleSelectionListDialog;
import pl.com.weddingPlanner.view.tasks.dialog.TaskBookmarksDialog;
import pl.com.weddingPlanner.view.tasks.dialog.TaskTimeDialog;
import pl.com.weddingPlanner.view.util.ComponentsUtil;
import pl.com.weddingPlanner.view.util.TasksUtil;

import static pl.com.weddingPlanner.view.NavigationActivity.FRAGMENT_TO_LOAD_ID;
import static pl.com.weddingPlanner.view.util.ComponentsUtil.setButtonEnability;
import static pl.com.weddingPlanner.view.util.ExtraUtil.ACTIVITY_TITLE_EXTRA;
import static pl.com.weddingPlanner.view.util.ExtraUtil.TASK_ID_EXTRA;
import static pl.com.weddingPlanner.view.util.LambdaUtil.getOnTextChangedTextWatcher;
import static pl.com.weddingPlanner.view.util.ResourceUtil.CATEGORY_OTHER;

public class NewTaskActivity extends BaseActivity {

    private ActivityNewTaskBinding binding;

    private int taskId;
    private int headerTitle;

    private Task taskDetails;
    private Category categoryDetails;

    @Setter
    private List<Integer> selectedBookmarksKeys = new ArrayList<>();
    private List<Integer> selectedPeopleKeys = new ArrayList<>();
    private PickedDate pickedDate;
    @Setter
    private PickedTime pickedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_task);

        getExtrasAndSetVariables();
        setActivityToolbarContentWithBackIcon(headerTitle);

        prepareForEditing();
        setListeners();
        setButtonEnability(binding.addSaveButton, false);
    }

    private void getExtrasAndSetVariables() {
        taskId = getIntent().getIntExtra(TASK_ID_EXTRA, 0);
        headerTitle = getIntent().getIntExtra(ACTIVITY_TITLE_EXTRA, R.string.header_title_task_new);
    }

    private void prepareForEditing() {
        getDataAndSetVariables();
        fillFields();
        setPositionsSelectedInDialogs();
    }

    private void getDataAndSetVariables() {
        if (taskId > 0) {
            taskDetails = DAOUtil.getTaskById(this, taskId);
            categoryDetails = DAOUtil.getCategoryByNameAndType(this, taskDetails.getCategory(), CategoryTypeEnum.TASKS.name());
        }
    }

    private void fillFields() {
        if (taskDetails != null) {
            binding.taskName.setText(taskDetails.getTitle());

            fillCategory();
            fillBookmarks();
            fillAssignees();
            fillDescription();

            binding.taskDate.setText(taskDetails.getDate());

            fillTime();

            binding.addSaveButton.setText(getResources().getString(R.string.button_save));
        }
    }

    private void fillCategory() {
        binding.categoryTitle.setVisibility(View.VISIBLE);
        binding.categoryName.setText(categoryDetails.getName());
    }

    private void fillBookmarks() {
        if (StringUtils.isNotBlank(taskDetails.getBookmarks())) {
            binding.bookmarksTitle.setVisibility(View.VISIBLE);

            String bookmarksNamesSeparated = TasksUtil.getBookmarksNamesSeparated(taskDetails.getBookmarks(), this);
            binding.bookmarksName.setText(bookmarksNamesSeparated);
        }
    }

    private void fillAssignees() {
        if (StringUtils.isNotBlank(taskDetails.getAssignees())) {
            binding.peopleTitle.setVisibility(View.VISIBLE);

            String assigneeNamesSeparated = TasksUtil.getAssigneeNamesSeparated(taskDetails.getAssignees(), this);
            binding.peopleName.setText(assigneeNamesSeparated);
        }
    }

    private void fillDescription() {
        if (StringUtils.isNotBlank(taskDetails.getDescription())) {
            binding.taskDescriptionName.setText(taskDetails.getDescription());
        }
    }

    private void fillTime() {
        if (StringUtils.isNotBlank(taskDetails.getTime())) {
            binding.taskTime.setText(taskDetails.getTime());
        }
    }

    private void setPositionsSelectedInDialogs() {
        if (taskDetails != null) {
            if (StringUtils.isNotBlank(taskDetails.getBookmarks())) {
                selectedBookmarksKeys = TasksUtil.getListOfBookmarksIds(taskDetails.getBookmarks(), this);
            }

            if (StringUtils.isNotBlank(taskDetails.getAssignees())) {
                selectedPeopleKeys = TasksUtil.getListOfAssigneesIds(taskDetails.getAssignees(), this);
            }

            pickedDate = TasksUtil.getPickedDateFromString(taskDetails.getDate());

            if (StringUtils.isNotBlank(taskDetails.getTime())) {
                pickedTime = TasksUtil.getPickedTimeFromString(taskDetails.getTime());
            }
        }
    }

    private void setListeners() {
        initAddButtonEnableStatusListener();
        setCategoryOnClickListener();
        setBookmarksOnClickListener();
        setPeopleOnClickListener();
        setDateOnCLickListener();
        setTimeOnClickListener();
        initRootScrollViewListener();
        setOnFocusChangeListener();
        setAddButtonClickListener();
    }

    private void initAddButtonEnableStatusListener() {
        TextWatcher listener = getOnTextChangedTextWatcher((s, start, before, count) ->
                setButtonEnability(binding.addSaveButton, areFieldsValid())
        );

        binding.taskName.addTextChangedListener(listener);
        binding.categoryName.addTextChangedListener(listener);
        binding.bookmarksName.addTextChangedListener(listener);
        binding.peopleName.addTextChangedListener(listener);
        binding.taskDescriptionName.addTextChangedListener(listener);
        binding.taskDate.addTextChangedListener(listener);
        binding.taskTime.addTextChangedListener(listener);
    }

    private boolean areFieldsValid() {
        return !binding.taskName.getText().toString().isEmpty();
    }

    private void setCategoryOnClickListener() {
        binding.categoryLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                List<Category> categories = DAOUtil.getAllCategoriesByType(NewTaskActivity.this, CategoryTypeEnum.TASKS.name());
                new SingleSelectionListDialog(NewTaskActivity.this, categories, R.string.dialog_category_choice).showDialog();
            }
        });
    }

    private void setBookmarksOnClickListener() {
        binding.bookmarksLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                new TaskBookmarksDialog(NewTaskActivity.this, selectedBookmarksKeys).showDialog();
            }
        });
    }

    private void setPeopleOnClickListener() {
        binding.peopleLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                new PeopleDialog(NewTaskActivity.this, selectedPeopleKeys).showDialog();
            }
        });
    }

    private void setDateOnCLickListener() {
        binding.taskDateLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                new DateDialog(NewTaskActivity.this, pickedDate).showDialog();
            }
        });
    }

    private void setTimeOnClickListener() {
        binding.taskTimeLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                new TaskTimeDialog(NewTaskActivity.this, pickedTime).showDialog();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initRootScrollViewListener() {
        binding.taskRootScrollView.setOnTouchListener((v, event) -> {
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

    private void setAddButtonClickListener() {
        binding.addSaveButton.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();

                Task newTask = getNewTaskData();

                if (!newTask.getTitle().isEmpty() && !newTask.getDate().isEmpty()) {
                    DAOUtil.insertTask(getApplicationContext(), newTask);

                    Intent intent = new Intent(NewTaskActivity.this, NavigationActivity.class);
                    intent.putExtra(FRAGMENT_TO_LOAD_ID, R.id.navigation_tasks);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast toast;
                    if (newTask.getTitle().isEmpty()) {
                        toast = Toast.makeText(NewTaskActivity.this, "Nazwa nie może być pusta", Toast.LENGTH_LONG);
                        toast.show();
                    } else if (newTask.getDate().isEmpty()) {
                        toast = Toast.makeText(NewTaskActivity.this, "Data nie może być pusta", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });
    }

    private Task getNewTaskData() {
        String category = binding.categoryName.getText().toString();
        String bookmarks = binding.bookmarksName.getText().toString();
        String assignees = binding.peopleName.getText().toString();
        String time = binding.taskTime.getText().toString();

        boolean isCategoryChosen = !category.equals(getResources().getString(R.string.field_category));
        boolean areBookmarksSet = !bookmarks.equals(getResources().getString(R.string.task_field_bookmarks));
        boolean areAssigneesSet = !assignees.equals(getResources().getString(R.string.task_field_people));
        boolean isTimeSet = !time.equals(getResources().getString(R.string.task_field_time));

        String bookmarksIdsString = areBookmarksSet ? TasksUtil.getBookmarksIds(bookmarks, this) : StringUtils.EMPTY;
        String assigneesIdsString = areAssigneesSet ? TasksUtil.getAssigneesIds(assignees, this) : StringUtils.EMPTY;

        return Task.builder()
                .category(isCategoryChosen ? category : CATEGORY_OTHER)
                .title(binding.taskName.getText().toString())
                .description(binding.taskDescriptionName.getText().toString())
                .bookmarks(bookmarksIdsString)
                .assignees(assigneesIdsString)
                .date(binding.taskDate.getText().toString())
                .time(isTimeSet ? time : StringUtils.EMPTY)
                .build();
    }

    @Override
    public void setDefaultFieldName(TextView view) {
        switch (view.getId()) {
            case R.id.bookmarks_name:
                view.setText(getResources().getString(R.string.task_field_bookmarks));
                break;
            case R.id.people_name:
                view.setText(getResources().getString(R.string.task_field_people));
                break;
            case R.id.task_date:
                view.setText(getResources().getString(R.string.task_field_date));
                break;
        }
    }

    @Override
    public void setSelectedPeopleKeys(List<Integer> selectedPeopleKeys) {
        this.selectedPeopleKeys = selectedPeopleKeys;
    }

    @Override
    public void setPickedDate(PickedDate pickedDate) {
        this.pickedDate = pickedDate;
    }
}
