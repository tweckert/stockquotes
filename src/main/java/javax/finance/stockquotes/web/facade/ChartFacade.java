package javax.finance.stockquotes.web.facade;

import javax.finance.stockquotes.persistence.entity.Frequency;

public interface ChartFacade<DTO> {

    DTO getByWkn(final String wkn, final TimeRange timeRange, final Frequency frequency);

    DTO getByIsin(final String isin, final TimeRange timeRange, final Frequency frequency);

}
