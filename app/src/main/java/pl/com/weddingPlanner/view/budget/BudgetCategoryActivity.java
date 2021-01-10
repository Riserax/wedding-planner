package pl.com.weddingPlanner.view.budget;

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
import pl.com.weddingPlanner.databinding.ActivityCategoryBudgetBinding;
import pl.com.weddingPlanner.model.ExpenseInfo;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Expense;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.util.DateUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.enums.CategoryTypeEnum;
import pl.com.weddingPlanner.view.list.ContentItem;
import pl.com.weddingPlanner.view.list.HeaderItem;
import pl.com.weddingPlanner.view.list.ListItem;
import pl.com.weddingPlanner.view.list.ListRecyclerAdapter;
import pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView;
import pl.com.weddingPlanner.view.util.BudgetUtil;

import static pl.com.weddingPlanner.view.budget.ExpenseActivity.EXPENSE_ID_EXTRA;
import static pl.com.weddingPlanner.view.list.HeaderItem.getHeaderItemWithDayOfWeek;
import static pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView.PAGE_START;
import static pl.com.weddingPlanner.view.util.SideBySideListUtil.CATEGORY_NAME_EXTRA;

public class BudgetCategoryActivity extends BaseActivity {

    private ActivityCategoryBudgetBinding binding;

    private String categoryName;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private LinearLayoutManager linearLayoutManager;
    private ListRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category_budget);

        setActivityToolbarContentWithBackIcon(getCategoryNameAndSetVariable());

        setRecyclerView();
        setSwipeRefresh();

        getList();
        setListeners();
    }

    private String getCategoryNameAndSetVariable() {
        StringBuilder categoryNameSB = new StringBuilder();
        String categoryName = getIntent().getExtras().getString(CATEGORY_NAME_EXTRA, getResources().getString(R.string.header_title_category));

        categoryNameSB.append(getResources().getString(R.string.header_title_budget)).append(" - ");
        categoryNameSB.append(categoryName);

        this.categoryName = categoryName;

        return categoryNameSB.toString();
    }

    private void setRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new ListRecyclerAdapter(this, new LinkedList<>(), item -> {
            Intent intent = new Intent(this, ExpenseActivity.class);
            intent.putExtra(EXPENSE_ID_EXTRA, item.getItemId());
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
        List<ExpenseInfo> toReturn = new ArrayList<>();
        List<Expense> allExpenses = DAOUtil.getAllExpensesByCategory(this, categoryName);

        if (!allExpenses.isEmpty()) {
            Map<Integer, LocalDate> sortedIdDateMap = BudgetUtil.getSortedIdDateMap(allExpenses);
            Map<Integer, Object> expensesMap = BudgetUtil.getObjectsMap(allExpenses);

            for (Map.Entry<Integer, LocalDate> sortedIdDate : sortedIdDateMap.entrySet()) {
                Expense expense = (Expense) expensesMap.get(sortedIdDate.getKey());

                Category category = DAOUtil.getCategoryByNameAndType(this, expense.getCategory(), CategoryTypeEnum.BUDGET.name());

                ExpenseInfo expenseInfo = ExpenseInfo.builder()
                        .itemId(expense.getId())
                        .title(expense.getTitle())
                        .categoryIconId(category.getIconId())
                        .amount(expense.getInitialAmount())
                        .payer(expense.getPayers())
                        .date(expense.getEditDate())
                        .build();

                toReturn.add(expenseInfo);
            }
        }

        List<ListItem> listItems = prepareItemsInfoList(toReturn, adapter.getItems());
        adapter.addItems(listItems);
    }

    private List<ListItem> prepareItemsInfoList(List<ExpenseInfo> expenseInfoList, List<ListItem> list) {
        List<ListItem> toReturn = new ArrayList<>();

        for (ExpenseInfo expenseInfo : expenseInfoList) {
            String date = DateUtil.getDateFromDateTime(expenseInfo.getDate());
            HeaderItem headerItem = getHeaderItemWithDayOfWeek(this, date);

            if (!toReturn.contains(headerItem) && !list.contains(headerItem)) {
                toReturn.add(headerItem);
            }
            toReturn.add(ContentItem.of(expenseInfo));
        }

        return toReturn;
    }

    private void setListeners() {
        binding.categoryBudgetFloatingButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewExpenseActivity.class);
            startActivity(intent);
        });
    }
}
