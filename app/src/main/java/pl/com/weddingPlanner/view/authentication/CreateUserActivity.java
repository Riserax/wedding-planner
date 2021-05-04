package pl.com.weddingPlanner.view.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityCreateUserBinding;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.NavigationActivity;

public class CreateUserActivity extends BaseActivity {

    private ActivityCreateUserBinding binding;

    private FirebaseAuth firebaseAuth;

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_user);

        setListeners();
    }

    private void setListeners() {
        binding.registerButton.setOnClickListener(v -> handleCreateUserWithEmailAndPassword("camil.kala@gmail.com", "haslo123"));
        binding.signInButton.setOnClickListener(v -> startActivity(new Intent(CreateUserActivity.this, SignInActivity.class)));
    }

    private void handleCreateUserWithEmailAndPassword(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, this::handleRegistrationTask);
    }

    private void handleRegistrationTask(Task<AuthResult> task) {
        if (task.isSuccessful()) {
            sendVerificationEmail(firebaseAuth.getCurrentUser());
            startActivity(new Intent(CreateUserActivity.this, NavigationActivity.class));
        } else {
            Toast.makeText(CreateUserActivity.this, "Niepowodzenie podczas rejestracji",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void sendVerificationEmail(FirebaseUser user) {
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(CreateUserActivity.this,
                            "Wysłano e-mail weryfikujący na adres " + user.getEmail(),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CreateUserActivity.this,
                            "Błąd podczas wysyłania e-maila weryfikującego",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
    }
}
