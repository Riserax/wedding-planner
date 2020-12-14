package pl.com.weddingPlanner.view.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ComponentsUtil {

    public static void hideKeyboard(Activity activity, View view) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager == null) return;

            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
