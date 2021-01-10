package pl.com.weddingPlanner.validator;

import android.content.Context;
import android.widget.Toast;

public class ValidationUtil {

    public static boolean isValid(String text, boolean showError, Context context, AbstractValidator... validators) {
        for (AbstractValidator validator : validators) {
            if (!validator.isValid(text)) {
                if (showError) {
                    Toast toast = Toast.makeText(context, validator.getErrorMsg(), Toast.LENGTH_LONG);
                    toast.show();
                }
                return false;
            }
        }
        return true;
    }
}
