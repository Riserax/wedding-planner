package pl.com.weddingPlanner.view.weddings;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityWeddingChoiceBinding;
import pl.com.weddingPlanner.view.BaseActivity;

public class WeddingChoiceActivity extends BaseActivity {

    private ActivityWeddingChoiceBinding binding;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance(getString(R.string.firebase_database_url)).getReference();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wedding_choice);

        setListeners();
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
}
