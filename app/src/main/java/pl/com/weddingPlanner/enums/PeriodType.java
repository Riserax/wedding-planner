package pl.com.weddingPlanner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.com.weddingPlanner.R;

@Getter
@AllArgsConstructor
public enum PeriodType {

    YEAR(R.string.period_year_1, R.string.period_year_2, R.string.period_year_3),
    MONTH(R.string.period_month_1, R.string.period_month_2, R.string.period_month_3),
    DAY(R.string.period_day_1, R.string.period_day_2, 0),
    HOUR(R.string.period_hour_1, R.string.period_hour_2, R.string.period_hour_3),
    MINUTE(R.string.period_minute_1, R.string.period_minute_2, R.string.period_minute_3),
    SECOND(R.string.period_second_1, R.string.period_second_2, R.string.period_second_3),
    ;

    private final int translation1ResId;
    private final int translation2ResId;
    private final int translation3ResId;
}
