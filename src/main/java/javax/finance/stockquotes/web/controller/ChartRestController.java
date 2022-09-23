package javax.finance.stockquotes.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.finance.stockquotes.web.dto.OhlcChartDto;
import javax.finance.stockquotes.web.dto.OhlcDto;
import javax.finance.stockquotes.web.facade.ChartFacade;
import java.util.List;

@RestController
@RequestMapping("/api/json")
public class ChartRestController extends AbstractController {

    @Autowired
    public ChartRestController(final ChartFacade chartFacade,
                               final Converter<OhlcDto, List<String>> ohlcCsvConverter) {
        super(chartFacade, ohlcCsvConverter);
    }

    @RequestMapping(value = "/ohlc/wkn/{wkn}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OhlcChartDto> ohlcChartByWkn(@PathVariable(required = true) final String wkn,
                                                       @RequestParam(name = "range", required = false) final String timeRangeName,
                                                       @RequestParam(name = "frequency", required = false) final String frequencyName) {
        return ohlcChartDtoResponse(true, wkn, timeRangeName, frequencyName);
    }

    @RequestMapping(value = "/ohlc/isin/{isin}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OhlcChartDto> ohlcChartByIsin(@PathVariable(required = true) final String isin,
                                                        @RequestParam(name = "range", required = false) final String timeRangeName,
                                                        @RequestParam(name = "frequency", required = false) final String frequencyName) {
        return ohlcChartDtoResponse(false, isin, timeRangeName, frequencyName);
    }

}
