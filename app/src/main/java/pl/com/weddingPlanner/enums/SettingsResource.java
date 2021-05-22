package pl.com.weddingPlanner.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.exception.EnumValueNotFoundException;
import pl.com.weddingPlanner.view.settings.HelpersActivity;
import pl.com.weddingPlanner.view.settings.SettingsActivity;
import pl.com.weddingPlanner.view.weddings.MyWeddingsActivity;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum SettingsResource {

    PROFILE(Settings.PROFILE, SettingsActivity.class, R.string.header_title_profile, "ic_person_manage"),
    WEDDINGS(Settings.WEDDINGS, MyWeddingsActivity.class, R.string.header_title_weddings, "ic_heart"),
    HELPERS(Settings.HELPERS, HelpersActivity.class, R.string.header_title_helpers, "ic_people"),
    ABOUT_APP(Settings.ABOUT_APP, SettingsActivity.class, R.string.header_title_about_app, "ic_info"),
    ;

    private final Settings settings;
    private final Class<?> targetActivity;
    private final int resourceId;
    private final String iconCode;

    public static SettingsResource of(Settings settings) throws EnumValueNotFoundException {
        for (SettingsResource value : values()) {
            if (settings.equals(value.settings)) {
                return value;
            }
        }
        throw new EnumValueNotFoundException(settings.name());
    }
}