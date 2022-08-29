package javax.finance.stockquotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockQuotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockQuotesApplication.class, args);
	}

}
