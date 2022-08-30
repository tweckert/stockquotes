package javax.finance.stockquotes.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class QuoteDto implements Serializable {

    private static final long serialVersionUID = 6679112907916970452L;

    private final long timestampSecs;
    private final String iso8601Date;
    private final BigDecimal adjClose;
    private final BigDecimal volume;

    public QuoteDto(final long timestampSecs, final String iso8601Date,
                    final BigDecimal adjClose, final BigDecimal volume) {
        this.timestampSecs = timestampSecs;
        this.iso8601Date = iso8601Date;
        this.adjClose = adjClose;
        this.volume = volume;
    }

    public long getTimestampSecs() {
        return timestampSecs;
    }

    public String getIso8601Date() {
        return iso8601Date;
    }

    public BigDecimal getAdjClose() {
        return adjClose;
    }

    public BigDecimal getVolume() {
        return volume;
    }

}
