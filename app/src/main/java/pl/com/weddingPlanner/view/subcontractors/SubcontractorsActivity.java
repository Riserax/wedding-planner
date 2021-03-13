package pl.com.weddingPlanner.view.subcontractors;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivitySubcontractorsBinding;
import pl.com.weddingPlanner.view.BaseActivity;

public class SubcontractorsActivity extends BaseActivity {

    private ActivitySubcontractorsBinding binding;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subcontractors);

        setToolbarContentWithBackIcon(R.string.header_title_subcontractors, this, R.id.navigation_more);

        initLayouts();
        initAndSetViewPager();
        attachTabLayoutMediator();
        setListeners();
    }

    private void initLayouts() {
        tabLayout = binding.tabs;
        viewPager = binding.viewPager;
    }

    private void initAndSetViewPager() {
        viewPager.setAdapter(new SubcontractorsAdapter(SubcontractorsActivity.this));
        viewPager.setOffscreenPageLimit(1);
    }

    private void attachTabLayoutMediator() {
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(getString(R.string.tab_title_subcontractors_list));
                            break;
                        case 1:
                            tab.setText(getString(R.string.tab_title_subcontractors_categories));
                            break;
                    }
                }).attach();
    }

    private void setListeners() {
        binding.floatingButton.setOnClickListener(view -> {
            Intent intent;
            switch (viewPager.getCurrentItem()) {
                case 0:
                    intent = new Intent(this, NewSubcontractorActivity.class);
                    startActivity(intent);
                    break;
                case 1:
//                    intent = new Intent(this, NewTableActivity.class);
//                    startActivity(intent);
            }
        });
    }
}