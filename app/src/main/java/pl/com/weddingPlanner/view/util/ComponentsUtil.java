package pl.com.weddingPlanner.view.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.persistence.entity.SubTask;

public class ComponentsUtil {

    public static void hideKeyboard(Activity activity, View view) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager == null) return;

            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static Drawable getIcon(Context context, int iconId) {
        return ContextCompat.getDrawable(context, iconId);
    }

    public static void setButtonEnability(Button button, boolean isEnabled, int buttonEnabledResourceId, int buttonDisabledResourceId) {
        button.setEnabled(isEnabled);

        if (isEnabled) {
            button.setBackground(ContextCompat.getDrawable(button.getContext(), buttonEnabledResourceId));
        } else {
            button.setBackground(ContextCompat.getDrawable(button.getContext(), buttonDisabledResourceId));
        }
    }

    public static void setButtonEnability(Button button, boolean isEnabled) {
        setButtonEnability(button, isEnabled, R.drawable.bg_button, R.drawable.bg_button_disabled);
    }

    public static CheckBox createSubTaskCheckbox(Context context, SubTask subTask, boolean canBeChecked) {
        CheckBox checkBox = new CheckBox(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        int marginStart = Math.round(context.getResources().getDimension(R.dimen.checkbox_margin_start));
        int marginBottom = Math.round(context.getResources().getDimension(R.dimen.checkbox_margin_bottom));
        layoutParams.setMargins(marginStart, 0, 0, marginBottom);

        checkBox.setLayoutParams(layoutParams);
        checkBox.setId(subTask.getId());
        checkBox.setText(subTask.getName());
        checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        checkBox.setTextColor(ContextCompat.getColor(context, R.color.gray_949494));
        checkBox.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimaryDark)));
        checkBox.setChecked(canBeChecked && subTask.getDone() != null && (subTask.getDone().equals("true")));

        return checkBox;
    }
}
