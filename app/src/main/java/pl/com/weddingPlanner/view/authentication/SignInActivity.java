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
import com.google.firebase.auth.FirebaseAuth;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivitySignInBinding;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.NavigationActivity;
import pl.com.weddingPlanner.view.util.ComponentsUtil;

import static pl.com.weddingPlanner.view.util.ComponentsUtil.setButtonEnability;
import static pl.com.weddingPlanner.view.util.LambdaUtil.getOnTextChangedTextWatcher;

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

        setButtonEnability(binding.signInButton, false);
        setListeners();
    }

    private void setListeners() {
        setSignInButtonEnableStatusListener();
        setRootLayoutListener();
        setOnFocusChangeListener();

        binding.signInButton.setOnClickListener(v -> {
            handleSignInWithEmailAndPassword(binding.email.getText().toString(), binding.password.getText().toString());
        });

        binding.registerButton.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, CreateUserActivity.class));
        });
    }

    private void setSignInButtonEnableStatusListener() {
        TextWatcher listener = getOnTextChangedTextWatcher((s, start, before, count) ->
                setButtonEnability(binding.signInButton, areFieldsValid())
        );

        binding.email.addTextChangedListener(listener);
        binding.password.addTextChangedListener(listener);
    }

    private boolean areFieldsValid() {
        return !binding.email.getText().toString().isEmpty()
                && !binding.password.getText().toString().isEmpty();
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

        binding.signInButton.setOnFocusChangeListener(listener);
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
