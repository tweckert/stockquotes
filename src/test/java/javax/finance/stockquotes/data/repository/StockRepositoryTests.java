package javax.finance.stockquotes.data.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import javax.finance.stockquotes.data.entity.Stock;

@SpringBootTest()
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class StockRepositoryTests {

    @Autowired
    private StockRepository stockRepository;

    @Test
    public void saveTest() {

        // GIVEN
        final Stock dirtyStock = new Stock();
        dirtyStock.setIsin("IE00B4L5Y983");
        dirtyStock.setName("iShares Core MSCI World UCITS ETF USD (Acc)");

        // WHEN
        final Stock savedStock = stockRepository.save(dirtyStock);

        // THEN
        Assert.notNull(savedStock, "saved stock must not be null");
        Assert.notNull(savedStock.getId(), "saved stock ID must not be null");
    }

}
