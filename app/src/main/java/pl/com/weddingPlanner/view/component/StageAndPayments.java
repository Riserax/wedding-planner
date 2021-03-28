package pl.com.weddingPlanner.view.component;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import lombok.Getter;
import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.enums.CollaborationStage;
import pl.com.weddingPlanner.model.info.SubcontractorInfo;

public class StageAndPayments {

    private final Context context;
    private final SubcontractorInfo subcontractorInfo;
    @Getter
    private final LinearLayout layoutContainer;

    public StageAndPayments(Context context, SubcontractorInfo subcontractorInfo) {
        this.context = context;
        this.subcontractorInfo = subcontractorInfo;
        this.layoutContainer = new LinearLayout(context);
        buildStagePayments();
    }

    private void buildStagePayments() {
        setLayoutContainer();
        prepareAndAddStage();
        prepareAndAddPaymentsPercentage();
    }

    private void setLayoutContainer() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutContainer.setLayoutParams(layoutParams);
        layoutContainer.setGravity(Gravity.CENTER);
        layoutContainer.setOrientation(LinearLayout.VERTICAL);
    }

    private void prepareAndAddStage() {
        if (subcontractorInfo.getCollaborationStage() != CollaborationStage.NONE) {
            LinearLayout backgroundLayout = createBackgroundLayout();
            ImageButton imageButton = createImageButton();

            backgroundLayout.addView(imageButton);
            layoutContainer.addView(backgroundLayout);
        }
    }

    private void prepareAndAddPaymentsPercentage() {
        RelativeLayout relativeLayout = createRelativeLayout();
        ProgressBar progressBar = createProgressBar();
        TextView textView = createTextView();

        relativeLayout.addView(progressBar);
        relativeLayout.addView(textView, getTextViewLayoutParams());
        layoutContainer.addView(relativeLayout);
    }

    private LinearLayout createBackgroundLayout() {
        LinearLayout backgroundLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                context.getResources().getDimensionPixelSize(R.dimen.circle_background_small_size), context.getResources().getDimensionPixelSize(R.dimen.circle_background_small_size));

        backgroundLayout.setLayoutParams(layoutParams);
        backgroundLayout.setGravity(Gravity.CENTER);
        backgroundLayout.setOrientation(LinearLayout.VERTICAL);
        backgroundLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_circle_white));

        return backgroundLayout;
    }

    private ImageButton createImageButton() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                context.getResources().getDimensionPixelSize(R.dimen.image_button_size_12), context.getResources().getDimensionPixelSize(R.dimen.image_button_size_12));

        ImageButton imageButton = new ImageButton(context);
        imageButton.setLayoutParams(layoutParams);
        imageButton.setClickable(true);
        imageButton.setFocusable(true);
        imageButton.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
        imageButton.setImageDrawable(ContextCompat.getDrawable(context, subcontractorInfo.getCollaborationStage().getDrawableResourceId()));
        imageButton.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));

        return imageButton;
    }

    private RelativeLayout createRelativeLayout() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 10);

        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setLayoutParams(layoutParams);

        return relativeLayout;
    }

    private ProgressBar createProgressBar() {
        RelativeLayout.LayoutParams progressBarLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, context.getResources().getDimensionPixelSize(R.dimen.progress_bar_height));

        ProgressBar progressBar = new ProgressBar(context, null, 0, android.R.style.Widget_ProgressBar_Horizontal);
        progressBar.setLayoutParams(progressBarLayoutParams);
        progressBar.setMax(100);
        progressBar.setProgress(subcontractorInfo.getPaymentsPercentage());
        progressBar.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimaryLight)));
        progressBar.setProgressBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gray_D5D5D5_80)));

        return progressBar;
    }

    private TextView createTextView() {
        String text = subcontractorInfo.getPaymentsPercentage() + "%";

        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        textView.setTextColor(ContextCompat.getColor(context, R.color.gray_555555));

        return textView;
    }

    private RelativeLayout.LayoutParams getTextViewLayoutParams() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        return layoutParams;
    }
}
