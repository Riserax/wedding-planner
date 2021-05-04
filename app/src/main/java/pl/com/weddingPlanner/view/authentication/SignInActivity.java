package pl.com.weddingPlanner.view.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivitySignInBinding;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.NavigationActivity;

public class SignInActivity extends BaseActivity {

    private ActivitySignInBinding binding;

    private FirebaseAuth firebaseAuth;

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);

        setListeners();
    }

    private void setListeners() {
        binding.signInButton.setOnClickListener(v -> handleSignInWithEmailAndPassword("camil.kala@gmail.com", "haslo123"));
        binding.registerButton.setOnClickListener(v -> startActivity(new Intent(SignInActivity.this, CreateUserActivity.class)));
    }

    private void handleSignInWithEmailAndPassword(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, this::handleSignInTask);
    }

    private void handleSignInTask(Task<AuthResult> task) {
        if (task.isSuccessful()) {
            startActivity(new Intent(SignInActivity.this, NavigationActivity.class));
        } else {
            Toast.makeText(SignInActivity.this, "Niepowodzenie podczas logowania",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
