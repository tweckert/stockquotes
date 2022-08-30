package javax.finance.stockquotes.web.converter;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.finance.stockquotes.data.entity.Stock;
import javax.finance.stockquotes.data.entity.StockQuote;
import javax.finance.stockquotes.web.dto.ChartDto;
import javax.finance.stockquotes.web.dto.QuoteDto;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class ChartDtoConverter implements Converter<List<StockQuote>, ChartDto> {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100l);

    private final Converter<StockQuote, QuoteDto> quoteConverter;

    @Autowired
    public ChartDtoConverter(final Converter<StockQuote, QuoteDto> quoteConverter) {
        this.quoteConverter = quoteConverter;
    }

    @Override
    public ChartDto convert(final List<StockQuote> stockQuotes) {

        if (CollectionUtils.isEmpty(stockQuotes)) {
            return null;
        }

        final List<QuoteDto> quoteDtoList = new ArrayList<>();
        for (final StockQuote stockQuote : stockQuotes) {

            final QuoteDto quoteDto = quoteConverter.convert(stockQuote);
            if (quoteDto != null) {
                quoteDtoList.add(quoteDto);
            }
        }

        if (CollectionUtils.isEmpty(quoteDtoList)) {
            return null;
        }

        final Stock stock = stockQuotes.get(0).getStock();
        final StockQuote latestStockQuote = stockQuotes.get(stockQuotes.size() - 1);
        final StockQuote oldestStockQuote = stockQuotes.get(0);
        final BigDecimal performance =
                calculatePerformance(latestStockQuote.getAdjClose(), oldestStockQuote.getAdjClose());

        final ChartDto chartDto =
                new ChartDto(stock.getWkn(), stock.getIsin(), stock.getName(), quoteDtoList, performance);

        return chartDto;
    }

    protected BigDecimal calculatePerformance(final BigDecimal marketValue, final BigDecimal costBasis) {
        return (marketValue.subtract(costBasis))
                .divide(costBasis, 6, RoundingMode.CEILING)
                .multiply(HUNDRED)
                .setScale(2, RoundingMode.CEILING);
    }

}
