package javax.finance.stockquotes.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.finance.stockquotes.constant.StockQuotesConstants;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class QuoteDto implements Serializable {

    private static final long serialVersionUID = 6679112907916970452L;

    private final Long timestampSecs;

    @JsonFormat(pattern = StockQuotesConstants.ISO8601_DATE_FORMAT)
    private final Date date;

    private final BigDecimal adjClose;

    private final Integer volume;

    public QuoteDto(final Long timestampSecs, final Date date,
                    final BigDecimal adjClose, final Integer volume) {
        this.timestampSecs = timestampSecs;
        this.date = date;
        this.adjClose = adjClose;
        this.volume = volume;
    }

    public Long getTimestampSecs() {
        return timestampSecs;
    }

    public Date getDate() {
        return date;
    }

    public BigDecimal getAdjClose() {
        return adjClose;
    }

    public Integer getVolume() {
        return volume;
    }
}
