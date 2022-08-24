package javax.finance.stockquotes.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import javax.finance.stockquotes.data.entity.Stock;
import javax.finance.stockquotes.data.repository.StockQuoteRepository;
import javax.finance.stockquotes.data.repository.StockRepository;
import java.io.File;

@SpringBootTest()
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class DefaultYahooFinanceImportServiceTest {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockQuoteRepository stockQuoteRepository;

    @Autowired
    private DefaultYahooFinanceImportService defaultYahooFinanceImportService;

    @Test
    public void importHistoricalDataTest() throws Exception {

        // GIVEN
        final Stock dirtyStock = new Stock();
        dirtyStock.setIsin("IE00B4L5Y983");
        dirtyStock.setName("iShares Core MSCI World UCITS ETF USD (Acc)");
        final Stock stock = stockRepository.save(dirtyStock);

        final File file = ResourceUtils.getFile("classpath:data/IWDA.L.csv");

        // WHEN
        defaultYahooFinanceImportService.importHistoricalData(stock, file);

        // THEN
        final long expectedCount = 3262;
        Assert.isTrue(stockQuoteRepository.count() == expectedCount,
                StringUtils.join("Yahoo Finance import service should have imported ", expectedCount, " stock quotes"));
    }

}
