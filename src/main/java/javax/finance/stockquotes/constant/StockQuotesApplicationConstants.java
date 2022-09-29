package javax.finance.stockquotes.constant;

import java.util.Locale;
import java.util.TimeZone;

public class StockQuotesApplicationConstants {

    public static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_TIMEZONE_ID = "UTC";
    public static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone(DEFAULT_TIMEZONE_ID);
    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    private StockQuotesApplicationConstants() {
        // intentionally left blank
    }

}
