package javax.finance.stockquotes.web.dto;

import javax.finance.stockquotes.data.entity.Frequency;
import javax.finance.stockquotes.web.facade.TimeRange;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class OhlcChartDto implements Serializable {

    private static final long serialVersionUID = 4508637980500714657L;

    private String wkn;
    private String isin;
    private String name;
    private BigDecimal performance;
    private Frequency frequency;
    private TimeRange range;
    private List<OhlcDto> ohlc;

    public OhlcChartDto(final String wkn, final String isin, final String name,
                        final BigDecimal performance, final Frequency frequency,
                        final TimeRange timeRange, final List<OhlcDto> ohlc) {
        this.wkn = wkn;
        this.isin = isin;
        this.name = name;
        this.performance = performance;
        this.frequency = frequency;
        this.range = timeRange;
        this.ohlc = ohlc;
    }

    public String getWkn() {
        return wkn;
    }

    public void setWkn(final String wkn) {
        this.wkn = wkn;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(final String isin) {
        this.isin = isin;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPerformance() {
        return performance;
    }

    public void setPerformance(final BigDecimal performance) {
        this.performance = performance;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(final Frequency frequency) {
        this.frequency = frequency;
    }

    public TimeRange getRange() {
        return range;
    }

    public void setRange(final TimeRange range) {
        this.range = range;
    }

    public List<OhlcDto> getOhlc() {
        return ohlc;
    }

    public void setOhlc(final List<OhlcDto> ohlc) {
        this.ohlc = ohlc;
    }
}
