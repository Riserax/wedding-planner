package pl.com.weddingPlanner.view.authentication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.lang3.StringUtils;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivitySignInBinding;
import pl.com.weddingPlanner.util.FirebaseUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.util.ComponentsUtil;
import pl.com.weddingPlanner.view.weddings.WeddingChoiceActivity;

import static pl.com.weddingPlanner.view.util.ComponentsUtil.setButtonEnablity;
import static pl.com.weddingPlanner.view.util.LambdaUtil.getOnTextChangedTextWatcher;

public class SignInActivity extends BaseActivity {

    private ActivitySignInBinding binding;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);

        setButtonEnablity(binding.signInButton, false);
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
                setButtonEnablity(binding.signInButton, areFieldsValid())
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
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            clearCurrentWeddingAndStartActivity();
        } else {
            Toast.makeText(SignInActivity.this, "Niepowodzenie podczas logowania",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void clearCurrentWeddingAndStartActivity() {
        FirebaseUtil.getUser(databaseReference, currentUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUtil.getUserChild(databaseReference, currentUser).child("currentWedding").setValue(StringUtils.EMPTY);
                startActivity(new Intent(SignInActivity.this, WeddingChoiceActivity.class));
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return onKeyDown() || super.onKeyDown(keyCode, event);

        return super.onKeyDown(keyCode, event);
    }

    private boolean onKeyDown() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        return true;
    }
}
