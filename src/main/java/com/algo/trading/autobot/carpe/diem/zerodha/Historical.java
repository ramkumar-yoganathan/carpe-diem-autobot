/*******************************************************************************
 * Copyright 2020 Ramkumar Yoganathan
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
package com.algo.trading.autobot.carpe.diem.zerodha;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.slf4j.LoggerFactory;

import com.algo.trading.autobot.carpe.diem.config.AppContext;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.HistoricalData;

public final class Historical
{
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Historical.class);

    public Historical()
    {
        // Invoke
    }

    public List<Date> calculateAndReturnHistoricalCandleStickDataRange(final DataRange dataRange,
        final TimeUnit timeUnit)
    {
        final SimpleDateFormat tickDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
        final List<Date> dateRange = new ArrayList<>();
        final long nowTime = System.currentTimeMillis();
        final String toTimeStamp = tickDateFormatter.format(nowTime);
        final long nowMinusRange = nowTime - timeUnit.toMillis(dataRange.get());
        final String fromTimeStamp = tickDateFormatter.format(nowMinusRange);
        try {
            final Date fromDate = tickDateFormatter.parse(fromTimeStamp);
            final Date toDate = tickDateFormatter.parse(toTimeStamp);
            dateRange.add(fromDate);
            dateRange.add(toDate);
        } catch (final ParseException e) {
            LOGGER.error(e.getMessage());
        }

        return dateRange;
    }

    /**
     * Calculate and return valid trade signal from the last two candle.
     *
     * @param kiteConnect
     * @param tradeInstrument
     * @return true/false
     * @throws KiteException
     * @throws IOException
     */
    public List<HistoricalData> calculateAndReturnHistoricalCandleStickDataRange(final String strikeInstrument,
        final List<Date> dataRange, final DataInterval dataInterval)
    {
        List<HistoricalData> allHistorical = new ArrayList<>();
        final String interval = dataInterval.get();

        /** Return the historical date for the previous range. **/
        try {
            allHistorical = AppContext.getStockBroker().getSession().getHistoricalData(dataRange.get(0),
                dataRange.get(1), strikeInstrument, interval, false, true).dataArrayList;
        } catch (JSONException | IOException | KiteException e) {
            LOGGER.error(e.getMessage());
        }

        return allHistorical;

    }

    public List<Date> calculateAndReturnHistoricalCandleStickDataRange(final String fromTimeStamp,
        final String toTimeStamp)
    {
        final SimpleDateFormat tickDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final List<Date> dateRange = new ArrayList<>();
        try {
            final Date fromDate = tickDateFormatter.parse(fromTimeStamp);
            final Date toDate = tickDateFormatter.parse(toTimeStamp);
            dateRange.add(fromDate);
            dateRange.add(toDate);
        } catch (final ParseException e) {
            LOGGER.error(e.getMessage());
        }

        return dateRange;
    }

}
