package javax.finance.stockquotes.web.converter.google;

import com.google.visualization.datasource.datatable.TableRow;
import com.google.visualization.datasource.datatable.value.DateValue;
import com.google.visualization.datasource.datatable.value.NumberValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.finance.stockquotes.persistence.entity.StockQuote;
import java.util.Date;

@Component
public class TableRowConverter implements Converter<StockQuote, TableRow> {

    private final Converter<Date, DateValue> dateDateValueConverter;
    private final Converter<Number, NumberValue> numberValueConverter;

    @Autowired
    public TableRowConverter(final Converter<Date, DateValue> dateDateValueConverter,
                             final Converter<Number, NumberValue> numberValueConverter) {
        this.dateDateValueConverter = dateDateValueConverter;
        this.numberValueConverter = numberValueConverter;
    }

    @Override
    public TableRow convert(final StockQuote stockQuote) {

        if (stockQuote == null) {
            return null;
        }

        final DateValue dateValue = dateDateValueConverter.convert(stockQuote.getDate());
        final NumberValue numberValue = numberValueConverter.convert(stockQuote.getAdjClose());

        final TableRow tableRow = new TableRow();
        tableRow.addCell(dateValue);
        tableRow.addCell(numberValue);

        return tableRow;
    }

}
