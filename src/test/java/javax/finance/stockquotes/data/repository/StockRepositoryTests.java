package javax.finance.stockquotes.data.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.Assert;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.finance.stockquotes.data.entity.Stock;
import javax.finance.stockquotes.test.PostgresTestContainer;
import javax.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@Transactional
public class StockRepositoryTests {

    @Container
    public static PostgresTestContainer POSTGRES_TEST_CONTAINER = PostgresTestContainer.getInstance();

    @Autowired
    private StockRepository stockRepository;

    @Sql(scripts = "/delete.sql")
    @Test
    public void saveTest() {

        // GIVEN
        final Stock dirtyStock = new Stock();
        dirtyStock.setIsin("IE00B4L5Y983");
        dirtyStock.setWkn("A0RPWH");
        dirtyStock.setName("iShares Core MSCI World UCITS ETF USD (Acc)");

        // WHEN
        final Stock savedStock = stockRepository.save(dirtyStock);

        // THEN
        Assert.notNull(savedStock, "saved stock must not be null");
        Assert.notNull(savedStock.getId(), "saved stock ID must not be null");
    }

}
