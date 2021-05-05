package pl.com.weddingPlanner.view.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivitySettingsBinding;
import pl.com.weddingPlanner.model.User;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.authentication.SignInActivity;

public class SettingsActivity extends BaseActivity {

    private ActivitySettingsBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance(getString(R.string.firebase_database_url)).getReference();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        setToolbarContentWithBackIcon(R.string.header_title_settings, this, R.id.navigation_more);

        setListeners();
    }

    private void setListeners() {
        binding.signOutButton.setOnClickListener(v -> signOut());

        binding.testReadButton.setOnClickListener(v -> {
            databaseReference.child("users").child(currentUser.getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null && task.getResult().exists() && task.getResult().getValue() != null) {
                    User user = task.getResult().getValue(User.class);
                    binding.testReadButton.setText(user.getUsername());
                }
            });
        });
    }

    private void signOut() {
        firebaseAuth.signOut();
        startActivity(new Intent(SettingsActivity.this, SignInActivity.class));
    }
}