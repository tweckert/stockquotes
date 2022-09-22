package javax.finance.stockquotes.persistence.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.finance.stockquotes.persistence.entity.Stock;
import java.util.List;

@Repository
public interface StockRepository extends PagingAndSortingRepository<Stock, Long> {

    List<Stock> findByIsin(final String isin);

    List<Stock> findByWkn(final String wkn);

}
