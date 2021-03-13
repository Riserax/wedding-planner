package pl.com.weddingPlanner.view.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.FragmentTasksMonthBinding;
import pl.com.weddingPlanner.model.info.TaskInfo;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Task;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.util.DateUtil;
import pl.com.weddingPlanner.enums.CategoryTypeEnum;
import pl.com.weddingPlanner.view.list.ContentItem;
import pl.com.weddingPlanner.view.list.HeaderItem;
import pl.com.weddingPlanner.view.list.ListItem;
import pl.com.weddingPlanner.view.list.ListRecyclerAdapter;
import pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView;
import pl.com.weddingPlanner.view.util.TasksUtil;

import static pl.com.weddingPlanner.view.list.HeaderItem.getHeaderItemWithDayOfWeek;
import static pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView.PAGE_START;
import static pl.com.weddingPlanner.view.util.ExtraUtil.TASK_ID_EXTRA;

public class TasksMonthFragment extends Fragment {

    private FragmentTasksMonthBinding binding;

    private final String month;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private LinearLayoutManager linearLayoutManager;
    private ListRecyclerAdapter adapter;

    public TasksMonthFragment(String month) {
        this.month = month;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks_month, container, false);

        setRecyclerView();
        setSwipeRefresh();

        getTasksList();

        return binding.getRoot();
    }

    private void setRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(requireContext());
        adapter = new ListRecyclerAdapter(requireContext(), new LinkedList<>(), item -> {
            Intent intent = new Intent(requireContext(), TaskDetailsActivity.class);
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
            getTasksList();
        });
    }

    private void getTasksList() {
        List<TaskInfo> toReturn = new ArrayList<>();
        List<Task> allTasks = DAOUtil.getAllTasks(requireContext());

        List<Task> tasksByMonth = new ArrayList<>();
        for (Task task : allTasks) {
            if (month.equals(DateUtil.getDateMonth(task.getDate(), requireContext()))) {
                tasksByMonth.add(task);
            }
        }

        if (!tasksByMonth.isEmpty()) {
            Map<Integer, LocalDate> sortedIdDateMap = TasksUtil.getSortedIdDateMap(tasksByMonth);
            Map<Integer, Task> tasksMap = TasksUtil.getTasksMap(tasksByMonth);

            for (Map.Entry<Integer, LocalDate> sortedIdDate : sortedIdDateMap.entrySet()) {
                Task task = (Task) tasksMap.get(sortedIdDate.getKey());

                Category category = DAOUtil.getCategoryByNameAndType(requireContext(), task.getCategory(), CategoryTypeEnum.TASKS.name());

                TaskInfo taskInfo = TaskInfo.builder()
                        .itemId(task.getId())
                        .title(task.getTitle())
                        .categoryIconId(category.getIconId())
                        .date(task.getDate())
                        .build();

                toReturn.add(taskInfo);
            }
        }

        List<ListItem> listItems = prepareTasksInfoList(toReturn, adapter.getItems());
        adapter.addItems(listItems);
    }

    private List<ListItem> prepareTasksInfoList(List<TaskInfo> taskInfoList, List<ListItem> list) {
        List<ListItem> toReturn = new ArrayList<>();

        for (TaskInfo taskInfo : taskInfoList) {
            HeaderItem headerItem = getHeaderItemWithDayOfWeek(requireContext(), taskInfo.getDate());

            if (!toReturn.contains(headerItem) && !list.contains(headerItem)) {
                toReturn.add(headerItem);
            }

            toReturn.add(ContentItem.of(taskInfo));
        }

        return toReturn;
    }
}