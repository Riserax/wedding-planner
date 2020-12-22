package pl.com.weddingPlanner.view.category;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityCategoryBinding;
import pl.com.weddingPlanner.model.TaskInfo;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.budget.NewBudgetActivity;
import pl.com.weddingPlanner.view.enums.CategoryResource;
import pl.com.weddingPlanner.view.list.ContentItem;
import pl.com.weddingPlanner.view.list.HeaderItem;
import pl.com.weddingPlanner.view.list.ListItem;
import pl.com.weddingPlanner.view.list.ListRecyclerAdapter;
import pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView;
import pl.com.weddingPlanner.view.tasks.NewTaskActivity;
import pl.com.weddingPlanner.view.tasks.TaskDetailsActivity;

import static pl.com.weddingPlanner.view.list.HeaderItem.getHeaderItemWithDayOfWeek;
import static pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView.PAGE_START;
import static pl.com.weddingPlanner.view.util.SideBySideListUtil.CATEGORY_NAME_EXTRA;
import static pl.com.weddingPlanner.view.util.SideBySideListUtil.FRAGMENT_SOURCE_EXTRA;

public class CategoryActivity extends BaseActivity {

    private final String TASKS_CATEGORIES_FRAGMENT = "class pl.com.weddingPlanner.view.tasks.TasksCategoriesFragment";
    private final String BUDGET_CATEGORIES_FRAGMENT = "class pl.com.weddingPlanner.view.budget.BudgetCategoriesFragment";

    private ActivityCategoryBinding binding;

    private int categoryNameId = R.string.header_title_category;
    private String fragmentClass = "";

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private LinearLayoutManager linearLayoutManager;
    private ListRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category);

        getExtrasAndSetVariables();
        setActivityToolbarContentWithBackIcon(categoryNameId);

        setRecyclerView();
        setSwipeRefresh();

        setTasksList();
        setListeners();
    }

    private void setRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new ListRecyclerAdapter(this, new LinkedList<>(), item -> {
            Intent intent = new Intent(this, TaskDetailsActivity.class);
//            intent.putExtra(ACCOUNT_NUMBER, item.getDetailsId()); //TODO put id extra
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
            setTasksList();
        });
    }

    private void getExtrasAndSetVariables() {
        categoryNameId = getIntent().getExtras().getInt(CATEGORY_NAME_EXTRA, categoryNameId);
        fragmentClass = getIntent().getExtras().getString(FRAGMENT_SOURCE_EXTRA, fragmentClass);
    }

    private void setTasksList() {
        List<TaskInfo> tasks = new ArrayList<>();

        String title;
        if (TASKS_CATEGORIES_FRAGMENT.equals(fragmentClass)) {
            title = "Zadanie ";
        } else {
            title = "Wydatek ";
        }

        for (int i = 0; i < 5; i++) {
            TaskInfo task = TaskInfo.builder()
                    .itemId(i)
                    .category(CategoryResource.SUBCONTRACTORS)
                    .title(title + i)
                    .date("2020-12-22")
                    .build();
            tasks.add(task);
        }

        for (int i = 5; i < 10; i++) {
            TaskInfo task = TaskInfo.builder()
                    .itemId(i)
                    .category(CategoryResource.FORMAL_DOCUMENTS)
                    .title(title + i)
                    .date("2020-12-23")
                    .build();
            tasks.add(task);
        }

        for (int i = 10; i < 15; i++) {
            TaskInfo task = TaskInfo.builder()
                    .itemId(i)
                    .category(CategoryResource.WEDDING_HALL)
                    .title(title + i)
                    .date("2020-12-24")
                    .build();
            tasks.add(task);
        }

        List<ListItem> listItems = prepareAccountsInfoList(tasks, adapter.getItems());
        adapter.addItems(listItems);
    }

    private List<ListItem> prepareAccountsInfoList(List<TaskInfo> taskInfoList, List<ListItem> list) {
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
        binding.categoryFloatingButton.setOnClickListener(view -> {
            Intent intent;
            if (TASKS_CATEGORIES_FRAGMENT.equals(fragmentClass)) {
                intent = new Intent(this, NewTaskActivity.class);
                startActivity(intent);
            } else if (BUDGET_CATEGORIES_FRAGMENT.equals(fragmentClass)) {
                intent = new Intent(this, NewBudgetActivity.class);
                startActivity(intent);
            }
        });
    }
}