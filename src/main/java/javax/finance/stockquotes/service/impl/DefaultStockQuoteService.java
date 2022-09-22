package javax.finance.stockquotes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.finance.stockquotes.persistence.entity.StockQuote;
import javax.finance.stockquotes.persistence.repository.StockQuoteRepository;
import javax.finance.stockquotes.persistence.specification.StockQuoteSpecification;
import javax.finance.stockquotes.service.StockQuoteService;

@Service
public class DefaultStockQuoteService implements StockQuoteService {

    private final StockQuoteRepository stockQuoteRepository;

    @Autowired
    public DefaultStockQuoteService(final StockQuoteRepository stockQuoteRepository) {
        this.stockQuoteRepository = stockQuoteRepository;
    }

    @Override
    public Page<StockQuote> getStockQuotesForDatatable(final String isin, final Pageable pageable) {
        final Specification<StockQuote> stockQuoteSpecification = new StockQuoteSpecification(isin);
        return stockQuoteRepository.findAll(stockQuoteSpecification, pageable);
    }

}
