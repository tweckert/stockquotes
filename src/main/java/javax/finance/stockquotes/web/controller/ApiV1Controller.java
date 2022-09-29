package javax.finance.stockquotes.web.controller;

import com.google.visualization.datasource.datatable.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.finance.stockquotes.web.constant.WebConstants;
import javax.finance.stockquotes.web.dto.OhlcChartDto;
import javax.finance.stockquotes.web.facade.ChartFacade;

@RestController
@RequestMapping(WebConstants.PATH_PREFIX_API_V1)
public class ApiV1Controller extends AbstractController {

    @Autowired
    public ApiV1Controller(final ChartFacade<OhlcChartDto> ohlcChartFacade,
                           final ChartFacade<DataTable> dataTableChartFacade) {
        super(ohlcChartFacade, dataTableChartFacade);
    }

    @RequestMapping(value = WebConstants.PATH_OHLC_API + "/{stockSymbol}",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OhlcChartDto> ohlcByWkn(@PathVariable(required = true) final String stockSymbol,
                                                  @RequestParam(name = "range", required = false) final String timeRangeName,
                                                  @RequestParam(name = "frequency", required = false) final String frequencyName) {
        return ohlcChartDtoResponse(stockSymbol, timeRangeName, frequencyName);
    }

    @RequestMapping(value = WebConstants.PATH_DATATABLE_API + "/{stockSymbol}",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String dataTableByWkn(@PathVariable(required = true) final String stockSymbol,
                                 @RequestParam(name = "range", required = false) final String timeRangeName,
                                 @RequestParam(name = "frequency", required = false) final String frequencyName) {
        return dataTableResponse(stockSymbol, timeRangeName, frequencyName);
    }

}
