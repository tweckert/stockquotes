package javax.finance.stockquotes.web.facade;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import javax.finance.stockquotes.data.entity.StockQuote;
import javax.finance.stockquotes.data.repository.StockQuoteRepository;
import javax.finance.stockquotes.web.dto.ChartDto;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@Service
public class ChartFacade {

    private final StockQuoteRepository stockQuoteRepository;
    private final Converter<List<StockQuote>, ChartDto> chartDtoConverter;

    @Autowired
    public ChartFacade(final StockQuoteRepository stockQuoteRepository,
                       final Converter<List<StockQuote>, ChartDto> chartDtoConverter) {
        this.stockQuoteRepository = stockQuoteRepository;
        this.chartDtoConverter = chartDtoConverter;
    }

    public ChartDto selectChartByWkn(final String wkn, final TimeRange timeRange) {

        if (StringUtils.isBlank(wkn)) {
            return null;
        }

        final LocalDateTime startTime = LocalDateTime.now();
        final Date startDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        final Date endDate = Date.from(calculateEndTime(startTime, timeRange).atZone(ZoneId.systemDefault()).toInstant());

        final List<StockQuote> stockQuotes = stockQuoteRepository.findByWkn(wkn, startDate, endDate);
        return createChartDto(stockQuotes);
    }

    public ChartDto selectChartByIsin(final String isin, final TimeRange timeRange) {

        if (StringUtils.isBlank(isin)) {
            return null;
        }

        final LocalDateTime startTime = LocalDateTime.now();
        final Date startDate = Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant());
        final Date endDate = Date.from(calculateEndTime(startTime, timeRange).atZone(ZoneId.systemDefault()).toInstant());

        final List<StockQuote> stockQuotes = stockQuoteRepository.findByIsin(isin, startDate, endDate);

        return createChartDto(stockQuotes);
    }

    protected LocalDateTime calculateEndTime(final LocalDateTime startTime, final TimeRange timeRange) {

        switch (timeRange) {
            case ONE_WEEK:
                return startTime.minusWeeks(1);
            case MONTH_TO_DATE:
                return LocalDateTime.of(startTime.getYear(), startTime.getMonth(), 1, 0, 0);
            case ONE_MONTH:
                return startTime.minusMonths(1);
            case TWO_MONTHS:
                return startTime.minusMonths(2);
            case THREE_MONTHS:
                return startTime.minusMonths(3);
            case SIX_MONTHS:
                return startTime.minusMonths(6);
            case YEAR_TO_DATE:
                return LocalDateTime.of(startTime.getYear(), 1, 1, 0, 0);
            case ONE_YEAR:
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

    protected ChartDto createChartDto(final List<StockQuote> stockQuotes) {
        return CollectionUtils.isNotEmpty(stockQuotes)
                ? chartDtoConverter.convert(stockQuotes)
                : null;
    }

}
