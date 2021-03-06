package pl.com.weddingPlanner.view.budget;

import android.content.Intent;
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

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.FragmentBudgetDescendingBinding;
import pl.com.weddingPlanner.enums.CategoryType;
import pl.com.weddingPlanner.enums.Location;
import pl.com.weddingPlanner.view.component.Assignees;
import pl.com.weddingPlanner.model.info.ExpenseInfo;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Expense;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.util.DateUtil;
import pl.com.weddingPlanner.view.list.ContentItem;
import pl.com.weddingPlanner.view.list.HeaderItem;
import pl.com.weddingPlanner.view.list.ListItem;
import pl.com.weddingPlanner.view.list.ListRecyclerAdapter;
import pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView;
import pl.com.weddingPlanner.view.util.BudgetUtil;
import pl.com.weddingPlanner.view.util.FormatUtil;
import pl.com.weddingPlanner.view.util.PersonUtil;

import static pl.com.weddingPlanner.view.budget.ExpenseActivity.EXPENSE_ID_EXTRA;
import static pl.com.weddingPlanner.view.list.HeaderItem.getHeaderItemWithDayOfWeek;
import static pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView.PAGE_START;

public class BudgetDescendingFragment extends Fragment {

    private FragmentBudgetDescendingBinding binding;

    private double initialAmountsSum = 0.00;
    private double initialTotalAmount = 0.00;
    private double paidPaymentsSum = 0.00;

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
        getAndSetInitialAmounts();
        getAndSetPayments();
        setProgressAndText();
    }

    private void getAndSetInitialAmounts() {
        initialAmountsSum = BudgetUtil.getInitialAmountsSum(getContext());
        initialTotalAmount = Double.parseDouble(DAOUtil.getWeddingById(getContext(), 1).getInitialTotalAmount()); //TODO rozróżniać w którym weselu się znajdujemy
    }

    private void getAndSetPayments() {
        paidPaymentsSum = BudgetUtil.getPaidPaymentsSum(getContext());
    }

    private void setProgressAndText() {
        int percentage = (int) (initialAmountsSum / initialTotalAmount * 100);
        String progress = percentage + "%";

        binding.progressBar.setProgress(percentage, true);
        binding.txtProgress.setText(progress);
    }

    private void setAmounts() {
        String initialSpentAmount = FormatUtil.convertToAmount(initialAmountsSum);
        String spentAmount = FormatUtil.convertToAmount(paidPaymentsSum);
        String totalAmount = FormatUtil.convertToAmount(initialTotalAmount);

        binding.amountInitialSpent.setText(initialSpentAmount);
        binding.amountSpent.setText(spentAmount);
        binding.totalAmount.setText(totalAmount);
    }

    private void setRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(requireContext());
        adapter = new ListRecyclerAdapter(requireContext(), new LinkedList<>(), item -> {
            Intent intent = new Intent(requireContext(), ExpenseActivity.class);
            intent.putExtra(EXPENSE_ID_EXTRA, item.getItemId());
            startActivity(intent);
        });

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setNestedScrollingEnabled(false);
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
            Map<Integer, Object> expensesMap = BudgetUtil.getObjectsMap(allExpenses);

            for (Map.Entry<Integer, LocalDate> sortedIdDate : sortedIdDateMap.entrySet()) {
                Expense expense = (Expense) expensesMap.get(sortedIdDate.getKey());

                Category category = DAOUtil.getCategoryByNameAndType(requireContext(), expense.getCategory(), CategoryType.BUDGET.name());

                List<Person> payersList = PersonUtil.getPersonsList(getContext(), expense.getPayers());
                Assignees assignees = new Assignees(getContext(), payersList, Location.LIST_ITEM);

                ExpenseInfo expenseInfo = ExpenseInfo.builder()
                        .itemId(expense.getId())
                        .title(expense.getTitle())
                        .categoryIconId(category.getIconId())
                        .amount(expense.getInitialAmount())
                        .date(expense.getEditDate())
                        .payersLayout(assignees.getAssigneesContainer())
                        .build();

                toReturn.add(expenseInfo);
            }
        }

        List<ListItem> listItems = prepareBudgetInfoList(toReturn, adapter.getItems());
        adapter.addItems(listItems);
    }

    private List<ListItem> prepareBudgetInfoList(List<ExpenseInfo> expenseInfoList, List<ListItem> list) {
        List<ListItem> toReturn = new ArrayList<>();

        for (ExpenseInfo expenseInfo : expenseInfoList) {
            String date = DateUtil.getDateFromDateTime(expenseInfo.getDate());
            HeaderItem headerItem = getHeaderItemWithDayOfWeek(requireContext(), date);

            if (!toReturn.contains(headerItem) && !list.contains(headerItem)) {
                toReturn.add(headerItem);
            }

            toReturn.add(ContentItem.of(expenseInfo));
        }

        return toReturn;
    }
}