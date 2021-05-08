package pl.com.weddingPlanner.view.authentication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityCreateUserBinding;
import pl.com.weddingPlanner.model.User;
import pl.com.weddingPlanner.util.FirebaseUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.util.ComponentsUtil;
import pl.com.weddingPlanner.view.weddings.WeddingChoiceActivity;

import static pl.com.weddingPlanner.view.util.ComponentsUtil.setButtonEnablity;
import static pl.com.weddingPlanner.view.util.LambdaUtil.getOnTextChangedTextWatcher;

public class CreateUserActivity extends BaseActivity {

    private ActivityCreateUserBinding binding;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_user);

        setButtonEnablity(binding.registerButton, false);
        setListeners();
    }

    private void setListeners() {
        setRegisterButtonEnableStatusListener();
        setRootLayoutListener();
        setOnFocusChangeListener();

        binding.registerButton.setOnClickListener(v -> {
            ComponentsUtil.hideKeyboard(this, getCurrentFocus());
            handleCreateUserWithEmailAndPassword(binding.email.getText().toString(), binding.password.getText().toString());
        });

        binding.signInButton.setOnClickListener(v -> {
            startActivity(new Intent(CreateUserActivity.this, SignInActivity.class));
        });

        binding.regulationCheckbox.setOnCheckedChangeListener((compoundButton, b) -> {
            ComponentsUtil.hideKeyboard(this, getCurrentFocus());
            setButtonEnablity(binding.registerButton, areFieldsValid());
        });
    }

    private void setRegisterButtonEnableStatusListener() {
        TextWatcher listener = getOnTextChangedTextWatcher((s, start, before, count) ->
                setButtonEnablity(binding.registerButton, areFieldsValid())
        );

        binding.email.addTextChangedListener(listener);
        binding.username.addTextChangedListener(listener);
        binding.password.addTextChangedListener(listener);
    }

    private boolean areFieldsValid() {
        return !binding.email.getText().toString().isEmpty()
                && !binding.username.getText().toString().isEmpty()
                && !binding.password.getText().toString().isEmpty()
                && binding.regulationCheckbox.isChecked();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setRootLayoutListener() {
        binding.rootLayout.setOnTouchListener((v, event) -> {
            clearFocusAndHideKeyboard();
            return false;
        });
    }

    private void clearFocusAndHideKeyboard() {
        View focused = getCurrentFocus();
        if (focused instanceof EditText) {
            focused.clearFocus();
            ComponentsUtil.hideKeyboard(this, focused);
        }
    }

    private void setOnFocusChangeListener() {
        final View.OnFocusChangeListener listener = (v, hasFocus) -> {
            if (!hasFocus) {
                ComponentsUtil.hideKeyboard(this, v);
            }
        };

        binding.registerButton.setOnFocusChangeListener(listener);
    }

    private void handleCreateUserWithEmailAndPassword(String email, String password) {
        //TODO walidacja email + hasło
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, this::handleRegistrationTask);
    }

    private void handleRegistrationTask(Task<AuthResult> task) {
        if (task.isSuccessful()) {
            sendVerificationEmail();
            saveUser();
            startActivity(new Intent(CreateUserActivity.this, WeddingChoiceActivity.class));
        } else {
            Toast.makeText(CreateUserActivity.this, "Niepowodzenie podczas rejestracji",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void sendVerificationEmail() {
        if (currentUser != null) {
            currentUser.sendEmailVerification().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(CreateUserActivity.this,
                            "Wysłano e-mail weryfikujący na adres " + currentUser.getEmail(),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CreateUserActivity.this,
                            "Błąd podczas wysyłania e-maila weryfikującego",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void saveUser() {
        if (currentUser != null) {
            User user = User.builder()
                    .username(binding.username.getText().toString())
                    .email(currentUser.getEmail())
                    .build();

            FirebaseUtil.getUserChild(databaseReference, currentUser).setValue(user);
        }
    }

    @Override
    public void onBackPressed() {
    }
}
