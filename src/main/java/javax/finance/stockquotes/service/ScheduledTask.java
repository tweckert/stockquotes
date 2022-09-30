package javax.finance.stockquotes.service;

public interface ScheduledTask {

    void execute();

    void logNextExecution(final String cron);

}
