package pl.com.weddingPlanner.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import pl.com.weddingPlanner.R;

import static pl.com.weddingPlanner.view.NavigationActivity.FRAGMENT_TO_LOAD_ID;

public class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    protected void initFragmentToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // finishes Activity (back to previous fragment/activity)
    public void setActivityToolbarContentWithBackIcon(@StringRes int resourceId) {
        initToolbar();
        ImageButton backIcon = setComponentsAndReturnBackIcon(resourceId);
        backIcon.setOnClickListener(view -> {
            finish();
        });
    }

    // back from Activity to chosen Fragment
    public void setToolbarContentWithBackIcon(@StringRes int headerTitleId, final Context context, @IdRes final int backDestination) {
        View.OnClickListener backIconOnClickListener = view -> {
            Intent intent = new Intent(context, NavigationActivity.class);
            intent.putExtra(FRAGMENT_TO_LOAD_ID, backDestination);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        };

        setToolbarContentWithBackIcon(headerTitleId, backIconOnClickListener);
    }

    private void setToolbarContentWithBackIcon(@StringRes int headerTitleId, final View.OnClickListener backIconOnClickListener) {
        initToolbar();

        ImageButton backIcon = setComponentsAndReturnBackIcon(headerTitleId);
        backIcon.setOnClickListener(backIconOnClickListener);
    }

    private ImageButton setComponentsAndReturnBackIcon(@StringRes int headerTitleId) {
        ImageButton backIcon = findViewById(R.id.back_button);
        backIcon.setVisibility(View.VISIBLE);

        TextView header = findViewById(R.id.activity_header);
        header.setText(headerTitleId);
        header.setVisibility(View.VISIBLE);

        return backIcon;
    }

    public void setFragmentToolbar(@StringRes int headerText) {
        initFragmentToolbar();

        Toolbar toolbar = findViewById(R.id.toolbar_layout);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

        ImageButton backIcon = findViewById(R.id.back_button);
        backIcon.setVisibility(View.GONE);

        TextView header = findViewById(R.id.activity_header);
        header.setText(headerText);
        header.setVisibility(View.VISIBLE);
    }

    public void setFragmentWithoutToolbar() {
        initFragmentToolbar();

        Toolbar toolbar = findViewById(R.id.toolbar_layout);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_F2F2F2));

        ImageButton backIcon = findViewById(R.id.back_button);
        backIcon.setVisibility(View.GONE);

        TextView header = findViewById(R.id.activity_header);
        header.setVisibility(View.GONE);
    }
}
