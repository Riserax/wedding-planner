package pl.com.weddingPlanner.view.budget;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class BudgetAdapter extends FragmentStateAdapter {

    private static final int BUDGET_TABS_NUMBER = 2;

    public BudgetAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new BudgetDescendingFragment();
            case 1:
                return new BudgetCategoriesFragment();
            default:
                throw new IllegalArgumentException("BudgetAdapter position=" + position + " but max=" + (BUDGET_TABS_NUMBER - 1));
        }
    }

    @Override
    public int getItemCount() {
        return BUDGET_TABS_NUMBER;
    }
}