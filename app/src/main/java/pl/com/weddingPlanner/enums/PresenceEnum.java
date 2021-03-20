package pl.com.weddingPlanner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.com.weddingPlanner.R;

@Getter
@AllArgsConstructor
public enum PresenceEnum {

    NONE(0, 0),
    INVITATION_SENT(R.string.guest_field_invitation_sent, R.drawable.ic_email),
    CONFIRMED_PRESENCE(R.string.guest_field_invitation_accepted, R.drawable.ic_check_circle),
    CONFIRMED_ABSENCE(R.string.guest_field_invitation_rejected, R.drawable.ic_cancel_circle),
    AWAITING(R.string.guest_field_invitation_awaiting, R.drawable.ic_help_circle),
    ;

    private final int textResourceId;
    private final int drawableResourceId;
}
