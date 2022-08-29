package javax.finance.stockquotes.data.entity;

import javax.persistence.*;

@Entity
@Table(name = "stocks")
public class Stock {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String isin;

    @Column(nullable = false, unique = true)
    private String wkn;

    @Column(nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(final String isin) {
        this.isin = isin;
    }

    public String getWkn() {
        return wkn;
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
