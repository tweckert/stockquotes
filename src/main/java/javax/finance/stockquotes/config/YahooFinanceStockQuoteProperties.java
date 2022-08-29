package javax.finance.stockquotes.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "quotes.yahoo-finance")
public class YahooFinanceStockQuoteProperties {

    private List<DownloadProperties> downloads;
    private List<ImportProperties> imports;

    public List<DownloadProperties> getDownloads() {
        return downloads;
    }

    public void setDownloads(final List<DownloadProperties> downloads) {
        this.downloads = downloads;
    }

    public List<ImportProperties> getImports() {
        return imports;
    }

    public void setImports(final List<ImportProperties> imports) {
        this.imports = imports;
    }

    @Component
    @ConfigurationProperties(prefix = "quotes.yahoo-finance.downloads")
    public static class DownloadProperties {

        private String url;
        private String file;

        public String getUrl() {
            return url;
        }

        public void setUrl(final String url) {
            this.url = url;
        }

        public String getFile() {
            return file;
        }

        public void setFile(final String file) {
            this.file = file;
        }
    }

    @Component
    @ConfigurationProperties(prefix = "quotes.yahoo-finance.imports")
    public static class ImportProperties {

        private String isin;
        private String wkn;
        private String name;
        private String file;

        public String getIsin() {
            return isin;
        }

        public void setIsin(final String isin) {
            this.isin = isin;
        }

        public String getWkn() {
            return wkn;
        }

        public void setWkn(final String wkn) {
            this.wkn = wkn;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getFile() {
            return file;
        }

        public void setFile(final String file) {
            this.file = file;
        }
    }

}
