package javax.finance.stockquotes.yahoo.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.finance.stockquotes.yahoo.config.YahooConfigurationProperties;
import javax.finance.stockquotes.service.DownloadService;
import javax.finance.stockquotes.service.ScheduledTask;
import java.io.File;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
public class YahooDownloadScheduledTask implements ScheduledTask, InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(YahooDownloadScheduledTask.class);
    private static final String DOWNLOAD_POSTFIX = ".download";

    private final DownloadService downloadService;
    private final YahooConfigurationProperties yahooConfigurationProperties;

    @Autowired
    public YahooDownloadScheduledTask(final DownloadService downloadService,
                                      final YahooConfigurationProperties yahooConfigurationProperties) {
        this.downloadService = downloadService;
        this.yahooConfigurationProperties = yahooConfigurationProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        FileUtils.createParentDirectories(yahooConfigurationProperties.getWorkDir());
    }

    @Scheduled(cron = "${quotes.yahoo.cron}")
    @Override
    public void execute() {

        final LocalDateTime endTime = LocalDateTime.now(ZoneId.systemDefault());
        final LocalDateTime startTime = endTime.minusYears(10);

        for (final YahooConfigurationProperties.DownloadProperties downloadProperties : yahooConfigurationProperties.getDownloads()) {

            final String sourceUrl = downloadProperties.getUrl();
            final String destinationFile = downloadProperties.getFile();

            downloadStockQuotes(startTime, endTime, sourceUrl, destinationFile);
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
