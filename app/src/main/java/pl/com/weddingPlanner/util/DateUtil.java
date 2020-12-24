package pl.com.weddingPlanner.util;

import android.content.Context;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.com.weddingPlanner.R;

public class DateUtil {

    public static String convertDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static Date convertStringToDate(String dateString) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    public static String getDateMonth(String date, Context context) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        Month month = localDate.getMonth();

        switch (month) {
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
                return month.toString();
        }
    }
}
