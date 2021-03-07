package pl.com.weddingPlanner.view.util;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;

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
        ButtonsUtil.setButtonSelection(binding.sentButton, context, true);
        ButtonsUtil.setButtonSelection(binding.acceptedButton, context, false);
        ButtonsUtil.setButtonSelection(binding.rejectedButton, context, false);
        ButtonsUtil.setButtonSelection(binding.awaitingButton, context, false);
    }

    public static void setInvitationAcceptedButtonsSelection(ActivityNewGuestBinding binding, Context context) {
        ButtonsUtil.setButtonSelection(binding.sentButton, context, false);
        ButtonsUtil.setButtonSelection(binding.acceptedButton, context, true);
        ButtonsUtil.setButtonSelection(binding.rejectedButton, context, false);
        ButtonsUtil.setButtonSelection(binding.awaitingButton, context, false);
    }

    public static void setInvitationRejectedButtonsSelection(ActivityNewGuestBinding binding, Context context) {
        ButtonsUtil.setButtonSelection(binding.sentButton, context, false);
        ButtonsUtil.setButtonSelection(binding.acceptedButton, context, false);
        ButtonsUtil.setButtonSelection(binding.rejectedButton, context, true);
        ButtonsUtil.setButtonSelection(binding.awaitingButton, context, false);
    }

    public static void setInvitationAwaitingButtonsSelection(ActivityNewGuestBinding binding, Context context) {
        ButtonsUtil.setButtonSelection(binding.sentButton, context, false);
        ButtonsUtil.setButtonSelection(binding.acceptedButton, context, false);
        ButtonsUtil.setButtonSelection(binding.rejectedButton, context, false);
        ButtonsUtil.setButtonSelection(binding.awaitingButton, context, true);
    }

    public static String getTableDescription(Table table) {
        String tableName = StringUtils.isNotBlank(table.getName()) ? ", " + table.getName() : StringUtils.EMPTY;
        String capacity = ", miejsca: " + table.getCapacity().toString();

        return "Stół nr " + table.getNumber() + tableName + capacity;
    }
}
