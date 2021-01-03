package pl.com.weddingPlanner.view.tasks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import pl.com.weddingPlanner.model.PickedDate;
import pl.com.weddingPlanner.model.PickedTime;
import pl.com.weddingPlanner.persistence.entity.Bookmark;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.persistence.entity.Task;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.util.DebouncedOnClickListener;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.NavigationActivity;
import pl.com.weddingPlanner.view.dialog.CategoriesDialog;
import pl.com.weddingPlanner.view.dialog.PeopleDialog;
import pl.com.weddingPlanner.view.enums.CategoryTypeEnum;
import pl.com.weddingPlanner.view.tasks.dialog.TaskBookmarksDialog;
import pl.com.weddingPlanner.view.tasks.dialog.TaskDateDialog;
import pl.com.weddingPlanner.view.tasks.dialog.TaskTimeDialog;
import pl.com.weddingPlanner.view.util.ComponentsUtil;

import static pl.com.weddingPlanner.view.NavigationActivity.FRAGMENT_TO_LOAD_ID;

public class NewTaskActivity extends BaseActivity {

    private ActivityNewTaskBinding binding;

    @Setter
    private List<Integer> selectedBookmarksKeys = new ArrayList<>();
    private List<Integer> selectedPeopleKeys = new ArrayList<>();
    @Setter
    private PickedDate pickedDate;
    @Setter
    private PickedTime pickedTime;

    private boolean isCategoryChosen;
    private boolean areBookmarksSet;
    private boolean isAssigneesSet;
    private boolean isTimeSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_task);
        setActivityToolbarContentWithBackIcon(R.string.header_title_tasks_new);

        setListeners();
    }

    private void setListeners() {
        setCategoryOnClickListener();
        setBookmarksOnClickListener();
        setPeopleOnClickListener();
        setDateOnCLickListener();
        setTimeOnClickListener();
        initRootScrollViewListener();
        setOnFocusChangeListener();
        setAddButtonClickListener();
    }

    private void setCategoryOnClickListener() {
        binding.categoryLayout.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
            @Override
            public void onDebouncedClick(View v) {
                clearFocusAndHideKeyboard();
                new CategoriesDialog(NewTaskActivity.this, CategoryTypeEnum.TASKS.name()).showDialog();
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
                new TaskDateDialog(NewTaskActivity.this, pickedDate).showDialog();
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
        binding.addButton.setOnClickListener(new DebouncedOnClickListener(getResources().getInteger(R.integer.debounce_long_block_time_ms)) {
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
                        toast = Toast.makeText(NewTaskActivity.this, "Nazwa nie może być pusta!", Toast.LENGTH_LONG);
                        toast.show();
                    } else if (newTask.getDate().isEmpty()) {
                        toast = Toast.makeText(NewTaskActivity.this, "Data nie może być pusta!", Toast.LENGTH_LONG);
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

        isCategoryChosen = !category.equals(getResources().getString(R.string.task_field_category));
        areBookmarksSet = !bookmarks.equals(getResources().getString(R.string.task_field_bookmarks));
        isAssigneesSet = !assignees.equals(getResources().getString(R.string.task_field_people));
        isTimeSet = !time.equals(getResources().getString(R.string.task_field_time));

        String bookmarksIdsString = getBookmarksIds(bookmarks);
        String assigneesIdsString = getAssigneesIds(assignees);

        return Task.builder()
                .category(isCategoryChosen ? category : "Różne")
                .title(binding.taskName.getText().toString())
                .description(binding.taskDescriptionName.getText().toString())
                .bookmarks(areBookmarksSet ? bookmarksIdsString : StringUtils.EMPTY)
                .assignees(isAssigneesSet ? assigneesIdsString : StringUtils.EMPTY)
                .date(binding.taskDate.getText().toString())
                .time(isTimeSet ? time : StringUtils.EMPTY)
                .build();
    }

    private String getBookmarksIds(String bookmarks) {
        StringBuilder bookmarksIdsSB = new StringBuilder();
        String[] bookmarksNames = bookmarks.split("\\|", -1);

        List<Bookmark> bookmarkList = new ArrayList<>();
        for (String bookmarkName : bookmarksNames) {
            bookmarkList.add(DAOUtil.getBookmarkByName(this, bookmarkName.trim()));
        }

        for (int i = 0; i < bookmarkList.size(); i++) {
            if (i != bookmarkList.size() - 1) {
                bookmarksIdsSB.append(bookmarkList.get(i).getId()).append(",");
            } else {
                bookmarksIdsSB.append(bookmarkList.get(i).getId());
            }
        }

        return bookmarksIdsSB.toString();
    }

    private String getAssigneesIds(String assignees) {
        StringBuilder assigneesIdsSB = new StringBuilder();
        String[] assigneesNames = assignees.split("\\|", -1);

        List<Person> assigneeList = new ArrayList<>();
        for (String assigneeName : assigneesNames) {
            assigneeList.add(DAOUtil.getPersonByName(this, assigneeName.trim()));
        }

        for (int i = 0; i < assigneeList.size(); i++) {
            if (i != assigneeList.size() - 1) {
                assigneesIdsSB.append(assigneeList.get(i).getId()).append(",");
            } else {
                assigneesIdsSB.append(assigneeList.get(i).getId());
            }
        }

        return assigneesIdsSB.toString();
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
        }
    }

    @Override
    public void setSelectedPeopleKeys(List<Integer> selectedPeopleKeys) {
        this.selectedPeopleKeys = selectedPeopleKeys;
    }
}
