package javax.finance.stockquotes.web.controller;

import com.google.visualization.datasource.datatable.DataTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.finance.stockquotes.web.constant.WebConstants;
import javax.finance.stockquotes.web.dto.OhlcChartDto;
import javax.finance.stockquotes.web.facade.ChartFacade;

@RestController
@RequestMapping(WebConstants.API_PATH_PREFIX_V1)
public class ApiV1Controller extends AbstractController {

    private static final Logger LOG = LoggerFactory.getLogger(ApiV1Controller.class);

    @Autowired
    public ApiV1Controller(final ChartFacade<OhlcChartDto> ohlcChartFacade,
                           final ChartFacade<DataTable> dataTableChartFacade) {
        super(ohlcChartFacade, dataTableChartFacade);
    }

    @RequestMapping(value = WebConstants.API_PATH_OHLC + "/{stockSymbol}",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity ohlcByWkn(@PathVariable(required = true) final String stockSymbol,
                                    @RequestParam(name = "range", required = false) final String timeRangeName,
                                    @RequestParam(name = "frequency", required = false) final String frequencyName) {

        try {

            final OhlcChartDto ohlcChartDto = getOhlcChartDto(stockSymbol, timeRangeName, frequencyName);

            return ohlcChartDto != null
                    ? ResponseEntity.ok(ohlcChartDto)
                    : notFoundResponseEntity(stockSymbol, timeRangeName, frequencyName);
        } catch (final Exception e) {

            if (LOG.isErrorEnabled()) {
                LOG.error(e.getMessage(), e);
            }

            return errorResponseEntity(e, stockSymbol, timeRangeName, frequencyName);
        }
    }

    @RequestMapping(value = WebConstants.API_PATH_DATATABLE + "/{stockSymbol}",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity dataTableByWkn(@PathVariable(required = true) final String stockSymbol,
                                         @RequestParam(name = "range", required = false) final String timeRangeName,
                                         @RequestParam(name = "frequency", required = false) final String frequencyName) {

        try {

            final DataTable dataTable = dataTableResponse(stockSymbol, timeRangeName, frequencyName);

            return dataTable != null
                    ? ResponseEntity.ok(dataTable)
                    : notFoundResponseEntity(stockSymbol, timeRangeName, frequencyName);
        } catch (final Exception e) {

            if (LOG.isErrorEnabled()) {
                LOG.error(e.getMessage(), e);
            }

            return errorResponseEntity(e, stockSymbol, timeRangeName, frequencyName);
        }
    }

}
