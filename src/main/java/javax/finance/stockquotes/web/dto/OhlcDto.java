package javax.finance.stockquotes.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.finance.stockquotes.constant.StockQuotesApplicationConstants;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OhlcDto implements Serializable {

    private static final long serialVersionUID = 6679112907916970452L;

    private final Long timestampSecs;

    @JsonFormat(pattern = StockQuotesApplicationConstants.ISO8601_DATE_FORMAT, timezone = StockQuotesApplicationConstants.TIMEZONE_UTC)
    private Date date;

    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal adjcClose;
    private Integer volume;

    public OhlcDto(final Long timestampSecs, final Date date, final BigDecimal open, final BigDecimal high,
                   final BigDecimal low, final BigDecimal adjcClose, final Integer volume) {
        this.timestampSecs = timestampSecs;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.adjcClose = adjcClose;
        this.volume = volume;
    }

    public Long getTimestampSecs() {
        return timestampSecs;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(final BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(final BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(final BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getAdjcClose() {
        return adjcClose;
    }

    public void setAdjcClose(final BigDecimal adjcClose) {
        this.adjcClose = adjcClose;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(final Integer volume) {
        this.volume = volume;
    }

}
