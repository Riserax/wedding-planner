package pl.com.weddingPlanner.view.util;

import android.content.Context;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import pl.com.weddingPlanner.R;

public class ButtonsUtil {

    public static void setButtonSelection(Button button, Context context, boolean selected) {
        if (selected) {
            button.setCompoundDrawableTintList(ContextCompat.getColorStateList(context, R.color.white_FFFFFF));
            button.setTextColor(ContextCompat.getColor(context, R.color.white_FFFFFF));
            button.setBackground(ContextCompat.getDrawable(context, R.drawable.button_shape_light));
        } else {
            button.setCompoundDrawableTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryLight));
            button.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
            button.setBackground(ContextCompat.getDrawable(context, R.drawable.button_shape_border_only));
        }

        button.setPadding(0, context.getResources().getDimensionPixelSize(R.dimen.button_padding_top),
                0, context.getResources().getDimensionPixelSize(R.dimen.button_padding_bottom));
    }
}
