package javax.finance.stockquotes.web.controller;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.finance.stockquotes.constant.StockQuotesApplicationConstants;
import javax.finance.stockquotes.persistence.entity.Frequency;
import javax.finance.stockquotes.web.constant.WebConstants;
import javax.finance.stockquotes.web.converter.OhlcCsvConverter;
import javax.finance.stockquotes.web.dto.OhlcChartDto;
import javax.finance.stockquotes.web.dto.OhlcDto;
import javax.finance.stockquotes.web.facade.ChartFacade;
import javax.finance.stockquotes.web.facade.TimeRange;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class AbstractController {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractController.class);

    private final ChartFacade chartFacade;
    private final Converter<OhlcDto, List<String>> ohlcCsvConverter;

    public AbstractController(final ChartFacade chartFacade,
                              final Converter<OhlcDto, List<String>> ohlcCsvConverter) {
        this.chartFacade = chartFacade;
        this.ohlcCsvConverter = ohlcCsvConverter;
    }

    protected ChartFacade getChartFacade() {
        return chartFacade;
    }

    protected OhlcChartDto ohlcChart(final boolean stockSymbolIsWkn, final String stockSymbol,
                                     final String timeRangeName, final String frequencyName) {

        final TimeRange timeRange = TimeRange.of(timeRangeName);
        final Frequency frequency = chartFacade.getFrequency(timeRange, frequencyName);

        return stockSymbolIsWkn
                ? chartFacade.selectOhlcChartByWkn(stockSymbol, timeRange, frequency)
                : chartFacade.selectOhlcChartByIsin(stockSymbol, timeRange, frequency);
    }

    protected ResponseEntity<OhlcChartDto> ohlcChartDtoResponse(final boolean stockSymbolIsWkn, final String stockSymbol,
                                                                final String timeRangeName, final String frequencyName) {

        final OhlcChartDto ohlcChartDto = ohlcChart(stockSymbolIsWkn, stockSymbol, timeRangeName, frequencyName);

        return ohlcChartDto != null
                ? ResponseEntity.ok(ohlcChartDto)
                : ResponseEntity.notFound().build();
    }

    protected ResponseEntity<Resource> ohlcChartResourceResponse(final boolean stockSymbolIsWkn, final String stockSymbol,
                                                                 final String timeRangeName, final String frequencyName) {

        final OhlcChartDto ohlcChartDto = ohlcChart(stockSymbolIsWkn, stockSymbol, timeRangeName, frequencyName);
        if (ohlcChartDto == null) {
            return ResponseEntity.notFound().build();
        }

        try (final ByteArrayOutputStream out = new ByteArrayOutputStream();
             final PrintWriter printWriter = new PrintWriter(out);
             final CSVPrinter csvPrinter = new CSVPrinter(printWriter, CSVFormat.DEFAULT.builder().setHeader(OhlcCsvConverter.CSV_HEADER).build())) {

            for (final OhlcDto ohlcDto : ohlcChartDto.getOhlc()) {
                csvPrinter.printRecord(ohlcCsvConverter.convert(ohlcDto));
            }

            csvPrinter.flush();

            final ByteArrayInputStream byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
            final InputStreamResource fileInputStream = new InputStreamResource(byteArrayOutputStream);
            final String csvFilename = buildCsvFilename(stockSymbolIsWkn, ohlcChartDto);

            final HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + csvFilename);
            headers.set(HttpHeaders.CONTENT_TYPE, WebConstants.MEDIA_TYPE_APPLICATION_CSV);

            return new ResponseEntity<>(fileInputStream, headers, HttpStatus.OK);
        } catch (final Exception e) {

            if (LOG.isErrorEnabled()) {
                LOG.error(e.getMessage(), e);
            }

            return ResponseEntity.internalServerError().build();
        }
    }

    protected String buildCsvFilename(final boolean stockSymbolIsWkn, final OhlcChartDto ohlcChartDto) {

        final SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(StockQuotesApplicationConstants.ISO8601_DATE_FORMAT);

        final StringBuffer buf = new StringBuffer();
        buf.append(stockSymbolIsWkn ? ohlcChartDto.getWkn().toUpperCase() : ohlcChartDto.getIsin().toUpperCase());
        buf.append("-");
        buf.append(ohlcChartDto.getRange().toString().toUpperCase());
        buf.append("-");
        buf.append(ohlcChartDto.getFrequency().toString().toUpperCase());
        buf.append("-");
        buf.append(simpleDateFormat.format(new Date()));
        buf.append(".csv");

        return buf.toString();
    }

}
