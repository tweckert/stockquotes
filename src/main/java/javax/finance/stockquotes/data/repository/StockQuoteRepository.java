package javax.finance.stockquotes.data.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.finance.stockquotes.data.entity.StockQuote;

@Repository
public interface StockQuoteRepository extends PagingAndSortingRepository<StockQuote, Long>, JpaSpecificationExecutor<StockQuote> {

    // intentionally left blank

}
