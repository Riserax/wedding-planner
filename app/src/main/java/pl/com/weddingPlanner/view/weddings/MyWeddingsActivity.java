package pl.com.weddingPlanner.view.weddings;

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
import pl.com.weddingPlanner.databinding.ActivityMyWeddingsBinding;
import pl.com.weddingPlanner.model.User;
import pl.com.weddingPlanner.model.info.MyWeddingInfo;
import pl.com.weddingPlanner.util.FirebaseUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.NavigationActivity;
import pl.com.weddingPlanner.view.list.ContentItem;
import pl.com.weddingPlanner.view.list.ListItem;
import pl.com.weddingPlanner.view.list.ListRecyclerAdapter;
import pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView;

import static pl.com.weddingPlanner.util.FirebaseUtil.isSuccessfulAndNotNull;
import static pl.com.weddingPlanner.view.NavigationActivity.FRAGMENT_TO_LOAD_ID;

public class MyWeddingsActivity extends BaseActivity {

    private ActivityMyWeddingsBinding binding;

    private LinearLayoutManager linearLayoutManager;
    private ListRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_weddings);
        setActivityToolbarContentWithBackIcon(R.string.header_title_weddings_list);

        setRecyclerView();
        setSwipeRefresh();

        setMyWeddingsList();
    }

    private void setRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new ListRecyclerAdapter(this, new LinkedList<>(), item -> {
            FirebaseUtil.getUser(databaseReference, currentUser).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUtil.getUserChild(databaseReference, currentUser).child("currentWedding").setValue(item.getItemUUID());
                    setIntentAndStartActivity();
                }
            });
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

    private void setIntentAndStartActivity() {
        Intent intent = new Intent(MyWeddingsActivity.this, NavigationActivity.class);
        intent.putExtra(FRAGMENT_TO_LOAD_ID, R.id.navigation_dashboard);
        startActivity(intent);
    }

    private void setSwipeRefresh() {
        SwipeRefreshLayout swipeRefresh = binding.swipeRefresh;
        swipeRefresh.setOnRefreshListener(() -> {
            swipeRefresh.setRefreshing(false);
            adapter.clear();
            setMyWeddingsList();
        });
    }

    private void setMyWeddingsList() {
        FirebaseUtil.getUser(databaseReference, currentUser).addOnCompleteListener(getUserTask -> {
            if (isSuccessfulAndNotNull(getUserTask)) {
                User userInfo = getUserTask.getResult().getValue(User.class);

                List<MyWeddingInfo> toReturn = new ArrayList<>();
                for (String weddingId : userInfo.getWeddings()) {
                    MyWeddingInfo myWeddingInfo = MyWeddingInfo.builder()
                            .id(weddingId)
                            .name(weddingId) //TODO
                            .build();
                    toReturn.add(myWeddingInfo);
                }

                List<ListItem> listItems = prepareMyWeddingsInfoList(toReturn);
                adapter.addItems(listItems);
            }
        });
    }

    //TODO pobranie wszystkich wesel użytkownika
//    private void getWeddingAndAddToList(String weddingId, List<MyWeddingInfo> toReturn, int weddingsAddedToList) {
//        FirebaseUtil.getWedding(databaseReference, weddingId).addOnCompleteListener(getWeddingTask -> {
//            if (isSuccessfulAndNotNull(getWeddingTask)) {
//                Wedding wedding = getWeddingTask.getResult().getValue(Wedding.class);
//                addWeddingToList(wedding, toReturn, weddingsAddedToList);
//            }
//        });
//    }
//
//    private void addWeddingToList(Wedding wedding, List<MyWeddingInfo> toReturn, int weddingsAddedToList) {
//        if (wedding != null) {
//            MyWeddingInfo myWeddingInfo = MyWeddingInfo.builder()
//                    .id(wedding.getId())
//                    .name(wedding.getName())
//                    .build();
//            toReturn.add(myWeddingInfo);
//            weddingsAddedToList++;
//        }
//    }

    private List<ListItem> prepareMyWeddingsInfoList(List<MyWeddingInfo> myWeddingsInfoList) {
        List<ListItem> toReturn = new ArrayList<>();

        for (MyWeddingInfo myWeddingInfo : myWeddingsInfoList) {
            toReturn.add(ContentItem.of(myWeddingInfo));
        }

        return toReturn;
    }
}
