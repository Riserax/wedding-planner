package pl.com.weddingPlanner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import pl.com.weddingPlanner.exception.EnumValueNotFoundException;

@Getter
@AllArgsConstructor
public enum GuestGroup {

    PRESENCE_STATUS("Status obecności"),
    AGE_RANGE("Przedział wiekowy"),
    TABLE("Stoły"),
    CATEGORY("Kategorie"),
    ;

    private final String text;

    @SneakyThrows(EnumValueNotFoundException.class)
    public static GuestGroup of(String text) {
        for (GuestGroup group : values()) {
            if (text.equals(group.text)) {
                return group;
            }
        }
        throw new EnumValueNotFoundException(text);
    }
}
