package javax.finance.stockquotes.web.converter.google;

import com.google.visualization.datasource.datatable.value.DateValue;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Component
public class DateValueConverter implements Converter<Date, DateValue> {

    @Override
    public DateValue convert(final Date date) {

        if (date == null) {
            return null;
        }

        final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(date);

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DateValue(year, month, day);
    }

}
