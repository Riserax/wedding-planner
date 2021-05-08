package pl.com.weddingPlanner.view.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivitySettingsBinding;
import pl.com.weddingPlanner.model.Wedding;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.authentication.SignInActivity;

public class SettingsActivity extends BaseActivity {

    private ActivitySettingsBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        setToolbarContentWithBackIcon(R.string.header_title_settings, this, R.id.navigation_more);

        establishFirebase();
        showUsername();
        setListeners();
    }

    private void establishFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance(getString(R.string.firebase_database_url)).getReference();
        databaseReference.keepSynced(true);
    }

    private void showUsername() {
        databaseReference.child("users").child(currentUser.getUid()).addChildEventListener(new ChildEventListener() {
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

        binding.testReadButton.setOnClickListener(v -> {
            databaseReference.child("weddings").child("69d7d5c8-630b-47b1-9013-8c24c5352d64").get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null && task.getResult().exists() && task.getResult().getValue() != null) {
                    Wedding wedding = task.getResult().getValue(Wedding.class);
                    binding.testReadButton.setText(wedding.getName());
                }
            });
        });
    }

    private void signOut() {
        firebaseAuth.signOut();
        startActivity(new Intent(SettingsActivity.this, SignInActivity.class));
    }
}