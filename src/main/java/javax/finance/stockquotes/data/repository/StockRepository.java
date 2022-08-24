package javax.finance.stockquotes.data.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.finance.stockquotes.data.entity.Stock;

@Repository
public interface StockRepository extends PagingAndSortingRepository<Stock, Long> {

    // intentionally left blank

}
