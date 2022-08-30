package javax.finance.stockquotes.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.finance.stockquotes.data.entity.StockQuote;
import javax.finance.stockquotes.web.dto.QuoteDto;
import javax.finance.stockquotes.constant.StockQuotesConstants;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Component
public class QuoteDtConverter implements Converter<StockQuote, QuoteDto> {

    @Override
    public QuoteDto convert(final StockQuote stockQuote) {

        if (stockQuote == null) {
            return null;
        }

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(StockQuotesConstants.ISO8601_DATE_FORMAT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        final long timestampSecs = stockQuote.getDate().getTime() / 1000;
        final String iso8601Date = simpleDateFormat.format(stockQuote.getDate());
        final BigDecimal adjClose = stockQuote.getAdjClose();
        final BigDecimal volume = stockQuote.getVolume();

        return new QuoteDto(timestampSecs, iso8601Date, adjClose, volume);
    }

}
