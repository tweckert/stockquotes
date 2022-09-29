package javax.finance.stockquotes.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.ISINValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.finance.stockquotes.persistence.entity.Stock;
import javax.finance.stockquotes.persistence.repository.StockRepository;
import javax.finance.stockquotes.service.StockService;
import java.util.List;

@Service
public class DefaultStockService implements StockService {

    private final StockRepository stockRepository;
    private final ISINValidator isinValidator;

    @Autowired
    public DefaultStockService(final StockRepository stockRepository,
                               final ISINValidator isinValidator) {
        this.stockRepository = stockRepository;
        this.isinValidator = isinValidator;
    }

    @Override
    public boolean isValidIsin(final String securitiesIdentificationCode) {
        return StringUtils.isNotBlank(securitiesIdentificationCode)
                && isinValidator.isValid(securitiesIdentificationCode.toUpperCase().trim());
    }

    @Override
    public Stock findStock(final String stockSymbol) {

        final List<Stock> stockList =
                isValidIsin(stockSymbol)
                        ? stockRepository.findByIsin(stockSymbol)
                        : stockRepository.findByWkn(stockSymbol);

        return CollectionUtils.isNotEmpty(stockList) ? stockList.get(0) : null;
    }

}
