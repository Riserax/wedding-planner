package pl.com.weddingPlanner.view.guests;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.FragmentGuestsGroupsBinding;
import pl.com.weddingPlanner.enums.GuestTypeEnum;
import pl.com.weddingPlanner.enums.PresenceEnum;
import pl.com.weddingPlanner.model.info.GuestInfo;
import pl.com.weddingPlanner.persistence.entity.Guest;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.component.TableAndPresence;
import pl.com.weddingPlanner.view.list.ContentItem;
import pl.com.weddingPlanner.view.list.HeaderItem;
import pl.com.weddingPlanner.view.list.ListItem;
import pl.com.weddingPlanner.view.list.ListRecyclerAdapter;
import pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView;
import pl.com.weddingPlanner.view.util.GuestUtil;

import static pl.com.weddingPlanner.view.list.PaginationListenerRecyclerView.PAGE_START;
import static pl.com.weddingPlanner.view.util.ExtraUtil.GUEST_ID_EXTRA;

public class GuestsGroupsFragment extends Fragment {

    private FragmentGuestsGroupsBinding binding;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private LinearLayoutManager linearLayoutManager;
    private ListRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_guests_groups, container, false);

        setSpinner();
        setRecyclerView();
        setSwipeRefresh();

        setGuestsList();

        return binding.getRoot();
    }

    private void setSpinner() {
        List<String> groups = Arrays.asList("Status obecności", "Przedział wiekowy", "Stoły", "Kategorie");
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_text_layout);

        for (String group : groups) {
            adapter.add(group);
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
                    .presence(StringUtils.isNotBlank(guest.getPresence()) ? PresenceEnum.valueOf(guest.getPresence()) : PresenceEnum.NONE)
                    .build();

            TableAndPresence tableAndPresence = new TableAndPresence(getContext(), guestInfo);

            guestInfo.setTablePresenceLayout(tableAndPresence.getLayoutContainer());

            toReturn.add(guestInfo);
        }

        List<ListItem> listItems = prepareGuestsInfoList(toReturn);
        adapter.addItems(listItems);
    }

    private List<ListItem> prepareGuestsInfoList(List<GuestInfo> guestInfoList) {
        List<ListItem> toReturn = new ArrayList<>();

        Map<PresenceEnum, List<GuestInfo>> guestsPerPresences = GuestUtil.groupGuestsPerPresences(guestInfoList);

        for (Map.Entry<PresenceEnum, List<GuestInfo>> guestsPerPresence : guestsPerPresences.entrySet()) {
            String headerText = getContext().getString(guestsPerPresence.getKey().getTextResourceId());
            HeaderItem headerItem = new HeaderItem(headerText);
            toReturn.add(headerItem);

            for (GuestInfo guest : guestsPerPresence.getValue()) {
                toReturn.add(ContentItem.of(guest));
            }
        }

        return toReturn;
    }
}