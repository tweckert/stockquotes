package javax.finance.stockquotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import javax.finance.stockquotes.constant.StockQuotesApplicationConstants;
import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class StockQuotesApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockQuotesApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // set the entire app in UTC timezone (e.g. so that Hibernate will store dates in UTC)
        TimeZone.setDefault(StockQuotesApplicationConstants.DEFAULT_TIMEZONE);
        Locale.setDefault(StockQuotesApplicationConstants.DEFAULT_LOCALE);
    }

}
