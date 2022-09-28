package javax.finance.stockquotes.web.facade.impl;

import com.google.visualization.datasource.datatable.DataTable;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import javax.finance.stockquotes.persistence.entity.Frequency;
import javax.finance.stockquotes.persistence.entity.StockQuote;
import javax.finance.stockquotes.persistence.repository.StockQuoteRepository;
import javax.finance.stockquotes.web.facade.ChartFacade;
import javax.finance.stockquotes.web.facade.TimeRange;
import java.util.List;

@Service
public class DataTableChartFacade extends AbstractChartFacade implements ChartFacade<DataTable> {

    private final Converter<List<StockQuote>, DataTable> dataTableConverter;

    @Autowired
    public DataTableChartFacade(final StockQuoteRepository stockQuoteRepository,
                                final Converter<List<StockQuote>, DataTable> dataTableConverter) {
        super(stockQuoteRepository);
        this.dataTableConverter = dataTableConverter;
    }

    @Override
    public DataTable getByWkn(final String wkn, final TimeRange timeRange, final Frequency frequency) {
        return getBySymbol(true, wkn, timeRange, frequency);
    }

    @Override
    public DataTable getByIsin(final String isin, final TimeRange timeRange, final Frequency frequency) {
        return getBySymbol(false, isin, timeRange, frequency);
    }

    protected DataTable getBySymbol(final boolean stockSymbolIsWkn, final String stockSymbol,
                                    final TimeRange timeRange, final Frequency frequency) {

        final List<StockQuote> stockQuotes = selectStockQuotes(stockSymbolIsWkn, stockSymbol, timeRange, frequency);
        final DataTable dataTable = createDataTable(stockQuotes, frequency, timeRange);

        return dataTable;
    }

    protected DataTable createDataTable(final List<StockQuote> stockQuotes, final Frequency frequency, final TimeRange timeRange) {

        if (CollectionUtils.isEmpty(stockQuotes) || frequency == null || timeRange == null) {
            return null;
        }

        return dataTableConverter.convert(stockQuotes);
    }

}
