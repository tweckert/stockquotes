package javax.finance.stockquotes.web.facade.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import javax.finance.stockquotes.persistence.entity.Frequency;
import javax.finance.stockquotes.persistence.entity.StockQuote;
import javax.finance.stockquotes.persistence.repository.StockQuoteRepository;
import javax.finance.stockquotes.service.StockService;
import javax.finance.stockquotes.web.dto.OhlcChartDto;
import javax.finance.stockquotes.web.facade.ChartFacade;
import javax.finance.stockquotes.web.facade.TimeRange;
import java.util.List;

@Service
public class OhlcChartFacade extends AbstractChartFacade implements ChartFacade<OhlcChartDto> {

    private final Converter<List<StockQuote>, OhlcChartDto> chartDtoConverter;

    @Autowired
    public OhlcChartFacade(final StockQuoteRepository stockQuoteRepository,
                           final StockService stockService,
                           final Converter<List<StockQuote>, OhlcChartDto> chartDtoConverter) {
        super(stockQuoteRepository, stockService);
        this.chartDtoConverter = chartDtoConverter;
    }

    @Override
    public OhlcChartDto selectStockQuoteData(final String stockSymbol, final TimeRange timeRange, final Frequency frequency) {

        final List<StockQuote> stockQuotes = selectStockQuotes(stockSymbol, timeRange, frequency);
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
