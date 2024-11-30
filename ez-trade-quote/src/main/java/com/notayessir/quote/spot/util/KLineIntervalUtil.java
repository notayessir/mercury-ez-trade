package com.notayessir.quote.spot.util;


import com.notayessir.quote.spot.constant.EnumKLineInterval;

import java.time.*;
import java.time.temporal.TemporalAdjusters;

public class KLineIntervalUtil {


    public static long calcTimestamp(long timestamp, EnumKLineInterval interval){
        return switch (interval) {
            case ONE_SECOND -> calcTimestampBySecond(timestamp);
            case ONE_MINUTE, FIFTEEN_MINUTE, THIRTY_MINUTE, ONE_HOUR, SIX_HOUR, ONE_DAY ->
                    calcTimestampByMinute(timestamp, interval.getValue() / 60);
            case ONE_WEEK -> calcTimestampByWeek(timestamp);
            case ONE_MONTH -> calcTimestampByMonth(timestamp);
            case ONE_YEAR -> calcTimestampByYear(timestamp);
        };
    }

    private static long calcTimestampBySecond(long timestamp) {
        return timestamp / 1000;
    }


    public static long calcTimestampByYear(long timestamp){
        Instant instant = Instant.ofEpochSecond(timestamp);
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());

        ZonedDateTime startOfYear = dateTime.with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
//        ZonedDateTime endOfYear = dateTime.with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);

        return startOfYear.toEpochSecond();
    }

    public static long calcTimestampByMonth(long timestamp){
        Instant instant = Instant.ofEpochSecond(timestamp);
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());

        ZonedDateTime startOfMonth = dateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
//        ZonedDateTime endOfMonth = dateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);

        return startOfMonth.toEpochSecond();
    }

    public static long calcTimestampByWeek(long timestamp){
        Instant instant = Instant.ofEpochSecond(timestamp);
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());

        ZonedDateTime startOfWeek = dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).with(LocalTime.MIN);
//        ZonedDateTime endOfWeek = dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MAX);

        return startOfWeek.toEpochSecond();

    }


    public static long calcTimestampByMinute(long timestamp, int interval){
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());

        // start of interval
        long minutes = dateTime.toLocalTime().toSecondOfDay() / 60;
        long startMinutes = (minutes / interval) * interval;
        ZonedDateTime startOfMinute = dateTime.with(LocalTime.ofSecondOfDay(startMinutes * 60));
//        ZonedDateTime endOfMinute = startOfInterval.plusMinutes(interval);

        return startOfMinute.toEpochSecond();
    }

    public static void main(String[] args) {
        long l = calcTimestampBySecond(System.currentTimeMillis());
        System.out.println(l);
    }



}
