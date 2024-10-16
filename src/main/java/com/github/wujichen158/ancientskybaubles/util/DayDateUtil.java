package com.github.wujichen158.ancientskybaubles.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class DayDateUtil {
    public static int dateToDays(LocalDate date) {
        LocalDate epoch = LocalDate.of(1970, 1, 1);
        return (int) ChronoUnit.DAYS.between(epoch, date);
    }

    public static LocalDate daysToDate(int daysSinceEpoch) {
        LocalDate epoch = LocalDate.of(1970, 1, 1);
        return epoch.plusDays(daysSinceEpoch);
    }

    public static int dateToMinutes(LocalDateTime dateTime) {
        return (int) dateTime.toEpochSecond(ZoneOffset.UTC) / 60;
    }

    public static LocalDateTime minutesToDate(int minutesSinceEpoch) {
        long seconds = minutesSinceEpoch * 60L;
        return LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC);
    }
}
