package javax.finance.stockquotes.service;

import javax.finance.stockquotes.persistence.entity.Frequency;
import javax.finance.stockquotes.persistence.entity.Stock;
import java.io.File;

public interface ImportService {

    boolean importHistoricalData(final Stock stock, final Frequency frequency, final File file);

}
