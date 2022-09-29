package javax.finance.stockquotes.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.finance.stockquotes.constant.StockQuotesApplicationConstants;

@Configuration
public class WebConfiguration {

    @Bean
    public LocaleResolver localeResolver() {
        final AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();
        acceptHeaderLocaleResolver.setDefaultLocale(StockQuotesApplicationConstants.DEFAULT_LOCALE);
        return acceptHeaderLocaleResolver;
    }

}
