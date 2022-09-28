package javax.finance.stockquotes.web.facade.impl;

import org.apache.commons.lang3.StringUtils;

import javax.finance.stockquotes.persistence.entity.Frequency;
import javax.finance.stockquotes.persistence.entity.StockQuote;
import javax.finance.stockquotes.persistence.repository.StockQuoteRepository;
import javax.finance.stockquotes.web.facade.TimeRange;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public abstract class AbstractChartFacade {

    private final StockQuoteRepository stockQuoteRepository;

    protected AbstractChartFacade(final StockQuoteRepository stockQuoteRepository) {
        this.stockQuoteRepository = stockQuoteRepository;
    }

    protected List<StockQuote> selectStockQuotes(final boolean stockSymbolIsWkn, final String stockSymbol,
                                                 final TimeRange timeRange, final Frequency frequency) {

        if (StringUtils.isBlank(stockSymbol) || timeRange == null || frequency == null) {
            return null;
        }

        final LocalDateTime startTime = LocalDateTime.now();
        final Date youngestDate = Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant());
        final Date oldestDate = Date.from(calculateEndTime(startTime, timeRange).atZone(ZoneId.systemDefault()).toInstant());

        return stockSymbolIsWkn
                ? stockQuoteRepository.findByWkn(stockSymbol, frequency, oldestDate, youngestDate)
                : stockQuoteRepository.findByIsin(stockSymbol, frequency, oldestDate, youngestDate);
    }

    protected LocalDateTime calculateEndTime(final LocalDateTime startTime, final TimeRange timeRange) {

        switch (timeRange) {
            case WEEK:
                return startTime.minusWeeks(1);
            case MTD:
                return LocalDateTime.of(startTime.getYear(), startTime.getMonth(), 1, 0, 0);
            case MONTH:
                return startTime.minusMonths(1);
            case TWO_MONTHS:
                return startTime.minusMonths(2);
            case THREE_MONTHS:
                return startTime.minusMonths(3);
            case SIX_MONTHS:
                return startTime.minusMonths(6);
            case YTD:
                return LocalDateTime.of(startTime.getYear(), 1, 1, 0, 0);
            case YEAR:
                return startTime.minusYears(1);
            case TWO_YEARS:
                return startTime.minusYears(2);
            case THREE_YEARS:
                return startTime.minusYears(3);
            case FIVE_YEARS:
                return startTime.minusYears(5);
            default:
                return LocalDateTime.of(1970, 1, 1, 0, 0);
        }
    }

}
