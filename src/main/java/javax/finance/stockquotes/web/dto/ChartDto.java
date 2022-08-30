package javax.finance.stockquotes.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ChartDto implements Serializable {

    private static final long serialVersionUID = 4508637980500714657L;

    private final String wkn;
    private final String isin;
    private final String name;
    private final BigDecimal performance;
    private final List<QuoteDto> quotes;

    public ChartDto(final String wkn, final String isin, final String name,
                    final List<QuoteDto> quotes, final BigDecimal performance) {
        this.wkn = wkn;
        this.isin = isin;
        this.name = name;
        this.performance = performance;
        this.quotes = quotes;
    }

    public String getWkn() {
        return wkn;
    }

    public String getIsin() {
        return isin;
    }

    public String getName() {
        return name;
    }

    public List<QuoteDto> getQuotes() {
        return quotes;
    }

    public BigDecimal getPerformance() {
        return performance;
    }

}
