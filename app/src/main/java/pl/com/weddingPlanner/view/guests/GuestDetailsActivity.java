package pl.com.weddingPlanner.view.guests;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityGuestDetailsBinding;
import pl.com.weddingPlanner.view.BaseActivity;

public class GuestDetailsActivity extends BaseActivity {

    private ActivityGuestDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guest_details);

        setActivityToolbarContentWithBackIcon(R.string.header_title_guest_details);
    }
}

