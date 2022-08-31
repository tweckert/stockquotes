package javax.finance.stockquotes.yahoo.service;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.finance.stockquotes.data.entity.Frequency;
import javax.finance.stockquotes.data.entity.Stock;
import javax.finance.stockquotes.data.repository.StockQuoteRepository;
import javax.finance.stockquotes.data.repository.StockRepository;
import javax.transaction.Transactional;
import java.io.File;

@SpringBootTest()
@ActiveProfiles("test")
@Testcontainers
@Transactional
public class YahooImportServiceTest {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockQuoteRepository stockQuoteRepository;

    @Autowired
    private YahooImportService yahooImportService;

    @Test
    public void importHistoricalDataTest() throws Exception {

        // GIVEN
        final Stock dirtyStock = new Stock();
        dirtyStock.setIsin("IE00B4L5Y983");
        dirtyStock.setWkn("A0RPWH");
        dirtyStock.setName("iShares Core MSCI World UCITS ETF USD (Acc)");
        final Stock stock = stockRepository.save(dirtyStock);

        final Frequency frequency = Frequency.DAILY;
        final File file = ResourceUtils.getFile("classpath:yahoo/A0RPWH.csv");

        // WHEN
        yahooImportService.importHistoricalData(stock, frequency, file);

        // THEN
        final long expectedCount = 3262;
        Assert.isTrue(stockQuoteRepository.count() == expectedCount,
                StringUtils.join("Yahoo Finance import service should have imported ", expectedCount, " stock quotes"));

        stockQuoteRepository
                .findAll()
                .forEach(stockQuote ->
                        Assert.isTrue(stock.getIsin().equals(stockQuote.getStock().getIsin()), "stock quote stock isin should be equal to stock isin"));
    }

}
