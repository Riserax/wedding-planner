package pl.com.weddingPlanner.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import java.util.List;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.model.PickedDate;

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
        backIcon.setOnClickListener(view -> finish());
    }

    // finishes Activity (back to previous fragment/activity)
    public void setActivityToolbarContentWithBackIcon(String headerTitle) {
        initToolbar();
        ImageButton backIcon = setComponentsAndReturnBackIcon(headerTitle);
        backIcon.setOnClickListener(view -> finish());
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

    private ImageButton setComponentsAndReturnBackIcon(String headerTitle) {
        ImageButton backIcon = findViewById(R.id.back_button);
        backIcon.setVisibility(View.VISIBLE);

        TextView header = findViewById(R.id.activity_header);
        header.setText(headerTitle);
        header.setVisibility(View.VISIBLE);

        return backIcon;
    }

    public void setFragmentToolbar(@StringRes int headerText) {
        initFragmentToolbar();

        Toolbar toolbar = findViewById(R.id.toolbar_layout);
        toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_toolbar));

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

    public void setFieldText(String text, TextView textView) {
        if (!text.isEmpty()) {
            if (R.id.connected_with_info == textView.getId()) {
                setInfoForChosenGuest(text);
            } else {
                textView.setText(text);
                setTitleVisibility(textView, true);
            }
        } else {
            setDefaultFieldName(textView);
            setTitleVisibility(textView, false);
        }
    }

    private void setInfoForChosenGuest(String text) {
        LinearLayout chosenGuestLayout = findViewById(R.id.connected_with_layout);
        TextView chosenGuestInfo = findViewById(R.id.connected_with_info);

        chosenGuestLayout.setVisibility(View.VISIBLE);
        chosenGuestInfo.setText(getString(R.string.guest_field_accompany_info, text));
    }

    private void setTitleVisibility(TextView view, boolean visible) {
        switch (view.getId()) {
            case R.id.category_name:
                TextView categoryTitle = findViewById(R.id.category_title);
                categoryTitle.setVisibility(visible ? View.VISIBLE : View.GONE);
                break;
            case R.id.bookmarks_name:
                TextView bookmarksTitle = findViewById(R.id.bookmarks_title);
                bookmarksTitle.setVisibility(visible ? View.VISIBLE : View.GONE);
                break;
            case R.id.people_name:
                TextView peopleTitle = findViewById(R.id.people_title);
                peopleTitle.setVisibility(visible ? View.VISIBLE : View.GONE);
                break;
            case R.id.payer_name:
                TextView payerTitle = findViewById(R.id.payer_title);
                payerTitle.setVisibility(visible ? View.VISIBLE : View.GONE);
                break;
            case R.id.age_name:
                TextView ageTitle = findViewById(R.id.age_title);
                ageTitle.setVisibility(visible ? View.VISIBLE : View.GONE);
                break;
            case R.id.table_name:
                TextView tableTitle = findViewById(R.id.table_title);
                tableTitle.setVisibility(visible ? View.VISIBLE : View.GONE);
                break;
            case R.id.connected_subcontractor_name:
                TextView connectedSubcontractorTitle = findViewById(R.id.connected_subcontractor_title);
                connectedSubcontractorTitle.setVisibility(visible ? View.VISIBLE : View.GONE);
                break;
        }
    }

    public void setDefaultFieldName(TextView view) {}

    public void setSelectedPeopleKeys(List<Integer> selectedPeopleKeys) {}

    public void setPickedDate(PickedDate pickedDate) {}

    public void executeQuestionDialog() {}
}
