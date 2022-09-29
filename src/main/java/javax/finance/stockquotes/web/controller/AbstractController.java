package javax.finance.stockquotes.web.controller;

import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.render.JsonRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import javax.finance.stockquotes.persistence.entity.Frequency;
import javax.finance.stockquotes.web.dto.OhlcChartDto;
import javax.finance.stockquotes.web.facade.ChartFacade;
import javax.finance.stockquotes.web.facade.TimeRange;

public abstract class AbstractController {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractController.class);

    private final ChartFacade<OhlcChartDto> ohlcChartFacade;
    private final ChartFacade<DataTable> dataTableChartFacade;

    protected AbstractController(final ChartFacade<OhlcChartDto> ohlcChartFacade,
                                 final ChartFacade<DataTable> dataTableChartFacade) {
        this.ohlcChartFacade = ohlcChartFacade;
        this.dataTableChartFacade = dataTableChartFacade;
    }

    protected ResponseEntity<OhlcChartDto> ohlcChartDtoResponse(final String stockSymbol, final String timeRangeName,
                                                                final String frequencyName) {

        final TimeRange timeRange = TimeRange.of(timeRangeName);
        final Frequency frequency = getFrequency(timeRange, frequencyName);
        final OhlcChartDto ohlcChartDto = ohlcChartFacade.selectStockQuoteData(stockSymbol, timeRange, frequency);

        return ohlcChartDto != null
                ? ResponseEntity.ok(ohlcChartDto)
                : ResponseEntity.notFound().build();
    }

    protected String dataTableResponse(final String stockSymbol, final String timeRangeName, final String frequencyName) {

        final TimeRange timeRange = TimeRange.of(timeRangeName);
        final Frequency frequency = getFrequency(timeRange, frequencyName);
        final DataTable dataTable = dataTableChartFacade.selectStockQuoteData(stockSymbol, timeRange, frequency);

        if (dataTable == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return JsonRenderer.renderDataTable(dataTable, true, false, false).toString();
    }

    protected Frequency getFrequency(final TimeRange timeRange, final String frequencyName) {
        return TimeRange.WEEK.equals(timeRange)
                ? Frequency.DAILY
                : Frequency.of(frequencyName);
    }

}
