package javax.finance.stockquotes.web.converter.ohlc;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.finance.stockquotes.persistence.entity.Stock;
import javax.finance.stockquotes.persistence.entity.StockQuote;
import javax.finance.stockquotes.web.dto.OhlcChartDto;
import javax.finance.stockquotes.web.dto.OhlcDto;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class OhlcChartDtoConverter implements Converter<List<StockQuote>, OhlcChartDto> {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100l);

    private final Converter<StockQuote, OhlcDto> ohlcDtoConverter;

    @Autowired
    public OhlcChartDtoConverter(final Converter<StockQuote, OhlcDto> ohlcDtoConverter) {
        this.ohlcDtoConverter = ohlcDtoConverter;
    }

    @Override
    public OhlcChartDto convert(final List<StockQuote> stockQuotes) {

        if (CollectionUtils.isEmpty(stockQuotes)) {
            return null;
        }

        final List<OhlcDto> ohlcDtoList = new ArrayList<>();
        for (final StockQuote stockQuote : stockQuotes) {

            final OhlcDto ohlcDto = ohlcDtoConverter.convert(stockQuote);
            if (ohlcDto != null) {
                ohlcDtoList.add(ohlcDto);
            }
        }

        if (CollectionUtils.isEmpty(ohlcDtoList)) {
            return null;
        }

        final Stock firstStock = stockQuotes.get(0).getStock();
        final StockQuote latestStockQuote = stockQuotes.get(stockQuotes.size() - 1);
        final StockQuote oldestStockQuote = stockQuotes.get(0);
        final BigDecimal performance =
                calculatePerformance(latestStockQuote.getAdjClose(), oldestStockQuote.getAdjClose());

        final OhlcChartDto ohlcChartDto =
                new OhlcChartDto(firstStock.getWkn(), firstStock.getIsin(), firstStock.getName(),
                        performance, null, null, ohlcDtoList);

        return ohlcChartDto;
    }

    protected BigDecimal calculatePerformance(final BigDecimal marketValue, final BigDecimal costBasis) {
        return (marketValue.subtract(costBasis))
                .divide(costBasis, 6, RoundingMode.CEILING)
                .multiply(HUNDRED)
                .setScale(2, RoundingMode.CEILING);
    }

}
