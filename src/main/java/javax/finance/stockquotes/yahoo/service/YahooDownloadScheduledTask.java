package javax.finance.stockquotes.yahoo.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.finance.stockquotes.constant.StockQuotesApplicationConstants;
import javax.finance.stockquotes.service.CronCalculator;
import javax.finance.stockquotes.service.DownloadService;
import javax.finance.stockquotes.service.impl.AbstractScheduledTask;
import javax.finance.stockquotes.yahoo.config.YahooConfigurationProperties;
import java.io.File;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class YahooDownloadScheduledTask extends AbstractScheduledTask implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(YahooDownloadScheduledTask.class);
    private static final String DOWNLOAD_POSTFIX = ".download";

    private final DownloadService downloadService;
    private final YahooConfigurationProperties yahooConfigurationProperties;
    private final String cronExpression;
    private final Counter errorDownloadCounter;
    private final Counter successDownloadCounter;

    @Autowired
    public YahooDownloadScheduledTask(final DownloadService downloadService,
                                      final YahooConfigurationProperties yahooConfigurationProperties,
                                      @Value("${quotes.yahoo.cron}") final String cronExpression,
                                      final CronCalculator cronCalculator,
                                      final MeterRegistry meterRegistry) {
        super("yahoo_download_next_execution_millis", cronCalculator, meterRegistry);
        this.downloadService = downloadService;
        this.yahooConfigurationProperties = yahooConfigurationProperties;
        this.cronExpression = cronExpression;
        this.errorDownloadCounter = Counter.builder("yahoo_download_error_count").register(meterRegistry);
        this.successDownloadCounter = Counter.builder("yahoo_download_success_count").register(meterRegistry);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        FileUtils.createParentDirectories(yahooConfigurationProperties.getWorkDir());
        logNextExecution(cronExpression);
    }

    @Scheduled(cron = "${quotes.yahoo.cron}", zone = StockQuotesApplicationConstants.DEFAULT_TIMEZONE_ID)
    @Override
    public void execute() {

        try {

            final LocalDateTime endTime = LocalDateTime.now(ZoneId.systemDefault());
            final LocalDateTime startTime = endTime.minusYears(10);

            for (final YahooConfigurationProperties.DownloadProperties downloadProperties : yahooConfigurationProperties.getDownloads()) {

                final String sourceUrl = downloadProperties.getUrl();
                final String destinationFile = downloadProperties.getFile();

                downloadStockQuotes(startTime, endTime, sourceUrl, destinationFile);
            }
        } finally {
            logNextExecution(cronExpression);
        }
    }

    protected void downloadStockQuotes(final LocalDateTime startTime, final LocalDateTime endTime,
                                       final String sourceUrl, final String destinationFilename) {

        final String formattedSourceUrl =
                MessageFormat.format(sourceUrl,
                        Long.toString(startTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond()),
                        Long.toString(endTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond()));

        try {

            final File workDir = yahooConfigurationProperties.getWorkDir();
            final String downloadFilename = destinationFilename.trim().concat(DOWNLOAD_POSTFIX);
            final File downloadFile = new File(workDir, downloadFilename);

            if (downloadFile.exists()) {

                if (LOG.isInfoEnabled()) {
                    LOG.info(StringUtils.join("Download file '", downloadFilename, "' already exists, skipping download"));
                }

                return;
            }

            final File sourceFile = downloadService.fetchUrl(formattedSourceUrl, downloadFile.getAbsolutePath());
            if (sourceFile != null) {

                final File destinationFile = new File(workDir, destinationFilename.trim());
                FileUtils.moveFile(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                successDownloadCounter.increment();

                if (LOG.isInfoEnabled()) {
                    LOG.info(StringUtils.join("Moved download file '", downloadFilename,
                            "' to import file '", destinationFilename, "' in work dir '", workDir.getAbsolutePath(), "'"));
                }
            } else {
                errorDownloadCounter.increment();
            }
        } catch (final Exception e) {

            errorDownloadCounter.increment();

            if (LOG.isErrorEnabled()) {
                LOG.error(StringUtils.join("Error downloading file '", destinationFilename,
                        "' from URL '", formattedSourceUrl, "': ", e.getMessage()), e);
            }
        }
    }

}
