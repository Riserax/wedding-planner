package pl.com.weddingPlanner.view.budget;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityExpenseBinding;
import pl.com.weddingPlanner.view.BaseActivity;

public class ExpenseActivity extends BaseActivity {

    public static String EXPENSE_ID_EXTRA = "expenseIdExtra";

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private ActivityExpenseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_expense);

        setActivityToolbarContentWithBackIcon(R.string.header_title_expense_details);

        initLayouts();
        initAndSetViewPager();
        attachTabLayoutMediator();
    }

    private void initLayouts() {
        tabLayout = binding.expenseTabs;
        viewPager = binding.expenseViewPager;
    }

    private void initAndSetViewPager() {
        int expenseId = getIntent().getExtras().getInt(EXPENSE_ID_EXTRA, 0);

        viewPager.setAdapter(new ExpenseAdapter(ExpenseActivity.this, expenseId));
        viewPager.setOffscreenPageLimit(1);
    }

    private void attachTabLayoutMediator() {
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(getString(R.string.tab_title_expense_details));
                            break;
                        case 1:
                            tab.setText(getString(R.string.tab_title_expense_payments));
                            break;
                    }
                }).attach();
    }
}
