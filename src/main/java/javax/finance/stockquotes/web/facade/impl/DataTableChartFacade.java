package javax.finance.stockquotes.web.facade.impl;

import com.google.visualization.datasource.datatable.DataTable;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import javax.finance.stockquotes.persistence.entity.Frequency;
import javax.finance.stockquotes.persistence.entity.StockQuote;
import javax.finance.stockquotes.persistence.repository.StockQuoteRepository;
import javax.finance.stockquotes.service.StockService;
import javax.finance.stockquotes.web.facade.ChartFacade;
import javax.finance.stockquotes.web.facade.TimeRange;
import java.util.List;

@Service
public class DataTableChartFacade extends AbstractChartFacade implements ChartFacade<DataTable> {

    private final Converter<List<StockQuote>, DataTable> dataTableConverter;

    @Autowired
    public DataTableChartFacade(final StockQuoteRepository stockQuoteRepository,
                                final StockService stockService,
                                final Converter<List<StockQuote>, DataTable> dataTableConverter) {
        super(stockQuoteRepository, stockService);
        this.dataTableConverter = dataTableConverter;
    }

    @Override
    public DataTable selectStockQuoteData(final String stockSymbol, final TimeRange timeRange, final Frequency frequency) {

        final List<StockQuote> stockQuotes = selectStockQuotes(stockSymbol, timeRange, frequency);
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
