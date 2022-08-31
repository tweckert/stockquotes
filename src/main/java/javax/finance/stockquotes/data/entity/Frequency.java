package javax.finance.stockquotes.data.entity;

import org.apache.commons.lang3.StringUtils;

public enum Frequency {

    DAILY,
    WEEKLY,
    MONTHLY;

    public static Frequency of(final String frequencyName) {

        if (StringUtils.isBlank(frequencyName)) {
            return DAILY;
        }

        final String upperCaseFrequencyName = frequencyName.toUpperCase();
        for (final Frequency frequency : Frequency.values()) {
            if (frequency.name().equalsIgnoreCase(upperCaseFrequencyName)) {
                return frequency;
            }
        }

        return DAILY;
    }

}
