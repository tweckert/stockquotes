package javax.finance.stockquotes.yahoo.config;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.finance.stockquotes.persistence.entity.Frequency;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@ConfigurationProperties(prefix = "quotes.yahoo")
public class YahooConfigurationProperties {

    private List<DownloadProperties> downloads;
    private List<ImportProperties> imports;
    private File workDir;

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

    public Map<String, ImportProperties> getImportPropertiesByFilename() {
        return CollectionUtils.isNotEmpty(imports)
                ? imports.stream().collect(Collectors.collectingAndThen(Collectors.toMap(ImportProperties::getFile, Function.identity()), Collections::unmodifiableMap))
                : Collections.emptyMap();
    }

    public File getWorkDir() {
        return workDir;
    }

    public void setWorkDir(final String workDir) {
        this.workDir = new File(workDir.trim());
    }

    @Component
    @ConfigurationProperties(prefix = "quotes.yahoo.downloads")
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
    @ConfigurationProperties(prefix = "quotes.yahoo.imports")
    public static class ImportProperties {

        private String wkn;
        private String file;
        private Frequency frequency;

        public String getWkn() {
            return StringUtils.isNotBlank(wkn) ? wkn.toUpperCase() : wkn;
        }

        public void setWkn(final String wkn) {
            this.wkn = wkn;
        }

        public String getFile() {
            return file;
        }

        public void setFile(final String file) {
            this.file = file;
        }

        public Frequency getFrequency() {
            return frequency;
        }

        public void setFrequency(final String frequencyName) {
            this.frequency = Frequency.of(frequencyName);
        }
    }

}
