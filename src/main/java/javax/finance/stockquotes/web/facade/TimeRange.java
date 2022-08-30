package javax.finance.stockquotes.web.facade;

import org.apache.commons.lang3.StringUtils;

public enum TimeRange {

    ONE_WEEK,
    MONTH_TO_DATE,
    ONE_MONTH,
    TWO_MONTHS,
    THREE_MONTHS,
    SIX_MONTHS,
    YEAR_TO_DATE,
    ONE_YEAR,
    TWO_YEARS,
    THREE_YEARS,
    FIVE_YEARS,
    MAXIMUM;

    public static TimeRange of(final String timeRangeName) {

        if (StringUtils.isBlank(timeRangeName)) {
            return ONE_MONTH;
        }

        final String upperCaseTimeRangeName = timeRangeName.toUpperCase();
        for (final TimeRange timeRange : TimeRange.values()) {
            if (timeRange.name().equalsIgnoreCase(upperCaseTimeRangeName)) {
                return timeRange;
            }
        }

        return ONE_MONTH;
    }

}
