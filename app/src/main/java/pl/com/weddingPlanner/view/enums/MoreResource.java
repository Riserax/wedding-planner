package pl.com.weddingPlanner.view.enums;

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
public enum MoreResource {

    GUESTS(MoreEnum.GUESTS, GuestsActivity.class, R.string.header_title_guests, "ic_groups"),
    SUBCONTRACTORS(MoreEnum.SUBCONTRACTORS, SubcontractorsActivity.class, R.string.header_title_subcontractors, "ic_engineering"),
    SETTINGS(MoreEnum.SETTINGS, SettingsActivity.class, R.string.header_title_settings, "ic_settings"),
    ;

    private final MoreEnum category;
    private final Class targetActivity;
    private final int resourceId;
    private final String iconCode;

    public static MoreResource of(MoreEnum moreEnum) throws EnumValueNotFoundException {
        for (MoreResource value : values()) {
            if (moreEnum.equals(value.category))
                return value;
        }
        throw new EnumValueNotFoundException(moreEnum.name());
    }
}