package pl.com.weddingPlanner.view.util;

import android.content.Context;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityNewGuestBinding;

public class GuestUtil {

    public static void setInvitationSentButtonsSelection(ActivityNewGuestBinding binding, Context context) {
        GuestUtil.setButtonSelection(binding.sentButton, context, true);
        GuestUtil.setButtonSelection(binding.acceptedButton, context, false);
        GuestUtil.setButtonSelection(binding.rejectedButton, context, false);
        GuestUtil.setButtonSelection(binding.awaitingButton, context, false);
    }

    public static void setInvitationAcceptedButtonsSelection(ActivityNewGuestBinding binding, Context context) {
        GuestUtil.setButtonSelection(binding.sentButton, context, false);
        GuestUtil.setButtonSelection(binding.acceptedButton, context, true);
        GuestUtil.setButtonSelection(binding.rejectedButton, context, false);
        GuestUtil.setButtonSelection(binding.awaitingButton, context, false);
    }

    public static void setInvitationRejectedButtonsSelection(ActivityNewGuestBinding binding, Context context) {
        GuestUtil.setButtonSelection(binding.sentButton, context, false);
        GuestUtil.setButtonSelection(binding.acceptedButton, context, false);
        GuestUtil.setButtonSelection(binding.rejectedButton, context, true);
        GuestUtil.setButtonSelection(binding.awaitingButton, context, false);
    }

    public static void setInvitationAwaitingButtonsSelection(ActivityNewGuestBinding binding, Context context) {
        GuestUtil.setButtonSelection(binding.sentButton, context, false);
        GuestUtil.setButtonSelection(binding.acceptedButton, context, false);
        GuestUtil.setButtonSelection(binding.rejectedButton, context, false);
        GuestUtil.setButtonSelection(binding.awaitingButton, context, true);
    }

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
