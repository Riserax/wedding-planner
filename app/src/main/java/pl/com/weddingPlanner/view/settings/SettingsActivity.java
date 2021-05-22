package pl.com.weddingPlanner.view.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivitySettingsBinding;
import pl.com.weddingPlanner.enums.Settings;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.authentication.SignInActivity;

import static pl.com.weddingPlanner.view.util.SideBySideListUtil.renderButtons;

public class SettingsActivity extends BaseActivity {

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        setToolbarContentWithBackIcon(R.string.header_title_settings, this, R.id.navigation_more);

        renderButtons(
                this,
                getMenuItems(),
                binding.sideBySideMenu.leftColumn,
                binding.sideBySideMenu.rightColumn);

        setListeners();
    }

    private List<Settings> getMenuItems() {
        return new ArrayList<>(Arrays.asList(Settings.values()));
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
//        binding.inviteToWeddingButton.setOnClickListener(v -> {
//            startActivity(new Intent(SettingsActivity.this, InviteToWeddingActivity.class));
//        });
//
//        binding.joinWeddingButton.setOnClickListener(v -> {
//            new JoinWeddingDialog(SettingsActivity.this).showDialog();
//        });

        binding.signOutButton.setOnClickListener(v -> signOut());
    }

    private void signOut() {
        firebaseAuth.signOut();
        startActivity(new Intent(SettingsActivity.this, SignInActivity.class));
    }
}