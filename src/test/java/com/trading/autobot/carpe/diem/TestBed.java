package com.trading.autobot.carpe.diem;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;

import com.mgnt.utils.TimeUtils;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.HistoricalData;

public class TestBed
{
    public static void main(final String[] args) throws IOException, JSONException, KiteException
    {

        final KiteConnect kiteConnect = new KiteConnect("xsgsc96ut7r190n7", false);
        // final User user =
        /// kiteConnect.generateSession("OWSsDavlNX7k0hjY6CQPQCzbEDXJ0xeP", "olcniqxzz7gp69klgdh98xdrp07775iy");
        kiteConnect.setAccessToken("Y0VdiPZOwU7yO5DVZS1r6C2A1G12cxxD");
        while (true) {
            getHistoricalData(kiteConnect);
        }
    }

    public static void getHistoricalData(final KiteConnect kiteConnect) throws KiteException, IOException, JSONException
    {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String nowStartTimeStamp = formatter.format(Calendar.getInstance().getTime()) + ":00";
        final String nowEndTimeStamp = formatter1.format(Calendar.getInstance().getTime());
        Date from = new Date();
        Date to = new Date();
        try {
            from = formatter.parse(nowStartTimeStamp);
            to = formatter1.parse(nowEndTimeStamp);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        final HistoricalData historicalData = kiteConnect.getHistoricalData(from, to, "9994242", "minute", false, true);
        if (!historicalData.dataArrayList.isEmpty()) {
            System.out.println(
                to + " = " + historicalData.dataArrayList.get(0).open + "|" + historicalData.dataArrayList.get(0).high
                    + "|" + historicalData.dataArrayList.get(0).low + "|" + historicalData.dataArrayList.get(0).close);
            TimeUtils.sleepFor(1, TimeUnit.SECONDS);
        }
    }
}
