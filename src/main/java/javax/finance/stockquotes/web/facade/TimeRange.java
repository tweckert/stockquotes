package javax.finance.stockquotes.web.facade;

import org.apache.commons.lang3.StringUtils;

public enum TimeRange {

    WEEK,
    MTD,
    MONTH,
    TWO_MONTHS,
    THREE_MONTHS,
    SIX_MONTHS,
    YTD,
    YEAR,
    TWO_YEARS,
    THREE_YEARS,
    FIVE_YEARS,
    MAX;

    public static TimeRange of(final String timeRangeName) {

        if (StringUtils.isBlank(timeRangeName)) {
            return MONTH;
        }

        final String upperCaseTimeRangeName = timeRangeName.toUpperCase();
        for (final TimeRange timeRange : TimeRange.values()) {
            if (timeRange.name().equalsIgnoreCase(upperCaseTimeRangeName)) {
                return timeRange;
            }
        }

        return MONTH;
    }

}
