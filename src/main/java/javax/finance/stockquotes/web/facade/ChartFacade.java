package javax.finance.stockquotes.web.facade;

import javax.finance.stockquotes.persistence.entity.Frequency;

public interface ChartFacade<DTO> {

    DTO selectStockQuoteData(final String stockSymbol, final TimeRange timeRange, final Frequency frequency);

}
