package javax.finance.stockquotes.converter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component(value = "iso8601DateConverter")
public class StringToDateConverter implements Converter<String, Date> {

    private static final Logger LOG = LoggerFactory.getLogger(StringToDateConverter.class);

    private final String dateFormat;

    @Autowired
    public StringToDateConverter(@Value("${iso8601.dateformat}") final String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public Date convert(final String str) {

        if (StringUtils.isBlank(str)) {
            return null;
        }

        try {
            return DateUtils.parseDate(str, dateFormat);
        } catch (final Exception e) {

            if (LOG.isErrorEnabled()) {
                LOG.error(StringUtils.join("Error converting source string '", str,
                        "' with date format '", dateFormat, "' to ", Date.class.getName()), e);
            }

            return null;
        }
    }

}
