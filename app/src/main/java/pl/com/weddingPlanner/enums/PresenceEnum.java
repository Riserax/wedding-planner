package pl.com.weddingPlanner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.com.weddingPlanner.R;

@Getter
@AllArgsConstructor
public enum PresenceEnum {

    INVITATION_SENT(1, R.string.guest_field_invitation_sent_short, R.string.guest_field_invitation_sent, R.drawable.ic_email),
    CONFIRMED_PRESENCE(2, R.string.guest_field_invitation_accepted_short, R.string.guest_field_invitation_accepted, R.drawable.ic_check_circle),
    CONFIRMED_ABSENCE(3, R.string.guest_field_invitation_rejected_short, R.string.guest_field_invitation_rejected, R.drawable.ic_cancel_circle),
    AWAITING(4, R.string.guest_field_invitation_awaiting_short, R.string.guest_field_invitation_awaiting, R.drawable.ic_help_circle),
    NONE(5, R.string.guest_field_invitation_none, R.string.guest_field_invitation_none, 0),
    ;

    private final int orderNumber;
    private final int shortTextResourceId;
    private final int textResourceId;
    private final int drawableResourceId;
}
