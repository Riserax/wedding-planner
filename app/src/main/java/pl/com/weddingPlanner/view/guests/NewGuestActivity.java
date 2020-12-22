package pl.com.weddingPlanner.view.guests;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityNewGuestBinding;
import pl.com.weddingPlanner.view.BaseActivity;

public class NewGuestActivity extends BaseActivity {

    private ActivityNewGuestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_guest);

        setActivityToolbarContentWithBackIcon(R.string.header_title_guest_new);
    }
}

