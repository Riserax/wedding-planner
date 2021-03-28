package pl.com.weddingPlanner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.com.weddingPlanner.R;

@Getter
@AllArgsConstructor
public enum GuestType {

    GUEST(R.string.guest_field_guest),
    ACCOMPANY(R.string.guest_field_accompany),
    ;

    private final int nameResId;
}
