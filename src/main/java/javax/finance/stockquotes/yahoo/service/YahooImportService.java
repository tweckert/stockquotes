package javax.finance.stockquotes.yahoo.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import javax.finance.stockquotes.data.entity.Frequency;
import javax.finance.stockquotes.data.entity.Stock;
import javax.finance.stockquotes.data.entity.StockQuote;
import javax.finance.stockquotes.data.repository.StockQuoteRepository;
import javax.finance.stockquotes.service.impl.AbstractImportService;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class YahooImportService extends AbstractImportService {

    private static final Logger LOG = LoggerFactory.getLogger(YahooImportService.class);

    private final Converter<CSVRecord, StockQuote> stockQuoteConverter;

    @Autowired
    public YahooImportService(final StockQuoteRepository stockQuoteRepository,
                              final Converter<CSVRecord, StockQuote> stockQuoteConverter) {
        super(stockQuoteRepository);
        this.stockQuoteConverter = stockQuoteConverter;
    }

    @Override
    public void importHistoricalData(final Stock stock, final Frequency frequency, final File file) {

        if (file == null || stock == null || stock.getId() == null || stock.getId().longValue() == 0) {
            return;
        }

        try (final Reader reader = new FileReader(file)) {

            final Iterable<CSVRecord> csvRecordsIterable =
                    CSVFormat.DEFAULT.builder()
                            .setHeader()
                            .setSkipHeaderRecord(true)
                            .build()
                            .parse(reader);

            final List<CSVRecord> csvRecordList =
                    StreamSupport.stream(csvRecordsIterable.spliterator(), false).collect(Collectors.toList());

            if (CollectionUtils.isEmpty(csvRecordList)) {
                return;
            }

            final List<StockQuote> stockQuoteList = new ArrayList<>();
            for (final CSVRecord csvRecord : csvRecordList) {

                final StockQuote stockQuote = stockQuoteConverter.convert(csvRecord);
                if (stockQuote == null) {

                    if (LOG.isErrorEnabled()) {
                        LOG.error(StringUtils.join("Unable to import CSV record #",
                                csvRecord.getRecordNumber(), " from import file '",
                                file.getName(),"': ", csvRecord.toString()));
                    }

                    continue;
                }

                stockQuote.setStock(stock);
                stockQuote.setFrequency(frequency);

                stockQuoteList.add(stockQuote);
            }

            importStockQuotes(stock, stockQuoteList, frequency);
        } catch (final Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error(StringUtils.join("Error importing stock data from file '",
                        file.getAbsolutePath(), "': ", e.getMessage()), e);
            }
        }
    }

}
