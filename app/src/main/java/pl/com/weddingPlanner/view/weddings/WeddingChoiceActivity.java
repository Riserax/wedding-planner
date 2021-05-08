package pl.com.weddingPlanner.view.weddings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityWeddingChoiceBinding;
import pl.com.weddingPlanner.model.User;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.util.ButtonsUtil;

public class WeddingChoiceActivity extends BaseActivity {

    private ActivityWeddingChoiceBinding binding;

    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wedding_choice);

        establishFirebase();
        setComponents();
        setChoicesVisibility();
        setListeners();
    }

    private void establishFirebase() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance(getString(R.string.firebase_database_url)).getReference();
    }

    private void setComponents() {
        ButtonsUtil.setBorderButtonDisabled(binding.joinButton, this);
    }

    private void setChoicesVisibility() {
        databaseReference.child("users").child(currentUser.getUid()).get().addOnCompleteListener(task -> {
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
