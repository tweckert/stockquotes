package javax.finance.stockquotes.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.finance.stockquotes.data.entity.Frequency;
import javax.finance.stockquotes.web.dto.ChartDto;
import javax.finance.stockquotes.web.facade.ChartFacade;
import javax.finance.stockquotes.web.facade.TimeRange;

@RestController
@RequestMapping("/stockquotes")
public class StockQuotesRestController {

    private final ChartFacade chartFacade;

    @Autowired
    public StockQuotesRestController(final ChartFacade chartFacade) {
        this.chartFacade = chartFacade;
    }

    @RequestMapping(value = "/chart/wkn/{wkn}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChartDto> chartByWkn(@PathVariable final String wkn,
                                               @RequestParam(name = "range", required = false) final String timeRangeName,
                                               @RequestParam(name = "frequency", required = false) final String frequencyName) {

        final ChartDto chartDto =
                chartFacade.selectChartByWkn(wkn, TimeRange.of(timeRangeName), Frequency.of(frequencyName));

        return chartDto != null
                ? ResponseEntity.ok(chartDto)
                : ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/chart/isin/{isin}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChartDto> chartByIsin(@PathVariable final String isin,
                                                @RequestParam(name = "range", required = false) final String timeRangeName,
                                                @RequestParam(name = "frequency", required = false) final String frequencyName) {

        final ChartDto chartDto =
                chartFacade.selectChartByIsin(isin, TimeRange.of(timeRangeName), Frequency.of(frequencyName));

        return chartDto != null
                ? ResponseEntity.ok(chartDto)
                : ResponseEntity.notFound().build();
    }

}
