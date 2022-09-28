package javax.finance.stockquotes.web.facade.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import javax.finance.stockquotes.persistence.entity.Frequency;
import javax.finance.stockquotes.persistence.entity.StockQuote;
import javax.finance.stockquotes.persistence.repository.StockQuoteRepository;
import javax.finance.stockquotes.web.dto.OhlcChartDto;
import javax.finance.stockquotes.web.facade.ChartFacade;
import javax.finance.stockquotes.web.facade.TimeRange;
import java.util.List;

@Service
public class OhlcChartFacade extends AbstractChartFacade implements ChartFacade<OhlcChartDto> {

    private final Converter<List<StockQuote>, OhlcChartDto> chartDtoConverter;

    @Autowired
    public OhlcChartFacade(final StockQuoteRepository stockQuoteRepository,
                           final Converter<List<StockQuote>, OhlcChartDto> chartDtoConverter) {
        super(stockQuoteRepository);
        this.chartDtoConverter = chartDtoConverter;
    }

    @Override
    public OhlcChartDto getByWkn(final String wkn, final TimeRange timeRange, final Frequency frequency) {
        return getBySymbol(true, wkn, timeRange, frequency);
    }

    @Override
    public OhlcChartDto getByIsin(final String isin, final TimeRange timeRange, final Frequency frequency) {
        return getBySymbol(false, isin, timeRange, frequency);
    }

    protected OhlcChartDto getBySymbol(final boolean stockSymbolIsWkn, final String stockSymbol,
                                       final TimeRange timeRange, final Frequency frequency) {

        final List<StockQuote> stockQuotes = selectStockQuotes(stockSymbolIsWkn, stockSymbol, timeRange, frequency);
        final OhlcChartDto ohlcChartDto = createChartDto(stockQuotes, frequency, timeRange);

        return ohlcChartDto;
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

}
