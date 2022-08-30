package javax.finance.stockquotes.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.finance.stockquotes.data.entity.StockQuote;
import javax.finance.stockquotes.web.dto.QuoteDto;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Component
public class QuoteDtConverter implements Converter<StockQuote, QuoteDto> {

    @Override
    public QuoteDto convert(final StockQuote stockQuote) {

        if (stockQuote == null) {
            return null;
        }

        final Long timestampSecs = stockQuote.getDate().getTime() / 1000;
        final Date date = stockQuote.getDate();
        final BigDecimal adjClose = stockQuote.getAdjClose().setScale(2, RoundingMode.HALF_UP);
        final Integer volume = stockQuote.getVolume();

        return new QuoteDto(timestampSecs, date, adjClose, volume);
    }

}
