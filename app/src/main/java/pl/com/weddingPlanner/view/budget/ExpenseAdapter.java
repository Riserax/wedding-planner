package pl.com.weddingPlanner.view.budget;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ExpenseAdapter extends FragmentStateAdapter {

    private static final int EXPENSE_TABS_NUMBER = 2;

    private final int expenseId;

    public ExpenseAdapter(@NonNull FragmentActivity fragmentActivity, int expenseId) {
        super(fragmentActivity);
        this.expenseId = expenseId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ExpenseDetailsFragment(expenseId);
            case 1:
                return new ExpensePaymentsFragment();
            default:
                throw new IllegalArgumentException("ExpenseAdapter position=" + position + " but max=" + (EXPENSE_TABS_NUMBER - 1));
        }
    }

    @Override
    public int getItemCount() {
        return EXPENSE_TABS_NUMBER;
    }
}
