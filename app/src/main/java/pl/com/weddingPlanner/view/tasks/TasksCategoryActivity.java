package pl.com.weddingPlanner.view.tasks;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityCategoryTasksBinding;
import pl.com.weddingPlanner.enums.LocationEnum;
import pl.com.weddingPlanner.enums.CategoryTypeEnum;
import pl.com.weddingPlanner.enums.TaskStatusEnum;
import pl.com.weddingPlanner.view.component.Assignees;
import pl.com.weddingPlanner.view.component.Bookmarks;
import pl.com.weddingPlanner.model.info.TaskInfo;
import pl.com.weddingPlanner.persistence.entity.Bookmark;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.persistence.entity.Task;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.list.ContentItem;
import pl.com.weddingPlanner.view.list.HeaderItem;
import pl.com.weddingPlanner.view.list.ListItem;
import pl.com.weddingPlanner.view.list.ListRecyclerAdapter;
import pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView;
import pl.com.weddingPlanner.view.util.PersonUtil;
import pl.com.weddingPlanner.view.util.TasksUtil;

import static pl.com.weddingPlanner.view.list.HeaderItem.getHeaderItemWithDayOfWeek;
import static pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView.PAGE_START;
import static pl.com.weddingPlanner.view.util.ExtraUtil.TASK_ID_EXTRA;
import static pl.com.weddingPlanner.view.util.SideBySideListUtil.CATEGORY_NAME_EXTRA;

public class TasksCategoryActivity extends BaseActivity {

    private ActivityCategoryTasksBinding binding;

    private String categoryName;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private LinearLayoutManager linearLayoutManager;
    private ListRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category_tasks);

        setActivityToolbarContentWithBackIcon(getCategoryNameAndSetVariable());

        setRecyclerView();
        setSwipeRefresh();

        getList();
        setListeners();
    }

    private String getCategoryNameAndSetVariable() {
        StringBuilder categoryNameSB = new StringBuilder();
        String categoryName = getIntent().getExtras().getString(CATEGORY_NAME_EXTRA, getResources().getString(R.string.header_title_category));

        categoryNameSB.append(getResources().getString(R.string.header_title_tasks)).append(" - ").append(categoryName);

        this.categoryName = categoryName;

        return categoryNameSB.toString();
    }

    private void setRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new ListRecyclerAdapter(this, new LinkedList<>(), item -> {
            Intent intent = new Intent(this, TaskDetailsActivity.class);
            intent.putExtra(TASK_ID_EXTRA, item.getItemId());
            startActivity(intent);
        });

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationListenerRecyclerView(linearLayoutManager) {
            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
//                getAccounts(currentPage);
            }
        });
    }

    private void setSwipeRefresh() {
        SwipeRefreshLayout swipeRefresh = binding.swipeRefresh;
        swipeRefresh.setOnRefreshListener(() -> {
            swipeRefresh.setRefreshing(false);
            adapter.clear();
            currentPage = PAGE_START;
            getList();
        });
    }

    private void getList() {
        List<TaskInfo> toReturn = new ArrayList<>();
        List<Task> allTasksByCategory = DAOUtil.getAllTasksByCategory(this, categoryName);

        if (!allTasksByCategory.isEmpty()) {
            Map<Integer, LocalDate> sortedIdDateMap = TasksUtil.getSortedIdDateMap(allTasksByCategory);
            Map<Integer, Task> tasksMap = TasksUtil.getTasksMap(allTasksByCategory);

            for (Map.Entry<Integer, LocalDate> sortedIdDate : sortedIdDateMap.entrySet()) {
                Task task = tasksMap.get(sortedIdDate.getKey());

                Category category = DAOUtil.getCategoryByNameAndType(this, task.getCategory(), CategoryTypeEnum.TASKS.name());

                List<Bookmark> bookmarkList = TasksUtil.getBookmarks(task, this);
                Bookmarks bookmarks = new Bookmarks(this, bookmarkList, LocationEnum.LIST_ITEM);

                List<Person> assigneeList = PersonUtil.getPersonsList(this, task.getAssignees());
                Assignees assignees = new Assignees(this, assigneeList, LocationEnum.LIST_ITEM);

                TaskInfo taskInfo = TaskInfo.builder()
                        .itemId(task.getId())
                        .title(task.getTitle())
                        .categoryIconId(category.getIconId())
                        .date(task.getDate())
                        .status(TaskStatusEnum.valueOf(task.getStatus()))
                        .bookmarksLayout(bookmarks.getBookmarksContainer())
                        .assigneesLayout(assignees.getAssigneesContainer())
                        .build();

                toReturn.add(taskInfo);
            }
        }

        List<ListItem> listItems = prepareItemsInfoList(toReturn, adapter.getItems());
        adapter.addItems(listItems);
    }

    private List<ListItem> prepareItemsInfoList(List<TaskInfo> taskInfoList, List<ListItem> list) {
        List<ListItem> toReturn = new ArrayList<>();

        for (TaskInfo taskInfo : taskInfoList) {
            HeaderItem headerItem = getHeaderItemWithDayOfWeek(this, taskInfo.getDate());

            if (!toReturn.contains(headerItem) && !list.contains(headerItem)) {
                toReturn.add(headerItem);
            }
            toReturn.add(ContentItem.of(taskInfo));
        }

        return toReturn;
    }

    private void setListeners() {
        binding.categoryTasksFloatingButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewTaskActivity.class);
            startActivity(intent);
        });
    }
}