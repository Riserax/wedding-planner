package pl.com.weddingPlanner.view.guests;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityGuestsBinding;
import pl.com.weddingPlanner.view.BaseActivity;

public class GuestsActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private ActivityGuestsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guests);

        setToolbarContentWithBackIcon(R.string.header_title_guests, this, R.id.navigation_more);

        initLayouts();
        initAndSetViewPager();
        attachTabLayoutMediator();
    }

    private void initLayouts() {
        tabLayout = binding.guestsTabs;
        viewPager = binding.guestsViewPager;
    }

    private void initAndSetViewPager() {
        viewPager.setAdapter(new GuestsAdapter(GuestsActivity.this));
        viewPager.setOffscreenPageLimit(1);
    }

    private void attachTabLayoutMediator() {
        new TabLayoutMediator(tabLayout, viewPager,
            (tab, position) -> {
                switch (position) {
                    case 0:
                        tab.setText(getString(R.string.tab_title_guests_list));
                        break;
                    case 1:
                        tab.setText(getString(R.string.tab_title_guests_groups));
                        break;
                }
            }).attach();
    }
}
