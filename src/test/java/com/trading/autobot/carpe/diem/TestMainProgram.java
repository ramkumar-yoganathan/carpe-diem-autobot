package com.trading.autobot.carpe.diem;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TestMainProgram
{
    public static void main(final String[] args)
    {
        final Calendar calInstance = Calendar.getInstance();
        calInstance.add(Calendar.DAY_OF_WEEK, -3);
        System.out.println(new SimpleDateFormat("EEE").format(calInstance.getTime()));
    }
}
