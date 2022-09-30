package javax.finance.stockquotes.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.support.CronExpression;

import javax.finance.stockquotes.service.ScheduledTask;
import java.time.LocalDateTime;
import java.time.ZoneId;

public abstract class AbstractScheduledTask implements ScheduledTask {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractScheduledTask.class);
    private static final FastDateFormat TIME_ZONED_DATE_TIME_FORMAT =
            FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss a z");

    @Override
    public void logNextExecution(final String cron) {

        if (StringUtils.isBlank(cron) || "-".equalsIgnoreCase(cron.trim())) {
            return;
        }

        if (LOG.isInfoEnabled()) {

            final CronExpression cronExpression = CronExpression.parse(cron);
            final LocalDateTime nextExecutionLocalDateTime =
                    cronExpression.next(LocalDateTime.now(ZoneId.systemDefault()));
            final long nextExecutionMillis =
                    nextExecutionLocalDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            final String nextExecution = TIME_ZONED_DATE_TIME_FORMAT.format(nextExecutionMillis);

            LOG.info(StringUtils.join("Next execution of task '",
                    this.getClass().getSimpleName(), "' is scheduled at: ", nextExecution));
        }

    }

}
