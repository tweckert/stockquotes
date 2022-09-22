package javax.finance.stockquotes.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.finance.stockquotes.persistence.entity.StockQuote;

public interface StockQuoteService {

    Page<StockQuote> getStockQuotesForDatatable(final String isin, final Pageable pageable);

}
