package javax.finance.stockquotes.service;

public interface CronCalculator {

    long nextExecutionTimeMillis();

    void calculate(final String cron);

    boolean isEnabled(final String cron);

}
