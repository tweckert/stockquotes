package javax.finance.stockquotes.web.facade;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import javax.finance.stockquotes.data.entity.Frequency;
import javax.finance.stockquotes.data.entity.StockQuote;
import javax.finance.stockquotes.data.repository.StockQuoteRepository;
import javax.finance.stockquotes.web.dto.OhlcChartDto;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class ChartFacade {

    private final StockQuoteRepository stockQuoteRepository;
    private final Converter<List<StockQuote>, OhlcChartDto> chartDtoConverter;

    @Autowired
    public ChartFacade(final StockQuoteRepository stockQuoteRepository,
                       final Converter<List<StockQuote>, OhlcChartDto> chartDtoConverter) {
        this.stockQuoteRepository = stockQuoteRepository;
        this.chartDtoConverter = chartDtoConverter;
    }

    public OhlcChartDto selectOhlcChartByWkn(final String wkn, final TimeRange timeRange, final Frequency frequency) {
        return selectOhlcChart(true, wkn, timeRange, frequency);
    }

    public OhlcChartDto selectOhlcChartByIsin(final String isin, final TimeRange timeRange, final Frequency frequency) {
        return selectOhlcChart(false, isin, timeRange, frequency);
    }

    protected OhlcChartDto selectOhlcChart(final boolean stockSymbolIsWkn, final String stockSymbol,
                                           final TimeRange timeRange, final Frequency frequency) {

        if (StringUtils.isBlank(stockSymbol) || timeRange == null || frequency == null) {
            return null;
        }

        final LocalDateTime startTime = LocalDateTime.now();
        final Date youngestDate = Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant());
        final Date oldestDate = Date.from(calculateEndTime(startTime, timeRange).atZone(ZoneId.systemDefault()).toInstant());

        final List<StockQuote> stockQuotes =
                stockSymbolIsWkn
                        ? stockQuoteRepository.findByWkn(stockSymbol, frequency, youngestDate, oldestDate)
                        : stockQuoteRepository.findByIsin(stockSymbol, frequency, youngestDate, oldestDate);

        return createChartDto(stockQuotes, frequency, timeRange);
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

    protected OhlcChartDto createChartDto(final List<StockQuote> stockQuotes, final Frequency frequency, final TimeRange timeRange) {

        if (CollectionUtils.isEmpty(stockQuotes) || frequency == null || timeRange == null) {
            return null;
        }

        final OhlcChartDto ohlcChartDto = chartDtoConverter.convert(stockQuotes);
        if (ohlcChartDto != null) {
            ohlcChartDto.setFrequency(frequency);
            ohlcChartDto.setRange(timeRange);
        }

        return ohlcChartDto;
    }

    public Frequency getFrequency(final TimeRange timeRange, final String frequencyName) {
        return TimeRange.WEEK.equals(timeRange)
                ? Frequency.DAILY
                : Frequency.of(frequencyName);
    }

}
