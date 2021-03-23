package pl.com.weddingPlanner.util;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.com.weddingPlanner.R;

import static java.util.Calendar.APRIL;
import static java.util.Calendar.AUGUST;
import static java.util.Calendar.DECEMBER;
import static java.util.Calendar.FEBRUARY;
import static java.util.Calendar.JANUARY;
import static java.util.Calendar.JULY;
import static java.util.Calendar.JUNE;
import static java.util.Calendar.MARCH;
import static java.util.Calendar.MAY;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.NOVEMBER;
import static java.util.Calendar.OCTOBER;
import static java.util.Calendar.SEPTEMBER;

public class DateUtil {

    public static Date convertStringToDate(String dateString) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("PL"));

        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    public static String getMonth(Calendar calendar, Context context) {
        switch (calendar.get(MONTH)) {
            case JANUARY:
                return context.getResources().getString(R.string.month_january);
            case FEBRUARY:
                return context.getResources().getString(R.string.month_february);
            case MARCH:
                return context.getResources().getString(R.string.month_march);
            case APRIL:
                return context.getResources().getString(R.string.month_april);
            case MAY:
                return context.getResources().getString(R.string.month_may);
            case JUNE:
                return context.getResources().getString(R.string.month_june);
            case JULY:
                return context.getResources().getString(R.string.month_july);
            case AUGUST:
                return context.getResources().getString(R.string.month_august);
            case SEPTEMBER:
                return context.getResources().getString(R.string.month_september);
            case OCTOBER:
                return context.getResources().getString(R.string.month_october);
            case NOVEMBER:
                return context.getResources().getString(R.string.month_november);
            case DECEMBER:
                return context.getResources().getString(R.string.month_december);
            default:
                return String.valueOf(calendar.get(MONTH));
        }
    }

    public static String getNewDateWithHourString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", new Locale("PL"));
        return formatter.format(new Date());
    }

    public static String getDateFromDateTime(String dateTime) {
        return StringUtils.substring(dateTime, 0, dateTime.indexOf(StringUtils.SPACE));
    }
}
