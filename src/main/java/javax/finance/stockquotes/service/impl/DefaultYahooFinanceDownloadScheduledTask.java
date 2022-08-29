package javax.finance.stockquotes.service.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.finance.stockquotes.config.YahooFinanceStockQuoteProperties;
import javax.finance.stockquotes.service.DownloadService;
import javax.finance.stockquotes.service.ScheduledTask;
import java.io.File;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service("yahooFinanceDownloadService")
public class DefaultYahooFinanceDownloadScheduledTask implements ScheduledTask, InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultYahooFinanceDownloadScheduledTask.class);
    private static final String DOWNLOAD_POSTFIX = ".download";

    private final DownloadService downloadService;
    private final YahooFinanceStockQuoteProperties stockQuoteProperties;
    private final File workDir;

    @Autowired
    public DefaultYahooFinanceDownloadScheduledTask(final DownloadService downloadService,
                                                    final YahooFinanceStockQuoteProperties stockQuoteProperties,
                                                    @Value("${quotes.yahoo-finance.work-dir}") final String workDir) {
        this.downloadService = downloadService;
        this.stockQuoteProperties = stockQuoteProperties;
        this.workDir = new File(workDir.trim());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        FileUtils.createParentDirectories(workDir);
    }

    @Scheduled(cron = "${quotes.yahoo-finance.cron}")
    @Override
    public void execute() {

        final LocalDateTime endTime = LocalDateTime.now(ZoneOffset.UTC);
        final LocalDateTime startTime = endTime.minusYears(10);

        for (final YahooFinanceStockQuoteProperties.DownloadProperties downloadProperties : stockQuoteProperties.getDownloads()) {

            final String sourceUrl = downloadProperties.getUrl();
            final String destinationFile = downloadProperties.getFile();

            downloadStockQuotes(startTime, endTime, sourceUrl, destinationFile);
        }
    }

    protected void downloadStockQuotes(final LocalDateTime startTime, final LocalDateTime endTime,
                                       final String sourceUrl, final String destinationFilename) {

        final String formattedSourceUrl =
                MessageFormat.format(sourceUrl,
                        Long.toString(startTime.toEpochSecond(ZoneOffset.UTC)),
                        Long.toString(endTime.toEpochSecond(ZoneOffset.UTC)));

        try {

            final String downloadFilename = destinationFilename.trim().concat(DOWNLOAD_POSTFIX);
            final File downloadFile = new File(workDir, downloadFilename);
            if (downloadFile.exists()) {

                if (LOG.isInfoEnabled()) {
                    LOG.info(StringUtils.join("Download file '", downloadFilename, "' already exists, skipping download"));
                }

                return;
            }

            final File sourceFile = downloadService.fetchUrl(formattedSourceUrl, downloadFile.getAbsolutePath());
            final File destinationFile = new File(workDir, destinationFilename.trim());

            FileUtils.moveFile(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);

            if (LOG.isInfoEnabled()) {
                LOG.info(StringUtils.join("Moved download file '", downloadFilename,
                        "' to import file '", destinationFilename, "' in work dir '", workDir.getAbsolutePath(), "'"));
            }
        } catch (final Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error(StringUtils.join("Error downloading file '", destinationFilename,
                        "' from URL '", formattedSourceUrl, "': ", e.getMessage()), e);
            }
        }
    }

}
