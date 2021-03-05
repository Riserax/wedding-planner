package pl.com.weddingPlanner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.com.weddingPlanner.R;

@Getter
@AllArgsConstructor
public enum PresenceEnum {

    NONE(0),
    INVITATION_SENT(R.string.guest_field_invitation_sent),
    CONFIRMED_PRESENCE(R.string.guest_field_invitation_accepted),
    CONFIRMED_ABSENCE(R.string.guest_field_invitation_rejected),
    AWAITING(R.string.guest_field_invitation_awaiting),
    ;

    private final int nameResId;
}
