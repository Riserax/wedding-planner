package pl.com.weddingPlanner.view.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import pl.com.weddingPlanner.R;

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
}
