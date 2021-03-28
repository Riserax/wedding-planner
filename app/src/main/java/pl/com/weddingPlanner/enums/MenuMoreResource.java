package pl.com.weddingPlanner.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.exception.EnumValueNotFoundException;
import pl.com.weddingPlanner.view.guests.GuestsActivity;
import pl.com.weddingPlanner.view.settings.SettingsActivity;
import pl.com.weddingPlanner.view.subcontractors.SubcontractorsActivity;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum MenuMoreResource {

    GUESTS(MenuMore.GUESTS, GuestsActivity.class, R.string.header_title_guests, "ic_groups"),
    SUBCONTRACTORS(MenuMore.SUBCONTRACTORS, SubcontractorsActivity.class, R.string.header_title_subcontractors, "ic_engineering"),
    SETTINGS(MenuMore.SETTINGS, SettingsActivity.class, R.string.header_title_settings, "ic_settings"),
    ;

    private final MenuMore menuMore;
    private final Class<?> targetActivity;
    private final int resourceId;
    private final String iconCode;

    public static MenuMoreResource of(MenuMore menuMore) throws EnumValueNotFoundException {
        for (MenuMoreResource value : values()) {
            if (menuMore.equals(value.menuMore)) {
                return value;
            }
        }
        throw new EnumValueNotFoundException(menuMore.name());
    }
}