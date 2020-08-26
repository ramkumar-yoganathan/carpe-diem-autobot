/*******************************************************************************
 * Copyright (C) 2020 Ramkumar Yoganathan
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.algo.trading.autobot.carpe.diem.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public final class CommonUtils
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
        final String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
        final boolean isExpiryFriday = dayOfWeek.equalsIgnoreCase("Fri");
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
            calendar.add(Calendar.DATE, 1);
        }
        if (isExpiryFriday) {
            calendar.add(Calendar.DATE, 7);
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

    public static double getRoundedPrice(final double tradingPrice, final int decimalPlaces)
    {
        BigDecimal bigDecimal = new BigDecimal(Double.toString(tradingPrice));
        bigDecimal = bigDecimal.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    private CommonUtils()
    {
    }
}
