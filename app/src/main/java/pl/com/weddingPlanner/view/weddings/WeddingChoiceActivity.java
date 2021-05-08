package pl.com.weddingPlanner.view.weddings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityWeddingChoiceBinding;
import pl.com.weddingPlanner.model.User;
import pl.com.weddingPlanner.util.FirebaseUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.util.ButtonsUtil;

public class WeddingChoiceActivity extends BaseActivity {

    private ActivityWeddingChoiceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wedding_choice);

        setComponents();
        setChoicesVisibility();
        setListeners();
    }

    private void setComponents() {
        ButtonsUtil.setBorderButtonDisabled(binding.joinButton, this);
    }

    private void setChoicesVisibility() {
        FirebaseUtil.getUser(databaseReference, currentUser).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                User user = task.getResult().getValue(User.class);
                if (user != null && (user.getWeddings() == null || user.getWeddings().isEmpty())) {
                    binding.chooseExistingButton.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setListeners() {
        binding.newWeddingButton.setOnClickListener(v -> {
            startActivity(new Intent(WeddingChoiceActivity.this, NewWeddingActivity.class));
        });

        binding.chooseExistingButton.setOnClickListener(v -> {
            //TODO
        });

        binding.joinButton.setOnClickListener(v -> {
            //TODO
        });
    }

    @Override
    public void onBackPressed() {
    }
}
