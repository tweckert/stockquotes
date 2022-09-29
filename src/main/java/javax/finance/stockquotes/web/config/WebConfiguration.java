package javax.finance.stockquotes.web.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.visualization.datasource.datatable.DataTable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.finance.stockquotes.constant.StockQuotesApplicationConstants;
import javax.finance.stockquotes.web.jackson.DataTableSerializer;

@Configuration
public class WebConfiguration {

    @Bean
    public LocaleResolver localeResolver() {
        final AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();
        acceptHeaderLocaleResolver.setDefaultLocale(StockQuotesApplicationConstants.DEFAULT_LOCALE);
        return acceptHeaderLocaleResolver;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {

        final SimpleModule dataTableModule = new SimpleModule();
        dataTableModule.addSerializer(DataTable.class, new DataTableSerializer(DataTable.class));

        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(dataTableModule);
    }

}
