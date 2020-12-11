package pl.com.weddingPlanner.view.guests;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class GuestsAdapter extends FragmentStateAdapter {

    private static final int GUESTS_TABS_NUMBER = 2;

    public GuestsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new GuestsListFragment();
            case 1:
                return new GuestsGroupsFragment();
            default:
                throw new IllegalArgumentException("GuestsAdapter position=" + position + " but max=" + (GUESTS_TABS_NUMBER - 1));
        }
    }

    @Override
    public int getItemCount() {
        return GUESTS_TABS_NUMBER;
    }
}