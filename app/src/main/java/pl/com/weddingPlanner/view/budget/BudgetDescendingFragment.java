package pl.com.weddingPlanner.view.budget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.apache.commons.lang3.StringUtils;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.FragmentBudgetDescendingBinding;
import pl.com.weddingPlanner.model.ExpenseInfo;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Expense;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.enums.CategoryTypeEnum;
import pl.com.weddingPlanner.view.list.ContentItem;
import pl.com.weddingPlanner.view.list.HeaderItem;
import pl.com.weddingPlanner.view.list.ListItem;
import pl.com.weddingPlanner.view.list.ListRecyclerAdapter;
import pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView;
import pl.com.weddingPlanner.view.util.BudgetUtil;
import pl.com.weddingPlanner.view.util.FormatUtil;

import static pl.com.weddingPlanner.view.list.HeaderItem.getHeaderItemWithDayOfWeek;
import static pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView.PAGE_START;

public class BudgetDescendingFragment extends Fragment {

    private final double TOTAL_AMOUNT = 40000.00;

    private FragmentBudgetDescendingBinding binding;

    private double amountSum = 0.00;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private LinearLayoutManager linearLayoutManager;
    private ListRecyclerAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_budget_descending, container, false);

        setComponents();

        getBudgetList();

        return binding.getRoot();
    }

    private void setComponents() {
        setProgressBar();
        setAmounts();
        setRecyclerView();
        setSwipeRefresh();
    }

    private void setProgressBar() {
        List<Expense> allExpenses = DAOUtil.getAllExpenses(getContext());
        amountSum = 0.00;

        for (Expense expense : allExpenses) {
            if (StringUtils.isNotBlank(expense.getAmount())) {
                amountSum += Double.parseDouble(expense.getAmount());
            }
        }

        setProgressAndText(amountSum);
    }

    private void setProgressAndText(double amountSum) {
        int percentage = (int) (amountSum / TOTAL_AMOUNT * 100);
        String progress = percentage + "%";

        binding.progressBar.setProgress(percentage, true);
        binding.txtProgress.setText(progress);
    }

    private void setAmounts() {
        String spentAmount = FormatUtil.convertToAmount(amountSum);
        String totalAmount = FormatUtil.convertToAmount(TOTAL_AMOUNT);

        binding.amountSpent.setText(spentAmount);
        binding.totalAmount.setText(totalAmount);
    }

    private void setRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(requireContext());
        adapter = new ListRecyclerAdapter(requireContext(), new LinkedList<>(), item -> {
            //TODO
//            Intent intent = new Intent(requireContext(), BudgetDetailsActivity.class);
//            intent.putExtra(TASK_ID_EXTRA, item.getItemId());
//            startActivity(intent);
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
            getBudgetList();
            setProgressBar();
            setAmounts();
        });
    }

    private void getBudgetList() {
        List<ExpenseInfo> toReturn = new ArrayList<>();
        List<Expense> allExpenses = DAOUtil.getAllExpenses(requireContext());

        if (!allExpenses.isEmpty()) {
            Map<Integer, LocalDate> sortedIdDateMap = BudgetUtil.getSortedIdDateMap(allExpenses);
            Map<Integer, Expense> expensesMap = BudgetUtil.getExpensesMap(allExpenses);

            for (Map.Entry<Integer, LocalDate> sortedIdDate : sortedIdDateMap.entrySet()) {
                Expense expense = (Expense) expensesMap.get(sortedIdDate.getKey());

                Category category = DAOUtil.getCategoryByNameAndType(requireContext(), expense.getCategory(), CategoryTypeEnum.BUDGET.name());

                ExpenseInfo expenseInfo = ExpenseInfo.builder()
                        .itemId(expense.getId())
                        .title(expense.getTitle())
                        .categoryIconId(category.getIconId())
                        .amount(expense.getAmount())
                        .payer(expense.getPayer())
                        .date(expense.getDate())
                        .build();

                toReturn.add(expenseInfo);
            }
        }

        List<ListItem> listItems = prepareAccountsInfoList(toReturn, adapter.getItems());
        adapter.addItems(listItems);
    }

    private List<ListItem> prepareAccountsInfoList(List<ExpenseInfo> expenseInfoList, List<ListItem> list) {
        List<ListItem> toReturn = new ArrayList<>();

        for (ExpenseInfo expenseInfo : expenseInfoList) {
            HeaderItem headerItem = getHeaderItemWithDayOfWeek(requireContext(), expenseInfo.getDate());

            if (!toReturn.contains(headerItem) && !list.contains(headerItem)) {
                toReturn.add(headerItem);
            }

            toReturn.add(ContentItem.of(expenseInfo));
        }

        return toReturn;
    }
}