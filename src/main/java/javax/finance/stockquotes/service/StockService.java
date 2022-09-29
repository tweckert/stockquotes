package javax.finance.stockquotes.service;

import javax.finance.stockquotes.persistence.entity.Stock;

public interface StockService {

    boolean isValidIsin(final String securitiesIdentificationCode);

    Stock findStock(final String stockSymbol);

}
