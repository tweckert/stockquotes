package javax.finance.stockquotes.data.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.finance.stockquotes.data.entity.Frequency;
import javax.finance.stockquotes.data.entity.Stock;
import javax.finance.stockquotes.data.entity.StockQuote;
import java.util.Date;
import java.util.List;

@Repository
public interface StockQuoteRepository extends PagingAndSortingRepository<StockQuote, Long>, JpaSpecificationExecutor<StockQuote> {

    List<StockQuote> deleteByStockAndFrequency(final Stock stock, final Frequency frequency);

    @Query("select distinct q from StockQuote q inner join Stock s on s = q.stock where s.wkn = :wkn and q.frequency = :frequency and q.date <= :startDate and q.date >= :endDate order by q.date asc")
    List<StockQuote> findByWkn(@Param("wkn") final String wkn,
                               @Param("frequency") final Frequency frequency,
                               @Param("startDate") final Date startDate,
                               @Param("endDate") final Date endDate);

    @Query("select distinct q from StockQuote q inner join Stock s on s = q.stock where s.isin = :isin and q.frequency = :frequency and q.date <= :startDate and q.date >= :endDate order by q.date asc")
    List<StockQuote> findByIsin(@Param("isin") final String isin,
                                @Param("frequency") final Frequency frequency,
                                @Param("startDate") final Date startDate,
                                @Param("endDate") final Date endDate);

}
