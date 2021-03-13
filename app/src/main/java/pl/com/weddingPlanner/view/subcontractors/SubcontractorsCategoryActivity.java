package pl.com.weddingPlanner.view.subcontractors;

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
import pl.com.weddingPlanner.databinding.ActivityCategorySubcontractorsBinding;
import pl.com.weddingPlanner.enums.CategoryTypeEnum;
import pl.com.weddingPlanner.model.info.SubcontractorInfo;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Subcontractor;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.list.ContentItem;
import pl.com.weddingPlanner.view.list.ListItem;
import pl.com.weddingPlanner.view.list.ListRecyclerAdapter;
import pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView;

import static pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView.PAGE_START;
import static pl.com.weddingPlanner.view.util.ExtraUtil.SUBCONTRACTOR_ID_EXTRA;
import static pl.com.weddingPlanner.view.util.SideBySideListUtil.CATEGORY_NAME_EXTRA;

public class SubcontractorsCategoryActivity extends BaseActivity {

    private ActivityCategorySubcontractorsBinding binding;

    private String categoryName;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private LinearLayoutManager linearLayoutManager;
    private ListRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category_subcontractors);

        setActivityToolbarContentWithBackIcon(getCategoryNameAndSetVariable());

        setRecyclerView();
        setSwipeRefresh();

        getList();
        setListeners();
    }

    private String getCategoryNameAndSetVariable() {
        StringBuilder categoryNameBuilder = new StringBuilder();
        String categoryName = getIntent().getExtras().getString(CATEGORY_NAME_EXTRA, getResources().getString(R.string.header_title_category));

        categoryNameBuilder.append(getResources().getString(R.string.header_title_subcontractors)).append(" - ").append(categoryName);

        this.categoryName = categoryName;

        return categoryNameBuilder.toString();
    }

    private void setRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new ListRecyclerAdapter(this, new LinkedList<>(), item -> {
            Intent intent = new Intent(this, SubcontractorDetailsActivity.class);
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
            getList();
        });
    }

    private void getList() {
        List<SubcontractorInfo> toReturn = new ArrayList<>();
        List<Subcontractor> allSubcontractors = DAOUtil.getAllSubcontractorsByCategory(this, categoryName);

        for (Subcontractor subcontractor : allSubcontractors) {
            Category category = DAOUtil.getCategoryByNameAndType(this, subcontractor.getCategory(), CategoryTypeEnum.SUBCONTRACTORS.name());

            SubcontractorInfo subcontractorInfo = SubcontractorInfo.builder()
                    .itemId(subcontractor.getId())
                    .name(subcontractor.getName())
                    .categoryIconId(category.getIconId())
//                    .subcontractorStage(SubcontractorStageEnum.valueOf(subcontractor.getStage()))
//                    .paymentPercentage()
                    .build();

            toReturn.add(subcontractorInfo);
        }

        List<ListItem> listItems = prepareSubcontractorsInfoList(toReturn);
        adapter.addItems(listItems);
    }

    private List<ListItem> prepareSubcontractorsInfoList(List<SubcontractorInfo> subcontractorInfoList) {
        List<ListItem> toReturn = new ArrayList<>();

        for (SubcontractorInfo subcontractorInfo : subcontractorInfoList) {
            toReturn.add(ContentItem.of(subcontractorInfo));
        }

        return toReturn;
    }

    private void setListeners() {
        binding.categoryFloatingButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewSubcontractorActivity.class);
            startActivity(intent);
        });
    }
}