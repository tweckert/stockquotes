package javax.finance.stockquotes.web.controller;

import com.google.visualization.datasource.datatable.DataTable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.finance.stockquotes.persistence.entity.Frequency;
import javax.finance.stockquotes.persistence.entity.Stock;
import javax.finance.stockquotes.service.StockService;
import javax.finance.stockquotes.web.constant.WebConstants;
import javax.finance.stockquotes.web.dto.OhlcChartDto;
import javax.finance.stockquotes.web.facade.ChartFacade;
import javax.finance.stockquotes.web.facade.TimeRange;

@Controller
public class TemplateController extends AbstractController {

    private final StockService stockService;

    @Autowired
    public TemplateController(final StockService stockService,
                              final ChartFacade<OhlcChartDto> ohlcChartFacade,
                              final ChartFacade<DataTable> dataTableChartFacade) {
        super(ohlcChartFacade, dataTableChartFacade);
        this.stockService = stockService;
    }

    @GetMapping(value = "/chart/{stockSymbol}")
    public ModelAndView chart(@PathVariable(required = true) final String stockSymbol,
                              @RequestParam(name = "range", required = false) final String timeRangeName,
                              @RequestParam(name = "frequency", required = false) final String frequencyName) {

        final Stock stock = stockService.findStock(stockSymbol);
        final TimeRange timeRange = TimeRange.of(timeRangeName);
        final Frequency frequency = getFrequency(timeRange, frequencyName);
        final String jsonDataUrl = StringUtils.join(WebConstants.PATH_PREFIX_API_V1, WebConstants.PATH_DATATABLE_API,
                "/", stockSymbol.trim(), "?range=", timeRange.toString(), WebConstants.UTF_CODE_AMPERSAND,
                "frequency=", frequency);

        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("jsonDataUrl", jsonDataUrl);
        modelAndView.addObject("stockName", stock.getName());
        modelAndView.addObject("isin", stock.getIsin());
        modelAndView.addObject("wkn", stock.getWkn());
        modelAndView.setViewName("/chart.html");

        return modelAndView;
    }

}
