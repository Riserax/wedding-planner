package pl.com.weddingPlanner.view.guests;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.FragmentGuestsGroupsBinding;
import pl.com.weddingPlanner.enums.GuestGroup;
import pl.com.weddingPlanner.enums.GuestType;
import pl.com.weddingPlanner.enums.Presence;
import pl.com.weddingPlanner.model.info.GuestInfo;
import pl.com.weddingPlanner.persistence.entity.Guest;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.component.TableAndPresence;
import pl.com.weddingPlanner.view.list.ListItem;
import pl.com.weddingPlanner.view.list.ListRecyclerAdapter;
import pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView;
import pl.com.weddingPlanner.view.util.GuestUtil;

import static pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView.PAGE_START;
import static pl.com.weddingPlanner.view.util.ExtraUtil.GUEST_ID_EXTRA;

public class GuestsGroupsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentGuestsGroupsBinding binding;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;

    private LinearLayoutManager linearLayoutManager;
    private ListRecyclerAdapter adapter;
    private RecyclerView recyclerView;

    private List<GuestInfo> guestInfoList = new ArrayList<>();
    private String currentlySelectedItemInSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_guests_groups, container, false);

        setSpinner();
        setRecyclerView();
        setGuestsList();
        setListeners();

        return binding.getRoot();
    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_text_layout);

        for (GuestGroup group : GuestGroup.values()) {
            adapter.add(group.getText());
        }

        adapter.setDropDownViewResource(R.layout.spinner_item_layout);

        Spinner spinner = binding.groupsSpinner;
        spinner.setAdapter(adapter);
    }

    private void setRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(requireContext());
        adapter = new ListRecyclerAdapter(requireContext(), new LinkedList<>(), item -> {
            Intent intent = new Intent(requireContext(), GuestDetailsActivity.class);
            intent.putExtra(GUEST_ID_EXTRA, item.getItemId());
            startActivity(intent);
        });

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setListeners() {
        setRecyclerViewListener();
        setSwipeRefresh();
        setSpinnerListener();
    }

    private void setRecyclerViewListener() {
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
            prepareGuestList(currentlySelectedItemInSpinner);
        });
    }

    private void setSpinnerListener() {
        binding.groupsSpinner.setOnItemSelectedListener(this);
    }

    private void setGuestsList() {
        List<Guest> allGuests = DAOUtil.getAllGuests(requireContext());
        guestInfoList.clear();

        for (Guest guest : allGuests) {
            GuestInfo guestInfo = GuestInfo.builder()
                    .itemId(guest.getId())
                    .type(GuestType.valueOf(guest.getType()))
                    .nameSurname(guest.getNameSurname())
                    .ageRange(guest.getAgeRange())
                    .tableNumber(guest.getTableNumber())
                    .presence(StringUtils.isNotBlank(guest.getPresence()) ? Presence.valueOf(guest.getPresence()) : Presence.NONE)
                    .build();

            TableAndPresence tableAndPresence = new TableAndPresence(getContext(), guestInfo);

            guestInfo.setTablePresenceLayout(tableAndPresence.getLayoutContainer());

            guestInfoList.add(guestInfo);
        }
    }

    private void prepareGuestList(String selectedItem) {
        List<ListItem> listItems = GuestUtil.getGroupedGuests(selectedItem, guestInfoList, getContext());
        adapter.clear();
        adapter.addItems(listItems);
        currentlySelectedItemInSpinner = selectedItem;
    }

    @Override
    public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
        String selectedItem = (String) parentView.getItemAtPosition(position);
        prepareGuestList(selectedItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}