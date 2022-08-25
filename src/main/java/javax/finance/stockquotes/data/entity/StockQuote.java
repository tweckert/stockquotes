package javax.finance.stockquotes.data.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "quotes")
public class StockQuote {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "open", nullable = false)
    private BigDecimal open;

    @Column(name = "close", nullable = false)
    private BigDecimal close;

    @Column(name = "adj_close", nullable = false)
    private BigDecimal adjClose;

    @Column(name = "high", nullable = false)
    private BigDecimal high;

    @Column(name = "low", nullable = false)
    private BigDecimal low;

    @Column(name = "volume", nullable = false)
    private BigDecimal volume;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private Stock stock;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
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

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(final BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getAdjClose() {
        return adjClose;
    }

    public void setAdjClose(final BigDecimal adjClose) {
        this.adjClose = adjClose;
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

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(final BigDecimal volume) {
        this.volume = volume;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(final Stock stock) {
        this.stock = stock;
    }

}
