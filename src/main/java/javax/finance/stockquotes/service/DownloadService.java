package javax.finance.stockquotes.service;

import java.io.File;

public interface DownloadService {

    File fetchUrl(final String sourceUrl, final String destinationFile);

}
