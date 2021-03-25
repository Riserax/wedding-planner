package pl.com.weddingPlanner.view.util;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pl.com.weddingPlanner.databinding.ActivityNewGuestBinding;
import pl.com.weddingPlanner.enums.PresenceEnum;
import pl.com.weddingPlanner.model.info.GuestInfo;
import pl.com.weddingPlanner.persistence.entity.Table;

public class GuestUtil {

    public static Map<PresenceEnum, List<GuestInfo>> groupGuestsPerPresences(List<GuestInfo> guestInfoList) {
        Map<PresenceEnum, List<GuestInfo>> guestsPerPresences = guestInfoList.stream().collect(Collectors.groupingBy(GuestInfo::getPresence));
        Map<PresenceEnum, List<GuestInfo>> orderedGuestsPerPresences = new HashMap<>();

        for (int i = 1; i <= 5; i++) {
            for (Map.Entry<PresenceEnum, List<GuestInfo>> guestsPerPresence : guestsPerPresences.entrySet()) {
                if (i == guestsPerPresence.getKey().getOrderNumber()) {
                    orderedGuestsPerPresences.put(guestsPerPresence.getKey(), guestsPerPresence.getValue());
                }
            }
        }

        return orderedGuestsPerPresences;
    }

    public static void setSelectedInvitationStatus(PresenceEnum selectedPresenceStatus, ActivityNewGuestBinding binding, Context context) {
        switch (selectedPresenceStatus) {
            case INVITATION_SENT:
                setInvitationSentButtonsSelection(binding, context);
                break;
            case CONFIRMED_PRESENCE:
                setInvitationAcceptedButtonsSelection(binding, context);
                break;
            case CONFIRMED_ABSENCE:
                setInvitationRejectedButtonsSelection(binding, context);
                break;
            case AWAITING:
                setInvitationAwaitingButtonsSelection(binding, context);
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
