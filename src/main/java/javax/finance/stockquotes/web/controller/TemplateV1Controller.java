package javax.finance.stockquotes.web.controller;

import com.google.visualization.datasource.datatable.DataTable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
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
public class TemplateV1Controller extends AbstractController {

    private static final Logger LOG = LoggerFactory.getLogger(TemplateV1Controller.class);

    private final StockService stockService;

    @Autowired
    public TemplateV1Controller(final StockService stockService,
                                final ChartFacade<OhlcChartDto> ohlcChartFacade,
                                final ChartFacade<DataTable> dataTableChartFacade) {
        super(ohlcChartFacade, dataTableChartFacade);
        this.stockService = stockService;
    }

    @GetMapping
    public String index() {
        return "/index.html";
    }

    @GetMapping(value = "/chart/v1/{stockSymbol}")
    public ModelAndView chart(@PathVariable(required = true) final String stockSymbol,
                              @RequestParam(name = "range", required = false) final String timeRangeName,
                              @RequestParam(name = "frequency", required = false) final String frequencyName) {

        final ModelAndView modelAndView = new ModelAndView();

        try {

            final Stock stock = stockService.findStock(stockSymbol);
            if (stock == null) {

                modelAndView.addObject("stockSymbol", String.valueOf(stockSymbol));
                modelAndView.setViewName("/v1/notfound");
                modelAndView.setStatus(HttpStatus.NOT_FOUND);
            } else {

                final TimeRange timeRange = TimeRange.of(timeRangeName);
                final Frequency frequency = getFrequency(timeRange, frequencyName);
                final String jsonDataUrl = StringUtils.join(WebConstants.API_PATH_PREFIX_V1, WebConstants.API_PATH_DATATABLE,
                        "/", stockSymbol.trim(), "?range=", timeRange.toString(), WebConstants.UTF_CODE_AMPERSAND,
                        "frequency=", frequency);

                modelAndView.addObject("jsonDataUrl", jsonDataUrl);
                modelAndView.addObject("stockName", stock.getName());
                modelAndView.addObject("isin", stock.getIsin());
                modelAndView.addObject("wkn", stock.getWkn());
                modelAndView.addObject("locale", LocaleContextHolder.getLocale().toString());
                modelAndView.setViewName("/v1/chart");
                modelAndView.setStatus(HttpStatus.OK);
            }
        } catch (final Exception e) {

            if (LOG.isErrorEnabled()) {
                LOG.error(e.getMessage(), e);
            }

            modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return modelAndView;
    }

}
