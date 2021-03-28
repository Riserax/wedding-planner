package pl.com.weddingPlanner.view.util;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivityNewGuestBinding;
import pl.com.weddingPlanner.enums.GuestGroup;
import pl.com.weddingPlanner.enums.Presence;
import pl.com.weddingPlanner.model.info.GuestInfo;
import pl.com.weddingPlanner.persistence.entity.AgeRange;
import pl.com.weddingPlanner.persistence.entity.Table;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.list.ContentItem;
import pl.com.weddingPlanner.view.list.HeaderItem;
import pl.com.weddingPlanner.view.list.ListItem;

public class GuestUtil {

    public static List<ListItem> getGroupedGuests(String selectedItem, List<GuestInfo> guestInfoList, Context context) {
        switch (GuestGroup.of(selectedItem)) {
            case PRESENCE_STATUS:
                return getGuestsGroupedByPresence(guestInfoList, context);
            case AGE_RANGE:
                return getGuestsGroupedByAgeRange(guestInfoList, context);
            case TABLE:
            case CATEGORY:
            default:
                return Collections.emptyList();
        }
    }

    private static List<ListItem> getGuestsGroupedByPresence(List<GuestInfo> guestInfoList, Context context) {
        List<ListItem> toReturn = new ArrayList<>();

        Map<Presence, List<GuestInfo>> guestsPerPresences = GuestUtil.groupGuestsByPresences(guestInfoList);

        for (Map.Entry<Presence, List<GuestInfo>> guestsPerPresence : guestsPerPresences.entrySet()) {
            String headerText = context.getString(guestsPerPresence.getKey().getTextResourceId());
            HeaderItem headerItem = new HeaderItem(headerText);
            toReturn.add(headerItem);

            for (GuestInfo guest : guestsPerPresence.getValue()) {
                toReturn.add(ContentItem.of(guest));
            }
        }

        return toReturn;
    }

    private static Map<Presence, List<GuestInfo>> groupGuestsByPresences(List<GuestInfo> guestInfoList) {
        Map<Presence, List<GuestInfo>> guestsPerPresences = guestInfoList.stream().collect(Collectors.groupingBy(GuestInfo::getPresence));
        Map<Presence, List<GuestInfo>> orderedGuestsPerPresences = new LinkedHashMap<>();

        for (Presence presence : Presence.values()) {
            for (Map.Entry<Presence, List<GuestInfo>> guestsPerPresence : guestsPerPresences.entrySet()) {
                if (presence.getOrderNumber() == guestsPerPresence.getKey().getOrderNumber()) {
                    orderedGuestsPerPresences.put(guestsPerPresence.getKey(), guestsPerPresence.getValue());
                }
            }
        }

        return orderedGuestsPerPresences;
    }

    private static List<ListItem> getGuestsGroupedByAgeRange(List<GuestInfo> guestInfoList, Context context) {
        List<ListItem> toReturn = new ArrayList<>();

        Map<String, List<GuestInfo>> guestsPerAgeRanges = GuestUtil.groupByAgeRange(guestInfoList, context);

        for (Map.Entry<String, List<GuestInfo>> guestsAgeRange : guestsPerAgeRanges.entrySet()) {
            HeaderItem headerItem = new HeaderItem(guestsAgeRange.getKey());
            toReturn.add(headerItem);

            for (GuestInfo guest : guestsAgeRange.getValue()) {
                toReturn.add(ContentItem.of(guest));
            }
        }

        return toReturn;
    }

    private static Map<String, List<GuestInfo>> groupByAgeRange(List<GuestInfo> guestInfoList, Context context) {
        Map<String, List<GuestInfo>> guestsPerAgeRanges = guestInfoList.stream().collect(Collectors.groupingBy(GuestInfo::getAgeRange));
        Map<String, List<GuestInfo>> orderedGuestsPerAgeRanges = new LinkedHashMap<>();
        List<AgeRange> ageRanges = DAOUtil.getAllAgeRanges(context);
        List<GuestInfo> notSpecifiedAgeGuests = new ArrayList<>();

        for (AgeRange ageRange : ageRanges) {
            for (Map.Entry<String, List<GuestInfo>> guestsPerAgeRange : guestsPerAgeRanges.entrySet()) {
                if (ageRange.getRange().equals(guestsPerAgeRange.getKey())) {
                    orderedGuestsPerAgeRanges.put(guestsPerAgeRange.getKey(), guestsPerAgeRange.getValue());
                }
            }
        }

        for (GuestInfo guest : guestInfoList) {
            if (StringUtils.isBlank(guest.getAgeRange())) {
                notSpecifiedAgeGuests.add(guest);
            }
        }

        orderedGuestsPerAgeRanges.put(context.getString(R.string.field_not_specified), notSpecifiedAgeGuests);

        return orderedGuestsPerAgeRanges;
    }

    public static void setSelectedInvitationStatus(Presence selectedPresenceStatus, ActivityNewGuestBinding binding, Context context) {
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
