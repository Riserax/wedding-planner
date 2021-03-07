package pl.com.weddingPlanner.view.subcontractors;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import pl.com.weddingPlanner.view.guests.GuestsListFragment;

public class SubcontractorsAdapter extends FragmentStateAdapter {

    private static final int SUBCONTRACTORS_TABS_NUMBER = 2;

    public SubcontractorsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SubcontractorsListFragment();
            case 1:
                return new SubcontractorsCategoriesFragment();
            default:
                throw new IllegalArgumentException("SubcontractorsAdapter position=" + position + " but max=" + (SUBCONTRACTORS_TABS_NUMBER - 1));
        }
    }

    @Override
    public int getItemCount() {
        return SUBCONTRACTORS_TABS_NUMBER;
    }
}
