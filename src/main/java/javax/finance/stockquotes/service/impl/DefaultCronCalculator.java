package javax.finance.stockquotes.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

import javax.finance.stockquotes.service.CronCalculator;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class DefaultCronCalculator implements CronCalculator {

    private static final String DISABLED_CRON_EXPR = "-";

    private long nextExecutionTimeMillis;

    public DefaultCronCalculator() {
        this.nextExecutionTimeMillis = Long.MAX_VALUE;
    }

    @Override
    public long nextExecutionTimeMillis() {
        return nextExecutionTimeMillis;
    }

    @Override
    public void calculate(final String cron) {

        if (!isEnabled(cron)) {
            nextExecutionTimeMillis = Long.MAX_VALUE;
            return;
        }

        final CronExpression cronExpression = CronExpression.parse(cron);
        final LocalDateTime nextExecutionLocalDateTime = cronExpression.next(LocalDateTime.now(ZoneId.systemDefault()));

        nextExecutionTimeMillis = nextExecutionLocalDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @Override
    public boolean isEnabled(final String cron) {
        return !(StringUtils.isBlank(cron) || DISABLED_CRON_EXPR.equalsIgnoreCase(cron.trim()));
    }

}
