package pl.com.weddingPlanner.view.list;

import android.content.Context;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.com.weddingPlanner.R;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class HeaderItem extends ListItem {
    private static final String HEADER_ITEM_DATE_PATTERN = "dd.MM.yyyy";

    private String header;

    public static HeaderItem getHeaderItemWithDayOfWeek(Context context, String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();

        DateTimeFormatter headerItemDateTimeFormatter = DateTimeFormatter.ofPattern(HEADER_ITEM_DATE_PATTERN);
        String dateFormatted = localDate.format(headerItemDateTimeFormatter);
        String header;

        switch (dayOfWeek) {
            case MONDAY:
                header = context.getResources().getString(R.string.day_monday) + " " + dateFormatted;
                break;
            case TUESDAY:
                header = context.getResources().getString(R.string.day_tuesday) + " " + dateFormatted;
                break;
            case WEDNESDAY:
                header = context.getResources().getString(R.string.day_wednesday) + " " + dateFormatted;
                break;
            case THURSDAY:
                header = context.getResources().getString(R.string.day_thursday) + " " + dateFormatted;
                break;
            case FRIDAY:
                header = context.getResources().getString(R.string.day_friday) + " " + dateFormatted;
                break;
            case SATURDAY:
                header = context.getResources().getString(R.string.day_saturday) + " " + dateFormatted;
                break;
            case SUNDAY:
                header = context.getResources().getString(R.string.day_sunday) + " " + dateFormatted;
                break;
            default:
                header = dateFormatted;
        }

        return new HeaderItem(header);
    }
}