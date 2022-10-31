package javax.finance.stockquotes.config;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@ConfigurationProperties(prefix = "quotes")
public class QuotesConfigurationProperties {

    private List<StockProperties> stocks;

    public List<StockProperties> getStocks() {
        return stocks;
    }

    public void setStocks(final List<StockProperties> stocks) {
        this.stocks = stocks;
    }

    public Map<String, StockProperties> getStockPropertiesByWkn() {
        return CollectionUtils.isNotEmpty(stocks)
                ? stocks.stream().collect(Collectors.collectingAndThen(Collectors.toMap(StockProperties::getWkn, Function.identity()), Collections::unmodifiableMap))
                : Collections.emptyMap();
    }

    @Component
    @ConfigurationProperties(prefix = "quotes.stocks")
    public static class StockProperties {

        private String isin;
        private String wkn;
        private String name;

        public String getIsin() {
            return StringUtils.isNotBlank(isin) ? isin.toUpperCase() : isin;
        }

        public void setIsin(final String isin) {
            this.isin = isin;
        }

        public String getWkn() {
            return StringUtils.isNotBlank(wkn) ? wkn.toUpperCase() : wkn;
        }

        public void setWkn(final String wkn) {
            this.wkn = wkn;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }
    }

}
