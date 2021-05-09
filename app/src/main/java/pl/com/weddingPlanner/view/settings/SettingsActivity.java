package pl.com.weddingPlanner.view.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivitySettingsBinding;
import pl.com.weddingPlanner.util.FirebaseUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.authentication.SignInActivity;
import pl.com.weddingPlanner.view.dialog.JoinWeddingDialog;

public class SettingsActivity extends BaseActivity {

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        setToolbarContentWithBackIcon(R.string.header_title_settings, this, R.id.navigation_more);

        showUsername();
        setListeners();
    }

    private void showUsername() {
        FirebaseUtil.getUserChild(databaseReference, currentUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if ("username".equals(snapshot.getKey())) {
                    binding.message.setText((String) snapshot.getValue());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if ("username".equals(snapshot.getKey())) {
                    binding.message.setText((String) snapshot.getValue());
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setListeners() {
        binding.signOutButton.setOnClickListener(v -> signOut());

        binding.joinOtherWeddingButton.setOnClickListener(v -> {
            new JoinWeddingDialog(SettingsActivity.this).showDialog();
        });
    }

    private void signOut() {
        firebaseAuth.signOut();
        startActivity(new Intent(SettingsActivity.this, SignInActivity.class));
    }
}