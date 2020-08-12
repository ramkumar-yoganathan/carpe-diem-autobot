package com.algo.trading.autobot.carpe.diem.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public final class DateTimeUtils
{
    static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

    public static SimpleDateFormat getDateFormatOfAbsoluteSeconds()
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static SimpleDateFormat getDateFormatOfRelativeSeconds()
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
    }

    public static String getToday()
    {
        return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }

    public static String getCurrentMonthExpiryDate()
    {
        final List<String> allWeeks = getAllExpiryDates();
        return allWeeks.get(allWeeks.size() - 1);
    }

    public static String getThisWeekExpiryDate()
    {
        final Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
            calendar.add(Calendar.DATE, 1);
        }
        calendar.add(Calendar.DATE, -1);
        return dateFormatter.format(calendar.getTime());
    }

    public static String getCurrentWeekExpiryDate()
    {
        final String dateOfWeeklyExpiry = getThisWeekExpiryDate();
        final List<String> allWeeklyExpiryDates = getAllExpiryDates().stream()
            .filter(w -> w.equalsIgnoreCase(dateOfWeeklyExpiry)).collect(Collectors.toList());
        if (!allWeeklyExpiryDates.isEmpty()) {
            return allWeeklyExpiryDates.get(0);
        }

        return "";
    }

    public static List<String> getAllExpiryDates()
    {
        final Calendar calenderInstance = Calendar.getInstance();
        final List<String> allWeeks = new ArrayList<>();
        calenderInstance.set(Calendar.DAY_OF_MONTH, 1);
        final int maxDay = calenderInstance.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i < maxDay; i++) {
            calenderInstance.set(Calendar.DAY_OF_MONTH, i + 1);
            final String dayOfWeek =
                calenderInstance.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
            if (dayOfWeek.equalsIgnoreCase("Thu")) {
                allWeeks.add(dateFormatter.format(calenderInstance.getTime()));
            }
        }

        return allWeeks;
    }

    private DateTimeUtils()
    {
    }
}
