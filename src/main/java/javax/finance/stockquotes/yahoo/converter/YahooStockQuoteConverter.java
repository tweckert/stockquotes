package javax.finance.stockquotes.yahoo.converter;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.finance.stockquotes.persistence.entity.StockQuote;
import java.math.BigDecimal;
import java.util.Date;

@Component
public class YahooStockQuoteConverter implements Converter<CSVRecord, StockQuote> {

    private static final String NULL_VALUE = "null";
    private static final String DATE = "Date";
    private static final String OPEN = "Open";
    private static final String HIGH = "High";
    private static final String LOW = "Low";
    private static final String CLOSE = "Close";
    private static final String ADJ_CLOSE = "Adj Close";
    private static final String VOLUME = "Volume";

    private final Converter<String, Date> dateConverter;

    @Autowired
    public YahooStockQuoteConverter(@Qualifier("yahooDateConverter") final Converter<String, Date> dateConverter) {
        this.dateConverter = dateConverter;
    }

    @Override
    public StockQuote convert(final CSVRecord csvRecord) {

        if (!accept(csvRecord)) {
            return null;
        }

        final StockQuote stockQuote = new StockQuote();

        convertDate(stockQuote, csvRecord.get(DATE));
        convertOpen(stockQuote, csvRecord.get(OPEN));
        convertClose(stockQuote, csvRecord.get(CLOSE));
        convertHigh(stockQuote, csvRecord.get(HIGH));
        convertLow(stockQuote, csvRecord.get(LOW));
        convertAdjClose(stockQuote, csvRecord.get(ADJ_CLOSE));
        convertVolume(stockQuote, csvRecord.get(VOLUME));

        return stockQuote;
    }

    protected boolean accept(final CSVRecord csvRecord) {

        if (csvRecord == null) {
            return false;
        }

        for (int i = 0; i < csvRecord.size(); i++) {

            final String value = csvRecord.get(i);
            if (StringUtils.isBlank(value) || NULL_VALUE.equalsIgnoreCase(value)) {
                return false;
            }
        }

        return true;
    }

    protected void convertDate(final StockQuote stockQuote, final String rawDate) {
        if (StringUtils.isNotBlank(rawDate)) {
            final Date date = dateConverter.convert(rawDate);
            stockQuote.setDate(date);
        }
    }

    protected BigDecimal convertToBigDecimal(final String str) {
        if (StringUtils.isNotBlank(str)) {
            return new BigDecimal(str);
        }
        return null;
    }

    protected Integer convertToInteger(final String str) {
        if (StringUtils.isNotBlank(str)) {
            return Integer.valueOf(str);
        }
        return null;
    }

    protected void convertOpen(final StockQuote stockQuote, final String rawOpen) {
        final BigDecimal open = convertToBigDecimal(rawOpen);
        if (open != null) {
            stockQuote.setOpen(open);
        }
    }

    protected void convertClose(final StockQuote stockQuote, final String rawClose) {
        final BigDecimal close = convertToBigDecimal(rawClose);
        if (close != null) {
            stockQuote.setClose(close);
        }
    }

    protected void convertHigh(final StockQuote stockQuote, final String rawHigh) {
        final BigDecimal high = convertToBigDecimal(rawHigh);
        if (high != null) {
            stockQuote.setHigh(high);
        }
    }

    protected void convertLow(final StockQuote stockQuote, final String rawLow) {
        final BigDecimal low = convertToBigDecimal(rawLow);
        if (low != null) {
            stockQuote.setLow(low);
        }
    }

    protected void convertVolume(final StockQuote stockQuote, final String rawVolume) {
        final Integer volume = convertToInteger(rawVolume);
        if (volume != null) {
            stockQuote.setVolume(volume);
        }
    }

    protected void convertAdjClose(final StockQuote stockQuote, final String rawAdjClose) {
        final BigDecimal adjClose = convertToBigDecimal(rawAdjClose);
        if (adjClose != null) {
            stockQuote.setAdjClose(adjClose);
        }
    }
}
