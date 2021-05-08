package pl.com.weddingPlanner.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.model.User;
import pl.com.weddingPlanner.view.authentication.CreateUserActivity;
import pl.com.weddingPlanner.view.weddings.WeddingChoiceActivity;

public class IntroActivity extends BaseActivity {

    private AnimationDrawable animation;

    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;

    public static Intent createIntent(Context context) {
        return new Intent(context, IntroActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        handleCurrentUser();
        establishFirebaseDatabase();
        setAnimation();
        handlePostDelay();
    }

    private void handleCurrentUser() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            currentUser.reload().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    if (isNotFirebaseNetworkException(task)) {
                        currentUser = null;
                    }
                }
            });
        }
    }

    private boolean isNotFirebaseNetworkException(Task<Void> task) {
        return FirebaseNetworkException.class != Objects.requireNonNull(task.getException()).getClass();
    }

    private void establishFirebaseDatabase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(getString(R.string.firebase_database_url));
        firebaseDatabase.setPersistenceEnabled(true);

        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);
    }

    private void setAnimation() {
        final RelativeLayout layout = findViewById(R.id.intro_layout);

        animation = (AnimationDrawable) layout.getBackground();
        animation.setOneShot(true);
        animation.setEnterFadeDuration(1000);
        animation.setExitFadeDuration(1000);
    }

    private void handlePostDelay() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, CreateUserActivity.class);

            if (currentUser != null) {
                proceedWhenUserLoggedIn();
            } else {
                goToActivity(intent);
            }
        }, 2000);
    }

    private void proceedWhenUserLoggedIn() {
        databaseReference.child("users").child(currentUser.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                User userInfo = task.getResult().getValue(User.class);
                handleAfterIntroIntent(userInfo);
            }
        });
    }

    private void handleAfterIntroIntent(User userInfo) {
        Intent intent;

        if (userInfo != null) {
            if (userHasWeddings(userInfo)) {
                //TODO pokazywać aktualnie użytkowane wesele
                intent = new Intent(this, NavigationActivity.class);
            } else {
                intent = new Intent(this, WeddingChoiceActivity.class);
            }
        } else {
            intent = new Intent(this, CreateUserActivity.class);
        }

        goToActivity(intent);
    }

    private boolean userHasWeddings(User userInfo) {
        return userInfo.getWeddings() != null && !userInfo.getWeddings().isEmpty();
    }

    private void goToActivity(Intent intent) {
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animation != null && !animation.isRunning()) {
            animation.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animation != null && animation.isRunning()) {
            animation.stop();
        }
    }
}
