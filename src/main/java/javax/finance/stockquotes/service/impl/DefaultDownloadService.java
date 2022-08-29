package javax.finance.stockquotes.service.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.finance.stockquotes.service.DownloadService;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@Service
public class DefaultDownloadService implements DownloadService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultDownloadService.class);

    private final int connectTimeoutMillis;
    private final int readTimeoutMillis;

    @Autowired
    public DefaultDownloadService(@Value("${quotes.connect-timeout-millis}") final int connectTimeoutMillis,
                                  @Value("${quotes.read-timeout-millis}") final int readTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
    }

    @Override
    public File fetchUrl(final String sourceUrl, final String destinationFile) {

        try {

            if (LOG.isInfoEnabled()) {
                LOG.info(StringUtils.join("Downloading URL '", sourceUrl, "' to file '", destinationFile, "'"));
            }

            final File file = new File(destinationFile);
            final URL url = new URL(sourceUrl);

            FileUtils.createParentDirectories(file);
            FileUtils.deleteQuietly(file);
            FileUtils.copyURLToFile(url, file, connectTimeoutMillis, readTimeoutMillis);

            return file;
        } catch (final IOException e) {

            if (LOG.isErrorEnabled()) {
                LOG.error(StringUtils.join("Error downloading file from URL '", String.valueOf(sourceUrl),
                        "': ", e.getMessage()), e);
            }

            return null;
        }
    }
}
