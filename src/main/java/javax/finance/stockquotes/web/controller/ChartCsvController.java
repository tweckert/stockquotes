package javax.finance.stockquotes.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.finance.stockquotes.web.constant.WebConstants;
import javax.finance.stockquotes.web.dto.OhlcDto;
import javax.finance.stockquotes.web.facade.ChartFacade;
import java.util.List;

@RestController
@RequestMapping("/download/csv")
public class ChartCsvController extends AbstractController {

    @Autowired
    public ChartCsvController(final ChartFacade chartFacade,
                              final Converter<OhlcDto, List<String>> ohlcCsvConverter) {
        super(chartFacade, ohlcCsvConverter);
    }

    @RequestMapping(value = "/ohlc/wkn/{wkn}", method = RequestMethod.GET, produces = WebConstants.MEDIA_TYPE_APPLICATION_CSV)
    public ResponseEntity<Resource> ohlcChartByWkn(@PathVariable(required = true) final String wkn,
                                                   @RequestParam(name = "range", required = false) final String timeRangeName,
                                                   @RequestParam(name = "frequency", required = false) final String frequencyName) {
        return ohlcChartResourceResponse(true, wkn, timeRangeName, frequencyName);
    }

    @RequestMapping(value = "/ohlc/isin/{isin}", method = RequestMethod.GET, produces = WebConstants.MEDIA_TYPE_APPLICATION_CSV)
    public ResponseEntity<Resource> ohlcChartByIsin(@PathVariable(required = true) final String isin,
                                                        @RequestParam(name = "range", required = false) final String timeRangeName,
                                                        @RequestParam(name = "frequency", required = false) final String frequencyName) {
        return ohlcChartResourceResponse(false, isin, timeRangeName, frequencyName);
    }

}
