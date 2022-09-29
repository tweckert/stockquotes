package javax.finance.stockquotes.web.controller;

import com.google.visualization.datasource.datatable.DataTable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.finance.stockquotes.persistence.entity.Frequency;
import javax.finance.stockquotes.web.dto.ApiErrorDto;
import javax.finance.stockquotes.web.dto.OhlcChartDto;
import javax.finance.stockquotes.web.facade.ChartFacade;
import javax.finance.stockquotes.web.facade.TimeRange;
import java.util.Collections;
import java.util.Date;

public abstract class AbstractController {

    private final ChartFacade<OhlcChartDto> ohlcChartFacade;
    private final ChartFacade<DataTable> dataTableChartFacade;

    protected AbstractController(final ChartFacade<OhlcChartDto> ohlcChartFacade,
                                 final ChartFacade<DataTable> dataTableChartFacade) {
        this.ohlcChartFacade = ohlcChartFacade;
        this.dataTableChartFacade = dataTableChartFacade;
    }

    protected OhlcChartDto getOhlcChartDto(final String stockSymbol, final String timeRangeName, final String frequencyName) {

        final TimeRange timeRange = TimeRange.of(timeRangeName);
        final Frequency frequency = getFrequency(timeRange, frequencyName);

        return ohlcChartFacade.selectStockQuoteData(stockSymbol, timeRange, frequency);
    }

    protected DataTable dataTableResponse(final String stockSymbol, final String timeRangeName, final String frequencyName) {

        final TimeRange timeRange = TimeRange.of(timeRangeName);
        final Frequency frequency = getFrequency(timeRange, frequencyName);

        return dataTableChartFacade.selectStockQuoteData(stockSymbol, timeRange, frequency);
    }

    protected Frequency getFrequency(final TimeRange timeRange, final String frequencyName) {
        return TimeRange.WEEK.equals(timeRange)
                ? Frequency.DAILY
                : Frequency.of(frequencyName);
    }

    protected ResponseEntity notFoundResponseEntity(final String stockSymbol,
                                                    final String timeRangeName,
                                                    final String frequencyName) {

        final ApiErrorDto apiErrorDto = new ApiErrorDto(HttpStatus.NOT_FOUND, new Date(),
                StringUtils.join("No data found for stock symbol '", String.valueOf(stockSymbol), "'",
                        "', time range '", String.valueOf(timeRangeName),
                        "' and frequency '", String.valueOf(frequencyName), "'"),
                StringUtils.EMPTY, Collections.emptyList());

        return new ResponseEntity(apiErrorDto, HttpStatus.NOT_FOUND);
    }

    protected ResponseEntity errorResponseEntity(final Throwable cause,
                                                 final String stockSymbol,
                                                 final String timeRangeName,
                                                 final String frequencyName) {

        final String debugMessage = cause != null ? cause.getMessage() : null;

        final ApiErrorDto apiErrorDto = new ApiErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, new Date(),
                StringUtils.join("Error getting data for stock symbol '", String.valueOf(stockSymbol), "'",
                        "', time range '", String.valueOf(timeRangeName),
                        "' and frequency '", String.valueOf(frequencyName), "'"),
                debugMessage, Collections.emptyList());

        return new ResponseEntity(apiErrorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
