package javax.finance.stockquotes.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.finance.stockquotes.constant.StockQuotesApplicationConstants;
import javax.finance.stockquotes.web.dto.OhlcDto;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class OhlcCsvConverter implements Converter<OhlcDto, List<String>> {

    public static final String[] CSV_HEADER = {
            "date", "open", "high", "low", "adj_close", "volume"
    };

    @Override
    public List<String> convert(final OhlcDto ohlcDto) {

        if (ohlcDto == null) {
            return null;
        }

        final SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(StockQuotesApplicationConstants.ISO8601_DATE_FORMAT);

        final List<String> csvValues = new ArrayList<>();
        csvValues.add(simpleDateFormat.format(ohlcDto.getDate()));
        csvValues.add(ohlcDto.getOpen().toString());
        csvValues.add(ohlcDto.getHigh().toString());
        csvValues.add(ohlcDto.getLow().toString());
        csvValues.add(ohlcDto.getAdjcClose().toString());
        csvValues.add(ohlcDto.getVolume().toString());

        return csvValues;
    }

}
