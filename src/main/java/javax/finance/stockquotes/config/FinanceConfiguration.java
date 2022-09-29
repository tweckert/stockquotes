package javax.finance.stockquotes.config;

import org.apache.commons.validator.routines.ISINValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FinanceConfiguration {

    @Bean
    public ISINValidator isinValidator() {
        return ISINValidator.getInstance(true);
    }

}
