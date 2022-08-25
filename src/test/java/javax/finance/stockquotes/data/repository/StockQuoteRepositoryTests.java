package javax.finance.stockquotes.data.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.Assert;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.finance.stockquotes.converter.DateConverter;
import javax.finance.stockquotes.data.entity.Stock;
import javax.finance.stockquotes.data.entity.StockQuote;
import javax.finance.stockquotes.test.PostgresTestContainer;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@Transactional
public class StockQuoteRepositoryTests {

    @Container
    public static PostgresTestContainer POSTGRES_TEST_CONTAINER = PostgresTestContainer.getInstance();

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockQuoteRepository stockQuoteRepository;

    @Sql(scripts = "/delete.sql")
    @Test
    public void saveTest() {

        // GIVEN
        final Stock dirtyStock = new Stock();
        dirtyStock.setIsin("IE00B4L5Y983");
        dirtyStock.setName("iShares Core MSCI World UCITS ETF USD (Acc)");
        final Stock stock = stockRepository.save(dirtyStock);

        final Converter<String, Date> stringToDateConverter = new DateConverter("yyyy-MM-dd");

        final Date date = stringToDateConverter.convert("2009-09-25");
        final BigDecimal open = new BigDecimal("24.820520");
        final BigDecimal close = new BigDecimal("24.773529");
        final BigDecimal high = new BigDecimal("24.259439");
        final BigDecimal low = new BigDecimal("23.973881");
        final BigDecimal adjClose = new BigDecimal("25.330000");
        final BigDecimal volume = new BigDecimal("208861");

        final StockQuote dirtyStockQuote = new StockQuote();
        dirtyStockQuote.setDate(date);
        dirtyStockQuote.setOpen(open);
        dirtyStockQuote.setClose(close);
        dirtyStockQuote.setHigh(high);
        dirtyStockQuote.setLow(low);
        dirtyStockQuote.setAdjClose(adjClose);
        dirtyStockQuote.setVolume(volume);
        dirtyStockQuote.setStock(stock);

        // WHEN
        final StockQuote savedStockQuote = stockQuoteRepository.save(dirtyStockQuote);

        // THEN
        Assert.notNull(savedStockQuote, "saved stock quote must not be null");
        Assert.notNull(savedStockQuote.getId(), "saved stock quote ID must not be null");
    }

}
