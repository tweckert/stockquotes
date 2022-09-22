package javax.finance.stockquotes.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.finance.stockquotes.persistence.entity.Frequency;
import javax.finance.stockquotes.web.dto.OhlcChartDto;
import javax.finance.stockquotes.web.facade.ChartFacade;
import javax.finance.stockquotes.web.facade.TimeRange;

@RestController
@RequestMapping("/chart")
public class ChartRestController {

    private final ChartFacade chartFacade;

    @Autowired
    public ChartRestController(final ChartFacade chartFacade) {
        this.chartFacade = chartFacade;
    }

    @RequestMapping(value = "/ohlc/wkn/{wkn}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OhlcChartDto> ohlcChartByWkn(@PathVariable(required = true) final String wkn,
                                                       @RequestParam(name = "range", required = false) final String timeRangeName,
                                                       @RequestParam(name = "frequency", required = false) final String frequencyName) {
        return ohlcChartResponse(true, wkn, timeRangeName, frequencyName);
    }

    @RequestMapping(value = "/ohlc/isin/{isin}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OhlcChartDto> ohlcChartByIsin(@PathVariable(required = true) final String isin,
                                                        @RequestParam(name = "range", required = false) final String timeRangeName,
                                                        @RequestParam(name = "frequency", required = false) final String frequencyName) {
        return ohlcChartResponse(false, isin, timeRangeName, frequencyName);
    }

    protected ResponseEntity<OhlcChartDto> ohlcChartResponse(final boolean stockSymbolIsWkn, final String stockSymbol,
                                                             final String timeRangeName, final String frequencyName) {

        final TimeRange timeRange = TimeRange.of(timeRangeName);
        final Frequency frequency = chartFacade.getFrequency(timeRange, frequencyName);

        final OhlcChartDto ohlcChartDto =
                stockSymbolIsWkn
                        ? chartFacade.selectOhlcChartByWkn(stockSymbol, timeRange, frequency)
                        : chartFacade.selectOhlcChartByIsin(stockSymbol, timeRange, frequency);

        return ohlcChartDto != null
                ? ResponseEntity.ok(ohlcChartDto)
                : ResponseEntity.notFound().build();
    }

}
