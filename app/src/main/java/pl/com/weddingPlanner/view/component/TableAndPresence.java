package pl.com.weddingPlanner.view.component;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import lombok.Getter;
import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.model.info.GuestInfo;

public class TableAndPresence {

    private final Context context;
    private final GuestInfo guestInfo;
    @Getter
    private final LinearLayout layoutContainer;

    public TableAndPresence(Context context, GuestInfo guestInfo) {
        this.context = context;
        this.guestInfo = guestInfo;
        this.layoutContainer = new LinearLayout(context);
        buildTablePresence();
    }

    private void buildTablePresence() {
        setLayoutContainer();
        prepareAndAddTable();
        prepareAndAddPresence();
    }

    private void setLayoutContainer() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutContainer.setLayoutParams(layoutParams);
        layoutContainer.setHorizontalGravity(Gravity.CENTER);
        layoutContainer.setOrientation(LinearLayout.HORIZONTAL);
    }

    private void prepareAndAddTable() {
        if (guestInfo.getTableNumber() != 0) {
            LinearLayout backgroundLayout = createBackgroundLayout();
            LinearLayout primaryCircleLayout = createPrimaryCircleLayout();
            TextView textView = createTextView();

            backgroundLayout.addView(primaryCircleLayout);
            primaryCircleLayout.addView(textView);
            layoutContainer.addView(backgroundLayout);
        }
    }

    private void prepareAndAddPresence() {
        if (guestInfo.getPresence() != null) {
            LinearLayout backgroundLayout = createBackgroundLayout();
            ImageButton imageButton = createImageButton();

            backgroundLayout.addView(imageButton);
            layoutContainer.addView(backgroundLayout);
        }
    }

    private LinearLayout createBackgroundLayout() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.circle_background_size), context.getResources().getDimensionPixelSize(R.dimen.circle_background_size));

        LinearLayout backgroundLayout = new LinearLayout(context);
        backgroundLayout.setLayoutParams(layoutParams);
        backgroundLayout.setGravity(Gravity.CENTER);
        backgroundLayout.setOrientation(LinearLayout.VERTICAL);
        backgroundLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_circle_white));

        return backgroundLayout;
    }

    private LinearLayout createPrimaryCircleLayout() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.circle_primary_size), context.getResources().getDimensionPixelSize(R.dimen.circle_primary_size));

        LinearLayout backgroundLayout = new LinearLayout(context);
        backgroundLayout.setLayoutParams(layoutParams);
        backgroundLayout.setPadding(0, 0, 0, 2);
        backgroundLayout.setGravity(Gravity.CENTER);
        backgroundLayout.setOrientation(LinearLayout.VERTICAL);
        backgroundLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_circle_primary));

        return backgroundLayout;
    }

    private TextView createTextView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setText(String.valueOf(guestInfo.getTableNumber()));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        textView.setTextColor(ContextCompat.getColor(context, R.color.white_FFFFFF));
        textView.setTypeface(Typeface.DEFAULT_BOLD);

        return textView;
    }

    private ImageButton createImageButton() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.image_button_size_17), context.getResources().getDimensionPixelSize(R.dimen.image_button_size_17));

        ImageButton imageButton = new ImageButton(context);
        imageButton.setLayoutParams(layoutParams);
        imageButton.setClickable(true);
        imageButton.setFocusable(true);
        imageButton.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
        imageButton.setImageDrawable(ContextCompat.getDrawable(context, guestInfo.getPresence().getDrawableResourceId()));
        imageButton.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));

        return imageButton;
    }
}
