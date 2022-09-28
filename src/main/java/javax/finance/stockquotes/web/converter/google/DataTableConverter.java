package javax.finance.stockquotes.web.converter.google;

import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.TableRow;
import com.google.visualization.datasource.datatable.value.ValueType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.finance.stockquotes.persistence.entity.StockQuote;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * See: {@link https://developers.google.com/chart/interactive/docs/reference}
 */
@Component
public class DataTableConverter implements Converter<List<StockQuote>, DataTable>  {

    private static final Logger LOG = LoggerFactory.getLogger(DataTableConverter.class);

    private final Converter<StockQuote, TableRow> tableRowConverter;

    @Autowired
    public DataTableConverter(final Converter<StockQuote, TableRow> tableRowConverter) {
        this.tableRowConverter = tableRowConverter;
    }

    @Override
    public DataTable convert(final List<StockQuote> stockQuotes) {

        if (CollectionUtils.isEmpty(stockQuotes)) {
            return null;
        }

        final List<TableRow> tableRowList = new ArrayList<>();
        for (final StockQuote stockQuote : stockQuotes) {

            final TableRow tableRow = tableRowConverter.convert(stockQuote);
            if (tableRow != null) {
                tableRowList.add(tableRow);
            }
        }

        if (CollectionUtils.isEmpty(tableRowList)) {
            return null;
        }

        final List<ColumnDescription> columnDescriptionList = new ArrayList<>();
        columnDescriptionList.add(new ColumnDescription(UUID.randomUUID().toString(), ValueType.DATE, "X"));
        columnDescriptionList.add(new ColumnDescription(UUID.randomUUID().toString(), ValueType.NUMBER, "Adj. Close"));

        final DataTable dataTable = new DataTable();
        dataTable.addColumns(columnDescriptionList);

        try {
            dataTable.addRows(tableRowList);
        } catch (final Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error(StringUtils.join("Error adding table rows to data table: ", e.getMessage()), e);
            }
        }

        return dataTable;
    }

}
