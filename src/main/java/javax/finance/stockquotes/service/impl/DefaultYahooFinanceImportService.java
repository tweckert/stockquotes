package javax.finance.stockquotes.service.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.finance.stockquotes.data.entity.Stock;
import javax.finance.stockquotes.data.entity.StockQuote;
import javax.finance.stockquotes.service.ImportService;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;

@Service
public class DefaultYahooFinanceImportService implements ImportService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultYahooFinanceImportService.class);

    private final CrudRepository<StockQuote, Long> stockQuoteRepository;
    private final Converter<CSVRecord, StockQuote> csvRecordToStockQuoteConverter;

    @Autowired
    public DefaultYahooFinanceImportService(final CrudRepository<StockQuote, Long> stockQuoteRepository,
                                            final Converter<CSVRecord, StockQuote> csvRecordToStockQuoteConverter) {
        this.stockQuoteRepository = stockQuoteRepository;
        this.csvRecordToStockQuoteConverter = csvRecordToStockQuoteConverter;
    }

    @Override
    public void importHistoricalData(final Stock stock, final File file) {

        if (file == null || stock == null || stock.getId() == null || stock.getId().longValue() == 0) {
            return;
        }

        Reader reader = null;
        try {

            reader = new FileReader(file);

            final Iterable<CSVRecord> csvRecords =
                    CSVFormat.DEFAULT.builder()
                            .setHeader()
                            .setSkipHeaderRecord(true)
                            .build()
                            .parse(reader);

            for (CSVRecord csvRecord : csvRecords) {

                final StockQuote stockQuote = csvRecordToStockQuoteConverter.convert(csvRecord);

                if (stockQuote == null) {
                    continue;
                }

                stockQuote.setStock(stock);

                stockQuoteRepository.save(stockQuote);
            }
        } catch (final Exception e) {

            if (LOG.isErrorEnabled()) {
                LOG.error(StringUtils.join("Error importing historical stock data from file '",
                        file.getAbsolutePath(), "': ", e.getMessage()), e);
            }

            IOUtils.closeQuietly(reader);
        }
    }

}
