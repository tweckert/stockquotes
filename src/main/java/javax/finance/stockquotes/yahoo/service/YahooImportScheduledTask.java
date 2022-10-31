package javax.finance.stockquotes.yahoo.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.finance.stockquotes.config.QuotesConfigurationProperties;
import javax.finance.stockquotes.persistence.entity.Frequency;
import javax.finance.stockquotes.persistence.entity.Stock;
import javax.finance.stockquotes.persistence.repository.StockRepository;
import javax.finance.stockquotes.service.ImportService;
import javax.finance.stockquotes.service.ScheduledTask;
import javax.finance.stockquotes.yahoo.config.YahooConfigurationProperties;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class YahooImportScheduledTask implements ScheduledTask, InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(YahooImportScheduledTask.class);

    private final YahooConfigurationProperties yahooConfigurationProperties;
    private final Map<String, YahooConfigurationProperties.ImportProperties> importPropertiesByFilename;
    private final Map<String, QuotesConfigurationProperties.StockProperties> stockPropertiesByWkn;
    private final StockRepository stockRepository;
    private final ImportService importService;
    private final Counter errorImportCounter;
    private final Counter successImportCounter;

    @Autowired
    public YahooImportScheduledTask(final YahooConfigurationProperties yahooConfigurationProperties,
                                    final QuotesConfigurationProperties quotesConfigurationProperties,
                                    final StockRepository stockRepository,
                                    final ImportService importService,
                                    final MeterRegistry meterRegistry) {
        this.importPropertiesByFilename = yahooConfigurationProperties.getImportPropertiesByFilename();
        this.stockPropertiesByWkn = quotesConfigurationProperties.getStockPropertiesByWkn();
        this.yahooConfigurationProperties = yahooConfigurationProperties;
        this.stockRepository = stockRepository;
        this.importService = importService;
        this.errorImportCounter = meterRegistry.counter("yahoo_import_error_count");
        this.successImportCounter = meterRegistry.counter("yahoo_import_success_count");
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        FileUtils.forceMkdir(yahooConfigurationProperties.getWorkDir());

        if (LOG.isInfoEnabled()) {
            LOG.info(StringUtils.join("Yahoo work directory is set to: '", yahooConfigurationProperties.getWorkDir(), "'"));
        }
    }

    @Override
    public void logNextExecution(final String cron) {
        // intentionally left blank
    }

    @Override
    @Scheduled(fixedDelay = 1000)
    public void execute() {

        final Collection<File> importFiles = FileUtils.listFiles(yahooConfigurationProperties.getWorkDir(), new String[]{"csv"}, false);
        for (final File importFile : importFiles) {

            try {

                final String filename = importFile.getName();

                final YahooConfigurationProperties.ImportProperties importProperties = importPropertiesByFilename.get(filename);
                if (importProperties == null) {

                    if (LOG.isErrorEnabled()) {
                        LOG.error(StringUtils.join("No import properties found for import file '", filename, "'"));
                    }

                    continue;
                }

                final String wkn = importProperties.getWkn();

                final QuotesConfigurationProperties.StockProperties stockProperties = stockPropertiesByWkn.get(wkn);
                if (stockProperties == null) {

                    if (LOG.isErrorEnabled()) {
                        LOG.error(StringUtils.join("No stock properties found for WKN '", wkn, "'"));
                    }

                    continue;
                }

                final String isin = stockProperties.getIsin();
                final String name = stockProperties.getName();
                final Frequency frequency = importProperties.getFrequency();

                final boolean success = importStockQuotes(isin, wkn, name, importFile, frequency);

                if (!success) {
                    errorImportCounter.increment();
                } else {
                    successImportCounter.increment();
                }
            } finally {
                FileUtils.deleteQuietly(importFile);
            }
        }
    }

    protected boolean importStockQuotes(final String isin, final String wkn, final String name,
                                        final File importFile, final Frequency frequency) {

        if (LOG.isInfoEnabled()) {
            LOG.info(StringUtils.join("Importing stock quotes from import file '",
                    importFile.getAbsolutePath(), "'"));
        }

        final Stock stock = createStockIfAbsent(isin, wkn, name);
        return importService.importHistoricalData(stock, frequency, importFile);
    }

    protected Stock createStockIfAbsent(final String isin, final String wkn, final String name) {

        final Stock stock = new Stock();
        stock.setIsin(isin.trim().toUpperCase());
        stock.setWkn(wkn.trim().toUpperCase());
        stock.setName(name.trim());

        final List<Stock> existingStocksList = stockRepository.findByWkn(wkn);

        return CollectionUtils.isNotEmpty(existingStocksList)
                ? existingStocksList.get(0)
                : stockRepository.save(stock);
    }

}
