package javax.finance.stockquotes.data.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.finance.stockquotes.data.entity.StockQuote;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class StockQuoteSpecification implements Specification<StockQuote> {

    private final String isin;

    public StockQuoteSpecification(final String isin) {
        this.isin = isin;
    }

    @Override
    public Predicate toPredicate(final Root<StockQuote> root, final CriteriaQuery<?> query,
                                 final CriteriaBuilder criteriaBuilder) {

        final List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(isin)) {
            predicates.add(criteriaBuilder.equal(root.get("isin"), isin));
        }

        return !predicates.isEmpty()
                ? criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]))
                : null;
    }

}
