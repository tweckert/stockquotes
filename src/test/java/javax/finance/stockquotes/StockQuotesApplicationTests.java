package javax.finance.stockquotes;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.transaction.Transactional;

@SpringBootTest()
@ActiveProfiles("test")
@Testcontainers
@Transactional
public class StockQuotesApplicationTests {

    @Test
    public void contextLoads() {
    }

}
