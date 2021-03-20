package pl.com.weddingPlanner.view.subcontractors;

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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.FragmentSubcontractorsListBinding;
import pl.com.weddingPlanner.enums.CategoryTypeEnum;
import pl.com.weddingPlanner.enums.CollaborationStageEnum;
import pl.com.weddingPlanner.enums.PaymentStateEnum;
import pl.com.weddingPlanner.model.info.SubcontractorInfo;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Expense;
import pl.com.weddingPlanner.persistence.entity.Payment;
import pl.com.weddingPlanner.persistence.entity.Subcontractor;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.component.StageAndPayments;
import pl.com.weddingPlanner.view.list.ContentItem;
import pl.com.weddingPlanner.view.list.ListItem;
import pl.com.weddingPlanner.view.list.ListRecyclerAdapter;
import pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView;

import static pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView.PAGE_START;
import static pl.com.weddingPlanner.view.util.ExtraUtil.SUBCONTRACTOR_ID_EXTRA;

public class SubcontractorsListFragment extends Fragment {

    private FragmentSubcontractorsListBinding binding;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private LinearLayoutManager linearLayoutManager;
    private ListRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subcontractors_list, container, false);

        setRecyclerView();
        setSwipeRefresh();

        setSubcontractorsList();

        return binding.getRoot();
    }

    private void setRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(requireContext());
        adapter = new ListRecyclerAdapter(requireContext(), new LinkedList<>(), item -> {
            Intent intent = new Intent(requireContext(), SubcontractorDetailsActivity.class);
            intent.putExtra(SUBCONTRACTOR_ID_EXTRA, item.getItemId());
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
            setSubcontractorsList();
        });
    }

    private void setSubcontractorsList() {
        List<SubcontractorInfo> toReturn = new ArrayList<>();
        List<Subcontractor> allSubcontractors = DAOUtil.getAllSubcontractors(requireContext());

        for (Subcontractor subcontractor : allSubcontractors) {
            Category category = DAOUtil.getCategoryByNameAndType(requireContext(), subcontractor.getCategory(), CategoryTypeEnum.SUBCONTRACTORS.name());

            SubcontractorInfo subcontractorInfo = SubcontractorInfo.builder()
                    .itemId(subcontractor.getId())
                    .name(subcontractor.getName())
                    .categoryIconId(category.getIconId())
                    .collaborationStage(CollaborationStageEnum.valueOf(subcontractor.getCollaborationStage()))
                    .paymentsPercentage(getPaymentsPercentage(subcontractor))
                    .build();

            StageAndPayments stageAndPayments = new StageAndPayments(getContext(), subcontractorInfo);
            subcontractorInfo.setStagePaymentsLayout(stageAndPayments.getLayoutContainer());

            toReturn.add(subcontractorInfo);
        }

        List<ListItem> listItems = prepareSubcontractorsInfoList(toReturn);
        adapter.addItems(listItems);
    }

    private int getPaymentsPercentage(Subcontractor subcontractor) {
        Expense expense = DAOUtil.getExpenseById(getContext(), subcontractor.getExpenseId());
        int percentage = 0;

        if (expense != null) {
            double initialAmount = Double.parseDouble(expense.getInitialAmount());
            double paidPaymentsSum = getPaidPaymentsSum(subcontractor);

            if (initialAmount == 0) {
                if (paidPaymentsSum > 0) {
                    percentage = 100;
                }
            } else {
                percentage = (int) (paidPaymentsSum / initialAmount * 100);
            }
        }

        return percentage;
    }

    private double getPaidPaymentsSum(Subcontractor subcontractor) {
        List<Payment> allPayments = DAOUtil.getAllPaymentsByExpenseId(getContext(), subcontractor.getExpenseId());
        double paidPaymentsSum = 0.00;

        for (Payment payment : allPayments) {
            if (PaymentStateEnum.PAID == PaymentStateEnum.valueOf(payment.getState())) {
                paidPaymentsSum += Double.parseDouble(payment.getAmount());
            }
        }

        return paidPaymentsSum;
    }

    private List<ListItem> prepareSubcontractorsInfoList(List<SubcontractorInfo> subcontractorInfoList) {
        List<ListItem> toReturn = new ArrayList<>();

        for (SubcontractorInfo subcontractorInfo : subcontractorInfoList) {
            toReturn.add(ContentItem.of(subcontractorInfo));
        }

        return toReturn;
    }
}