package javax.finance.stockquotes.web.controller;

import com.google.visualization.datasource.datatable.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.finance.stockquotes.web.dto.OhlcChartDto;
import javax.finance.stockquotes.web.facade.ChartFacade;

@RestController
@RequestMapping("/api/v1")
public class ApiV1Controller extends AbstractController {

    @Autowired
    public ApiV1Controller(final ChartFacade<OhlcChartDto> ohlcChartFacade,
                           final ChartFacade<DataTable> dataTableChartFacade) {
        super(ohlcChartFacade, dataTableChartFacade);
    }

    @RequestMapping(value = "/ohlc/wkn/{wkn}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OhlcChartDto> ohlcByWkn(@PathVariable(required = true) final String wkn,
                                                  @RequestParam(name = "range", required = false) final String timeRangeName,
                                                  @RequestParam(name = "frequency", required = false) final String frequencyName) {
        return ohlcChartDtoResponse(true, wkn, timeRangeName, frequencyName);
    }

    @RequestMapping(value = "/ohlc/isin/{isin}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OhlcChartDto> ohlcByIsin(@PathVariable(required = true) final String isin,
                                                   @RequestParam(name = "range", required = false) final String timeRangeName,
                                                   @RequestParam(name = "frequency", required = false) final String frequencyName) {
        return ohlcChartDtoResponse(false, isin, timeRangeName, frequencyName);
    }

    @RequestMapping(value = "/datatable/wkn/{wkn}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String dataTableByWkn(@PathVariable(required = true) final String wkn,
                                                    @RequestParam(name = "range", required = false) final String timeRangeName,
                                                    @RequestParam(name = "frequency", required = false) final String frequencyName) {
        return dataTableResponse(true, wkn, timeRangeName, frequencyName);
    }

    @RequestMapping(value = "/datatable/isin/{isin}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String dataTableByIsin(@PathVariable(required = true) final String isin,
                                                     @RequestParam(name = "range", required = false) final String timeRangeName,
                                                     @RequestParam(name = "frequency", required = false) final String frequencyName) {
        return dataTableResponse(false, isin, timeRangeName, frequencyName);
    }

}
