package pl.com.weddingPlanner.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import pl.com.weddingPlanner.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class CustomAlertDialog extends AlertDialog.Builder {

    private final Context context;
    AlertDialog dialog;
    View view;

    private final int buttonFontColor = R.color.colorPrimaryDark;
    private final int dialogBackgroundColor = R.color.white_FFFFFF;

    public CustomAlertDialog(Context context, int layoutResource) {
        super(context, R.style.AlertDialog);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        this.context = context;
        view = layoutInflater.inflate(layoutResource, null);
        setView(view).setCancelable(true);
    }

    public void showDialog() {}

    public void showOneButtonDialog() {
        createAndShowDialog();
        setOneButtonDialogLayout();
    }

    public void showTwoButtonDialog() {
        createAndShowDialog();
        setTwoButtonDialogLayout();
    }

    public void hideDialog() {
        dialog.dismiss();
    }

    private void createAndShowDialog() {
        dialog = this.create();
        dialog.show();
        setWindowSize();
    }

    private void setWindowSize() {
        if (dialog.getWindow() == null) return;

        float dialogWidthPercentage = 0.95f;
        float dialogHeightPercentage = 0.80f;
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * dialogWidthPercentage);
        int maxHeight = (int) (context.getResources().getDisplayMetrics().heightPixels * dialogHeightPercentage);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int height = layoutParams.height;

        if (height > maxHeight) {
            dialog.getWindow().setLayout(width, maxHeight);
        } else {
            dialog.getWindow().setLayout(width, WRAP_CONTENT);
        }
    }

    private void setOneButtonDialogLayout() {
        Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

        customizeParentLayout(neutralButton);

        neutralButton.setTextColor(ContextCompat.getColor(getContext(), buttonFontColor));
    }

    private void setTwoButtonDialogLayout() {
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        customizeParentLayout(positiveButton);

        positiveButton.setTextColor(ContextCompat.getColor(getContext(), buttonFontColor));
        negativeButton.setTextColor(ContextCompat.getColor(getContext(), buttonFontColor));
    }

    private void customizeParentLayout(Button button) {
        LinearLayout parent = (LinearLayout) button.getParent();
        parent.setGravity(Gravity.CENTER_HORIZONTAL);
        parent.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_dialog));
        parent.setBackgroundColor(ContextCompat.getColor(context, dialogBackgroundColor));

        View leftSpacer = parent.getChildAt(1);
        leftSpacer.setVisibility(View.GONE);
    }
}