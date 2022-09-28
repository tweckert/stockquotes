package javax.finance.stockquotes.web.converter.google;

import com.google.visualization.datasource.datatable.value.NumberValue;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class NumberValueConverter implements Converter<Number, NumberValue> {

    @Override
    public NumberValue convert(final Number number) {

        if (number == null) {
            return null;
        }

        return new NumberValue(number.doubleValue());
    }

}
