package javax.finance.stockquotes.persistence.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.finance.stockquotes.persistence.entity.Frequency;
import javax.finance.stockquotes.persistence.entity.StockQuote;
import java.util.Date;
import java.util.List;

@Repository
public interface StockQuoteRepository extends PagingAndSortingRepository<StockQuote, Long>, JpaSpecificationExecutor<StockQuote> {

    @Query("select distinct q from StockQuote q "
            + "inner join Stock s on s = q.stock "
            + "where s.wkn = :wkn "
            + "and q.frequency = :frequency "
            + "and q.date between :oldestDate and :youngestDate "
            + "order by q.date asc")
    List<StockQuote> findByWkn(@Param("wkn") final String wkn,
                               @Param("frequency") final Frequency frequency,
                               @Param("oldestDate") final Date oldestDate,
                               @Param("youngestDate") final Date youngestDate);

    @Query("select distinct q from StockQuote q "
            + "inner join Stock s on s = q.stock "
            + "where s.isin = :isin "
            + "and q.frequency = :frequency "
            + "and q.date between :oldestDate and :youngestDate "
            + "order by q.date asc")
    List<StockQuote> findByIsin(@Param("isin") final String isin,
                                @Param("frequency") final Frequency frequency,
                                @Param("oldestDate") final Date oldestDate,
                                @Param("youngestDate") final Date youngestDate);

}
