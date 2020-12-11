package pl.com.weddingPlanner.view.tasks;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TasksAdapter extends FragmentStateAdapter {

    private static final int TASKS_TABS_NUMBER = 2;

    public TasksAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TasksCategoriesFragment();
            case 1:
                return new TasksMonthsFragment();
            default:
                throw new IllegalArgumentException("TasksAdapter position=" + position + " but max=" + (TASKS_TABS_NUMBER - 1));
        }
    }

    @Override
    public int getItemCount() {
        return TASKS_TABS_NUMBER;
    }
}