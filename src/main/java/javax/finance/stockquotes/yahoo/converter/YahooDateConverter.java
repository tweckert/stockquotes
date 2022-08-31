package javax.finance.stockquotes.yahoo.converter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.finance.stockquotes.constant.StockQuotesApplicationConstants;
import java.util.Date;

@Component
public class YahooDateConverter implements Converter<String, Date> {

    private static final Logger LOG = LoggerFactory.getLogger(YahooDateConverter.class);

    @Override
    public Date convert(final String str) {

        try {
            return StringUtils.isNotBlank(str)
                    ? DateUtils.parseDate(str, StockQuotesApplicationConstants.ISO8601_DATE_FORMAT)
                    : null;
        } catch (final Exception e) {

            if (LOG.isErrorEnabled()) {
                LOG.error(StringUtils.join("Error converting source string '", str,
                        "' with date format '", StockQuotesApplicationConstants.ISO8601_DATE_FORMAT, "' to ", Date.class.getName()), e);
            }

            return null;
        }
    }

}
