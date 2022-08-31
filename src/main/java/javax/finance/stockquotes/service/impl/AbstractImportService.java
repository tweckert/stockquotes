package javax.finance.stockquotes.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.finance.stockquotes.data.entity.Frequency;
import javax.finance.stockquotes.data.entity.Stock;
import javax.finance.stockquotes.data.entity.StockQuote;
import javax.finance.stockquotes.data.repository.StockQuoteRepository;
import javax.finance.stockquotes.service.ImportService;
import javax.finance.stockquotes.yahoo.service.YahooImportService;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractImportService implements ImportService {

    private static final Logger LOG = LoggerFactory.getLogger(YahooImportService.class);

    private final StockQuoteRepository stockQuoteRepository;

    public AbstractImportService(final StockQuoteRepository stockQuoteRepository) {
        this.stockQuoteRepository = stockQuoteRepository;
    }

    protected void importStockQuotes(final Stock stock,
                                     final List<StockQuote> importStockQuoteList,
                                     final Frequency frequency) {

        if (stock == null || CollectionUtils.isEmpty(importStockQuoteList) || frequency == null) {
            return;
        }

        // sort oldest to youngest
        importStockQuoteList.sort(Comparator.comparing(StockQuote::getDate));

        final StockQuote oldestStockQuote = importStockQuoteList.get(0);
        final StockQuote youngestStockQuote = importStockQuoteList.get(importStockQuoteList.size() - 1);

        final List<StockQuote> removeStockQuoteList =
                stockQuoteRepository.findByWkn(stock.getWkn(), frequency, youngestStockQuote.getDate(), oldestStockQuote.getDate());

        transactionalUpdateStockQuotes(removeStockQuoteList, importStockQuoteList);

        if (LOG.isInfoEnabled()) {
            LOG.info(StringUtils.join("Deleted ", removeStockQuoteList.size(),
                    " existing stock quotes, imported ", importStockQuoteList.size(),
                    " stock quotes with frequency '", frequency.name(),
                    "' for stock '", stock.getWkn(), "'"));
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    protected void transactionalUpdateStockQuotes(final List<StockQuote> removeStockQuoteList,
                                                  final List<StockQuote> importStockQuoteList) {
        stockQuoteRepository.deleteAll(removeStockQuoteList);
        stockQuoteRepository.saveAll(importStockQuoteList);
    }

}
