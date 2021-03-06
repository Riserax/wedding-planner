package pl.com.weddingPlanner.view.util;

import android.content.Context;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import org.apache.commons.lang3.StringUtils;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityNewGuestBinding;
import pl.com.weddingPlanner.enums.PresenceEnum;
import pl.com.weddingPlanner.persistence.entity.Table;

public class GuestUtil {

    public static void setSelectedInvitationStatus(PresenceEnum selectedPresenceStatus, ActivityNewGuestBinding binding, Context context) {
        switch (selectedPresenceStatus) {
            case INVITATION_SENT:
                GuestUtil.setInvitationSentButtonsSelection(binding, context);
                break;
            case CONFIRMED_PRESENCE:
                GuestUtil.setInvitationAcceptedButtonsSelection(binding, context);
                break;
            case CONFIRMED_ABSENCE:
                GuestUtil.setInvitationRejectedButtonsSelection(binding, context);
                break;
            case AWAITING:
                GuestUtil.setInvitationAwaitingButtonsSelection(binding, context);
        }
    }

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

    public static String getTableDescription(Table table) {
        String tableName = StringUtils.isNotBlank(table.getName()) ? ", " + table.getName() : StringUtils.EMPTY;
        String capacity = ", miejsca: " + table.getCapacity().toString();

        return "Stół nr " + table.getNumber() + tableName + capacity;
    }
}
