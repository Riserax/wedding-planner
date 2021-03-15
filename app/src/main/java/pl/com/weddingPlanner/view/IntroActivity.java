package pl.com.weddingPlanner.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;

import pl.com.weddingPlanner.R;

public class IntroActivity extends BaseActivity {

    private AnimationDrawable animation;

    public static Intent createIntent(Context context) {
        return new Intent(context, IntroActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        setAnimation();
        handlePostDelay();
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
            Intent intent = new Intent(this, NavigationActivity.class);
            startActivity(intent);
            finish();
        }, 2500);
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
