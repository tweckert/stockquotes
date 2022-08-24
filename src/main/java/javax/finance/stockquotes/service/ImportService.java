package javax.finance.stockquotes.service;

import javax.finance.stockquotes.data.entity.Stock;
import java.io.File;

public interface ImportService {

    void importHistoricalData(final Stock stock, final File file);

}
