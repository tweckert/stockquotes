package javax.finance.stockquotes.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.finance.stockquotes.data.entity.StockQuote;
import javax.finance.stockquotes.web.dto.OhlcDto;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Component
public class OhlcDtoConverter implements Converter<StockQuote, OhlcDto> {

    @Override
    public OhlcDto convert(final StockQuote stockQuote) {

        if (stockQuote == null) {
            return null;
        }

        final Long timestampSecs = stockQuote.getDate().getTime() / 1000;
        final Date date = stockQuote.getDate();
        final BigDecimal open = stockQuote.getOpen().setScale(2, RoundingMode.HALF_UP);
        final BigDecimal high = stockQuote.getHigh().setScale(2, RoundingMode.HALF_UP);
        final BigDecimal low = stockQuote.getLow().setScale(2, RoundingMode.HALF_UP);
        final BigDecimal adjClose = stockQuote.getAdjClose().setScale(2, RoundingMode.HALF_UP);
        final Integer volume = stockQuote.getVolume();

        return new OhlcDto(timestampSecs, date, open, high, low, adjClose, volume);
    }

}
