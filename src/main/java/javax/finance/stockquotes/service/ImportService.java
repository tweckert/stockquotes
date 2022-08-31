package javax.finance.stockquotes.service;

import javax.finance.stockquotes.data.entity.Frequency;
import javax.finance.stockquotes.data.entity.Stock;
import java.io.File;

public interface ImportService {

    void importHistoricalData(final Stock stock, final Frequency frequency, final File file);

}
