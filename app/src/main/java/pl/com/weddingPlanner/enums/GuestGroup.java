package pl.com.weddingPlanner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GuestGroup {

    PRESENCE_STATUS("Status obecności"),
    AGE_RANGE("Przedział wiekowy"),
    TABLE("Stoły"),
    CATEGORY("Kategorie"),
    ;

    private final String text;

    public static GuestGroup of(String text) {
        for (GuestGroup group : GuestGroup.values()) {
            if (text.equals(group.text)) {
                return group;
            }
        }
        throw new IllegalArgumentException(String.format("No GuestGroup enum of value '%1$s' was found", text));
    }
}
