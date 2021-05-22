package pl.com.weddingPlanner.view.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityHelpersBinding;
import pl.com.weddingPlanner.model.info.MyWeddingInfo;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.list.ContentItem;
import pl.com.weddingPlanner.view.list.ListItem;
import pl.com.weddingPlanner.view.list.ListRecyclerAdapter;
import pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView;

public class HelpersActivity extends BaseActivity {

    private ActivityHelpersBinding binding;

    private LinearLayoutManager linearLayoutManager;
    private ListRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_helpers);
        setActivityToolbarContentWithBackIcon(R.string.header_title_helpers);

        binding.toolbarLayout.addButton.setVisibility(View.VISIBLE);

        setRecyclerView();
        setSwipeRefresh();

        setHelpersList();
        setListeners();
    }

    private void setRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new ListRecyclerAdapter(this, new LinkedList<>(), item -> {
            //TODO
        });

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationListenerRecyclerView(linearLayoutManager) {
            @Override
            public boolean isLoading() {
                return false;
            }

            @Override
            public boolean isLastPage() {
                return false;
            }

            @Override
            protected void loadMoreItems() {
            }
        });
    }

    private void setSwipeRefresh() {
        SwipeRefreshLayout swipeRefresh = binding.swipeRefresh;
        swipeRefresh.setOnRefreshListener(() -> {
            swipeRefresh.setRefreshing(false);
            adapter.clear();
            setHelpersList();
        });
    }

    private void setHelpersList() {
        List<ListItem> listItems = prepareHelpersList(new ArrayList<>());
        adapter.addItems(listItems);
    }

    private List<ListItem> prepareHelpersList(List<MyWeddingInfo> myWeddingsInfoList) {
        List<ListItem> toReturn = new ArrayList<>();

        for (MyWeddingInfo myWeddingInfo : myWeddingsInfoList) {
            toReturn.add(ContentItem.of(myWeddingInfo));
        }

        return toReturn;
    }

    private void setListeners() {
        binding.toolbarLayout.addButton.setOnClickListener(v -> {
            startActivity(new Intent(HelpersActivity.this, InviteToWeddingActivity.class));
        });
    }
}
