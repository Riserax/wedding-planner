package pl.com.weddingPlanner.view.budget;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityExpenseBinding;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.BaseActivity;

public class ExpenseActivity extends BaseActivity {

    public static String EXPENSE_ID_EXTRA = "expenseIdExtra";
    public static String TAB_ID_EXTRA = "tabIdExtra";
    public static int TAB_PAYMENTS_ID = 1;

    private int expenseId;
    private int paymentsCount = 0;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private ActivityExpenseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_expense);

        setToolbarContentWithBackIcon(R.string.header_title_expense_details, this, R.id.navigation_budget);

        initLayouts();
        initAndSetViewPager();
        attachTabLayoutMediator();
    }

    private void initLayouts() {
        tabLayout = binding.expenseTabs;
        viewPager = binding.expenseViewPager;
    }

    private void initAndSetViewPager() {
        expenseId = getIntent().getExtras().getInt(EXPENSE_ID_EXTRA, 0);

        viewPager.setAdapter(new ExpenseAdapter(ExpenseActivity.this, expenseId));
        viewPager.setOffscreenPageLimit(1);
    }

    private void attachTabLayoutMediator() {
        if (expenseId > 0) {
            paymentsCount = DAOUtil.getPaymentsCountByExpenseId(this, expenseId);
        }

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(getString(R.string.tab_title_expense_details));
                            break;
                        case 1:
                            tab.setText(String.format(getString(R.string.tab_title_expense_payments), paymentsCount));
                            break;
                    }
                }).attach();

        selectTab();
    }

    private void selectTab() {
        int tabId = getIntent().getExtras().getInt(TAB_ID_EXTRA, 0);

        TabLayout.Tab tab = tabLayout.getTabAt(tabId);
        tab.select();
    }
}
