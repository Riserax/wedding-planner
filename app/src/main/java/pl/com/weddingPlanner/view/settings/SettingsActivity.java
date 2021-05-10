package pl.com.weddingPlanner.view.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivitySettingsBinding;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.authentication.SignInActivity;
import pl.com.weddingPlanner.view.weddings.dialog.JoinWeddingDialog;

public class SettingsActivity extends BaseActivity {

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        setToolbarContentWithBackIcon(R.string.header_title_settings, this, R.id.navigation_more);

        setListeners();
    }

//    private void showUsername() {
//        FirebaseUtil.getUserChild(databaseReference, currentUser).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                if ("username".equals(snapshot.getKey())) {
//                    binding.message.setText((String) snapshot.getValue());
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                if ("username".equals(snapshot.getKey())) {
//                    binding.message.setText((String) snapshot.getValue());
//                }
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void setListeners() {
        binding.inviteToWeddingButton.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, InviteToWeddingActivity.class));
        });

        binding.joinWeddingButton.setOnClickListener(v -> {
            new JoinWeddingDialog(SettingsActivity.this).showDialog();
        });

        binding.signOutButton.setOnClickListener(v -> signOut());
    }

    private void signOut() {
        firebaseAuth.signOut();
        startActivity(new Intent(SettingsActivity.this, SignInActivity.class));
    }
}