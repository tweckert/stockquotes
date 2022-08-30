package javax.finance.stockquotes.yahoo.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import javax.finance.stockquotes.data.entity.Stock;
import javax.finance.stockquotes.data.entity.StockQuote;
import javax.finance.stockquotes.data.repository.StockQuoteRepository;
import javax.finance.stockquotes.service.ImportService;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;

@Service
public class YahooImportService implements ImportService {

    private static final Logger LOG = LoggerFactory.getLogger(YahooImportService.class);

    private final StockQuoteRepository stockQuoteRepository;
    private final Converter<CSVRecord, StockQuote> stockQuoteConverter;

    @Autowired
    public YahooImportService(final StockQuoteRepository stockQuoteRepository,
                              final Converter<CSVRecord, StockQuote> stockQuoteConverter) {
        this.stockQuoteRepository = stockQuoteRepository;
        this.stockQuoteConverter = stockQuoteConverter;
    }

    @Override
    public void importHistoricalData(final Stock stock, final File file) {

        if (file == null || stock == null || stock.getId() == null || stock.getId().longValue() == 0) {
            return;
        }

        try (final Reader reader = new FileReader(file)) {

            final Iterable<CSVRecord> csvRecords =
                    CSVFormat.DEFAULT.builder()
                            .setHeader()
                            .setSkipHeaderRecord(true)
                            .build()
                            .parse(reader);

            int count = 0;
            for (final CSVRecord csvRecord : csvRecords) {

                final StockQuote stockQuote = stockQuoteConverter.convert(csvRecord);

                if (stockQuote == null) {
                    continue;
                }

                stockQuote.setStock(stock);

                stockQuoteRepository.save(stockQuote);
                count++;
            }

            if (LOG.isInfoEnabled()) {
                LOG.info(StringUtils.join("Imported ", count, " stock quotes for stock '", stock.getWkn(), "'"));
            }
        } catch (final Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error(StringUtils.join("Error importing historical stock data from file '",
                        file.getAbsolutePath(), "': ", e.getMessage()), e);
            }
        }
    }

}
