package pl.com.weddingPlanner.view.tasks;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.Map;

public class TasksMonthsAdapter extends FragmentStateAdapter {

    private Map<Integer, String> months;

    public TasksMonthsAdapter(@NonNull FragmentActivity fragmentActivity, Map<Integer, String> months) {
        super(fragmentActivity);
        this.months = months;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new TasksMonthFragment(months.get(position));
    }

    @Override
    public int getItemCount() {
        return months.size();
    }

}
