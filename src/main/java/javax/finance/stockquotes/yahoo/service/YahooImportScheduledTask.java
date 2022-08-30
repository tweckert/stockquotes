package javax.finance.stockquotes.yahoo.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.finance.stockquotes.data.entity.Stock;
import javax.finance.stockquotes.data.repository.StockQuoteRepository;
import javax.finance.stockquotes.data.repository.StockRepository;
import javax.finance.stockquotes.service.ImportService;
import javax.finance.stockquotes.service.ScheduledTask;
import javax.finance.stockquotes.yahoo.config.YahooConfigurationProperties;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class YahooImportScheduledTask implements ScheduledTask, InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(YahooImportScheduledTask.class);

    private final YahooConfigurationProperties yahooConfigurationProperties;
    private final Map<String, YahooConfigurationProperties.ImportProperties> importPropertiesByWkn;
    private final StockQuoteRepository stockQuoteRepository;
    private final StockRepository stockRepository;
    private final ImportService importService;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public YahooImportScheduledTask(final YahooConfigurationProperties yahooConfigurationProperties,
                                    final StockQuoteRepository stockQuoteRepository,
                                    final StockRepository stockRepository,
                                    final ImportService importService,
                                    final PlatformTransactionManager platformTransactionManager) {
        this.importPropertiesByWkn = new HashMap<>();
        this.yahooConfigurationProperties = yahooConfigurationProperties;
        this.stockQuoteRepository = stockQuoteRepository;
        this.stockRepository = stockRepository;
        this.importService = importService;
        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        this.importPropertiesByWkn.clear();
        for (final YahooConfigurationProperties.ImportProperties importProperties : yahooConfigurationProperties.getImports()) {
            this.importPropertiesByWkn.put(importProperties.getWkn(), importProperties);
        }

        FileUtils.createParentDirectories(yahooConfigurationProperties.getWorkDir());
    }

    @Override
    @Scheduled(fixedDelay = 1000)
    public void execute() {

        final Collection<File> importFiles = FileUtils.listFiles(yahooConfigurationProperties.getWorkDir(), new String[]{"csv"}, false);
        for (final File importFile : importFiles) {

            try {

                final String wkn = FilenameUtils.getBaseName(importFile.getName());

                final YahooConfigurationProperties.ImportProperties importProperties = importPropertiesByWkn.get(wkn);
                if (importProperties == null) {

                    if (LOG.isErrorEnabled()) {
                        LOG.error(StringUtils.join("No Yahoo Finance import properties found for WKN '", wkn, "'"));
                    }

                    continue;
                }

                final String isin = importProperties.getIsin();
                final String name = importProperties.getName();

                importStockQuotes(isin, wkn, name, importFile);
            } finally {
                FileUtils.deleteQuietly(importFile);
            }
        }
    }

    protected void importStockQuotes(final String isin, final String wkn, final String name, final File importFile) {

        transactionTemplate.execute(status -> {

            if (LOG.isInfoEnabled()) {
                LOG.info(StringUtils.join("Importing stock quotes from import file '", importFile.getAbsolutePath(), "'"));
            }

            final Stock stock = createStockIfAbsent(isin, wkn, name);
            stockQuoteRepository.deleteByStock(stock);
            importService.importHistoricalData(stock, importFile);

            return importFile.getName();
        });
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