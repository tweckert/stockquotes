package javax.finance.stockquotes.service.impl;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.finance.stockquotes.service.CronCalculator;
import javax.finance.stockquotes.service.ScheduledTask;

public abstract class AbstractScheduledTask implements ScheduledTask {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractScheduledTask.class);
    private static final FastDateFormat TIME_ZONED_DATE_TIME_FORMAT =
            FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss a z");

    private final CronCalculator cronCalculator;

    protected AbstractScheduledTask(final String nextExecutionMillisGaugeName,
                                    final CronCalculator cronCalculator,
                                    final MeterRegistry meterRegistry) {
        this.cronCalculator = cronCalculator;
        Gauge.builder(nextExecutionMillisGaugeName, cronCalculator, CronCalculator::nextExecutionTimeMillis).register(meterRegistry);
    }

    @Override
    public void logNextExecution(final String cron) {

        if (!cronCalculator.isEnabled(cron)) {
            return;
        }

        cronCalculator.calculate(cron);
        final long nextExecutionMillis = cronCalculator.nextExecutionTimeMillis();
        final String nextExecution = TIME_ZONED_DATE_TIME_FORMAT.format(nextExecutionMillis);

        if (LOG.isInfoEnabled()) {
            LOG.info(StringUtils.join("Next execution of task '",
                    this.getClass().getSimpleName(), "' is scheduled at: ", nextExecution));
        }

    }

}
