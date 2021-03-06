package pl.com.weddingPlanner.view.guests;

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
import pl.com.weddingPlanner.databinding.FragmentGuestsListBinding;
import pl.com.weddingPlanner.model.GuestInfo;
import pl.com.weddingPlanner.persistence.entity.Guest;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.enums.GuestTypeEnum;
import pl.com.weddingPlanner.view.list.ContentItem;
import pl.com.weddingPlanner.view.list.ListItem;
import pl.com.weddingPlanner.view.list.ListRecyclerAdapter;
import pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView;

import static pl.com.weddingPlanner.view.guests.GuestsActivity.GUEST_ID_EXTRA;
import static pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView.PAGE_START;

public class GuestsListFragment extends Fragment {

    private FragmentGuestsListBinding binding;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private LinearLayoutManager linearLayoutManager;
    private ListRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_guests_list, container, false);

        setRecyclerView();
        setSwipeRefresh();

        setGuestsList();

        return binding.getRoot();
    }

    private void setRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(requireContext());
        adapter = new ListRecyclerAdapter(requireContext(), new LinkedList<>(), item -> {
            Intent intent = new Intent(requireContext(), GuestDetailsActivity.class);
            intent.putExtra(GUEST_ID_EXTRA, item.getItemId());
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
            setGuestsList();
        });
    }

    private void setGuestsList() {
        List<GuestInfo> toReturn = new ArrayList<>();
        List<Guest> allGuests = DAOUtil.getAllGuests(requireContext());

        for (Guest guest : allGuests) {
            GuestInfo guestInfo = GuestInfo.builder()
                    .itemId(guest.getId())
                    .type(GuestTypeEnum.valueOf(guest.getType()))
                    .nameSurname(guest.getNameSurname())
                    .tableNumber(guest.getTableNumber())
                    .build();

            toReturn.add(guestInfo);
        }

        List<ListItem> listItems = prepareGuestsInfoList(toReturn);
        adapter.addItems(listItems);
    }

    private List<ListItem> prepareGuestsInfoList(List<GuestInfo> guestInfoList) {
        List<ListItem> toReturn = new ArrayList<>();

        for (GuestInfo guestInfo : guestInfoList) {
            toReturn.add(ContentItem.of(guestInfo));
        }

        return toReturn;
    }
}