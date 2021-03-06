package pl.com.weddingPlanner.view.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.apache.commons.lang3.StringUtils;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.FragmentExpensePaymentsBinding;
import pl.com.weddingPlanner.enums.Location;
import pl.com.weddingPlanner.enums.PaymentState;
import pl.com.weddingPlanner.view.component.Assignees;
import pl.com.weddingPlanner.model.info.PaymentInfo;
import pl.com.weddingPlanner.persistence.entity.Payment;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.list.ContentItem;
import pl.com.weddingPlanner.view.list.HeaderItem;
import pl.com.weddingPlanner.view.list.ListItem;
import pl.com.weddingPlanner.view.list.ListRecyclerAdapter;
import pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView;
import pl.com.weddingPlanner.view.util.BudgetUtil;

import static pl.com.weddingPlanner.view.budget.ExpenseActivity.EXPENSE_ID_EXTRA;
import static pl.com.weddingPlanner.view.list.HeaderItem.getHeaderItemWithDayOfWeek;
import static pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView.PAGE_START;
import static pl.com.weddingPlanner.view.util.ExtraUtil.ACTIVITY_TITLE_EXTRA;

public class ExpensePaymentsFragment extends Fragment {

    public static String PAYMENT_ID_EXTRA = "paymentIdExtra";

    private FragmentExpensePaymentsBinding binding;

    private int expenseId;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private LinearLayoutManager linearLayoutManager;
    private ListRecyclerAdapter adapter;

    public ExpensePaymentsFragment(int expenseId) {
        this.expenseId = expenseId;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_expense_payments, container, false);

        setComponents();
        getPaymentsList();
        setListeners();

        return binding.getRoot();
    }

    private void setComponents() {
        setRecyclerView();
        setSwipeRefresh();
    }

    private void setRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(requireContext());
        adapter = new ListRecyclerAdapter(requireContext(), new LinkedList<>(), item -> {
            Intent intent = new Intent(getContext(), NewPaymentActivity.class);
            intent.putExtra(EXPENSE_ID_EXTRA, expenseId);
            intent.putExtra(PAYMENT_ID_EXTRA, item.getItemId());
            intent.putExtra(ACTIVITY_TITLE_EXTRA, R.string.header_title_payment);
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
            getPaymentsList();
        });
    }

    private void getPaymentsList() {
        List<PaymentInfo> toReturn = new ArrayList<>();
        List<Payment> allPayments = DAOUtil.getAllPaymentsByExpenseId(requireContext(), expenseId);

        if (!allPayments.isEmpty()) {
            Map<Integer, LocalDate> sortedIdDateMap = BudgetUtil.getSortedIdDateMap(allPayments);
            Map<Integer, Object> paymentsMap = BudgetUtil.getObjectsMap(allPayments);

            for (Map.Entry<Integer, LocalDate> sortedIdDate : sortedIdDateMap.entrySet()) {
                Payment payment = (Payment) paymentsMap.get(sortedIdDate.getKey());

                PaymentInfo paymentInfo = PaymentInfo.builder()
                        .itemId(payment.getId())
                        .title(payment.getTitle())
                        .state(PaymentState.valueOf(payment.getState()))
                        .amount(payment.getAmount())
                        .date(payment.getDate())
                        .payerLayout(getPayerLayout(payment))
                        .build();

                toReturn.add(paymentInfo);
            }
        }

        List<ListItem> listItems = preparePaymentsInfoList(toReturn, adapter.getItems());
        adapter.addItems(listItems);
    }

    private LinearLayout getPayerLayout(Payment payment) {
        Assignees assignees = null;

        if (StringUtils.isNotBlank(payment.getPayer())) {
            Person payer = DAOUtil.getPersonById(getContext(), Integer.parseInt(payment.getPayer()));
            assignees = new Assignees(getContext(), Collections.singletonList(payer), Location.LIST_ITEM);
        }

        return assignees != null ? assignees.getAssigneesContainer() : new LinearLayout(getContext());
    }

    private List<ListItem> preparePaymentsInfoList(List<PaymentInfo> paymentsInfoList, List<ListItem> list) {
        List<ListItem> toReturn = new ArrayList<>();

        for (PaymentInfo paymentInfo : paymentsInfoList) {
            HeaderItem headerItem = getHeaderItemWithDayOfWeek(requireContext(), paymentInfo.getDate());

            if (!toReturn.contains(headerItem) && !list.contains(headerItem)) {
                toReturn.add(headerItem);
            }

            toReturn.add(ContentItem.of(paymentInfo));
        }

        return toReturn;
    }

    private void setListeners() {
        setPaymentsFloatingButtonListener();
    }

    private void setPaymentsFloatingButtonListener() {
        binding.expensePaymentsFloatingButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), NewPaymentActivity.class);
            intent.putExtra(EXPENSE_ID_EXTRA, expenseId);
            startActivity(intent);
        });
    }
}