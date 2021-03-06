package pl.com.weddingPlanner.view.util;

import android.widget.TextView;

import pl.com.weddingPlanner.enums.PeriodType;

public class DashboardUtil {

    public static void setValueAndTitleForPeriod(TextView valueTextView, TextView titleTextView, int value, PeriodType periodType) {
        String preparedValue = prepareValue(value);
        valueTextView.setText(preparedValue);
        setTitleForPeriod(titleTextView, periodType, value);
    }

    private static String prepareValue(int value) {
        if (value < 10) {
            return "0" + value;
        }
        return String.valueOf(value);
    }

    private static void setTitleForPeriod(TextView titleTextView, PeriodType periodType, int value) {
        switch (periodType) {
            case YEAR:
            case MONTH:
                if (value > 4) {
                    titleTextView.setText(periodType.getTranslation3ResId());
                } else if (value > 1) {
                    titleTextView.setText(periodType.getTranslation2ResId());
                } else {
                    titleTextView.setText(periodType.getTranslation1ResId());
                }
                break;
            case DAY:
                if (value > 1) {
                    titleTextView.setText(periodType.getTranslation2ResId());
                } else {
                    titleTextView.setText(periodType.getTranslation1ResId());
                }
                break;
            case HOUR:
                if (value == 1) {
                    titleTextView.setText(periodType.getTranslation1ResId());
                } else if (value == 2 || value == 3 || value == 4 || value == 22 || value == 23) {
                    titleTextView.setText(periodType.getTranslation2ResId());
                } else {
                    titleTextView.setText(periodType.getTranslation3ResId());
                }
                break;
            case MINUTE:
            case SECOND:
                if (value == 1) {
                    titleTextView.setText(periodType.getTranslation1ResId());
                } else if (value == 2 || value == 3 || value == 4
                        || value == 22 || value == 23 || value == 24
                        || value == 32 || value == 33 || value == 34
                        || value == 42 || value == 43 || value == 44
                        || value == 52 || value == 53 || value == 54) {
                    titleTextView.setText(periodType.getTranslation2ResId());
                } else {
                    titleTextView.setText(periodType.getTranslation3ResId());
                }
        }
    }
}
