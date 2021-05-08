package pl.com.weddingPlanner.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.model.User;
import pl.com.weddingPlanner.util.FirebaseUtil;
import pl.com.weddingPlanner.view.authentication.CreateUserActivity;
import pl.com.weddingPlanner.view.weddings.WeddingChoiceActivity;

import static pl.com.weddingPlanner.view.NavigationActivity.FRAGMENT_TO_LOAD_ID;

public class IntroActivity extends BaseActivity {

    private AnimationDrawable animation;

    public static Intent createIntent(Context context) {
        return new Intent(context, IntroActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        handleCurrentUser();
        setAnimation();
        handlePostDelay();
    }

    private void handleCurrentUser() {
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
        FirebaseUtil.getUser(databaseReference, currentUser).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                User userInfo = task.getResult().getValue(User.class);
                handleAfterIntroIntent(userInfo);
            }
        });
    }

    private void handleAfterIntroIntent(User userInfo) {
        Intent intent;

        if (userInfo != null) {
            intent = getIntentWhenUserNotNull(userInfo);
        } else {
            intent = new Intent(this, CreateUserActivity.class);
        }

        goToActivity(intent);
    }

    private Intent getIntentWhenUserNotNull(User userInfo) {
        Intent intent;

        if (userHasWeddings(userInfo) && userHasCurrentWedding(userInfo)) {
            intent = new Intent(this, NavigationActivity.class);
            intent.putExtra(FRAGMENT_TO_LOAD_ID, R.id.navigation_dashboard);
        } else {
            intent = new Intent(this, WeddingChoiceActivity.class);
        }

        return intent;
    }

    private boolean userHasWeddings(User userInfo) {
        return userInfo.getWeddings() != null && !userInfo.getWeddings().isEmpty();
    }

    private boolean userHasCurrentWedding(User userInfo) {
        return userInfo != null && StringUtils.isNotBlank(userInfo.getCurrentWedding());
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
