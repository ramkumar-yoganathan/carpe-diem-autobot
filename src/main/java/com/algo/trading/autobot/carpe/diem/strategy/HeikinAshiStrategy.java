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
package com.algo.trading.autobot.carpe.diem.strategy;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;

import com.algo.trading.autobot.carpe.diem.data.EquityOptionsStrikePrice;
import com.algo.trading.autobot.carpe.diem.zerodha.DataInterval;
import com.algo.trading.autobot.carpe.diem.zerodha.Historical;
import com.algo.trading.autobot.carpe.diem.zerodha.OptionsStrikePrice;
import com.mgnt.utils.TimeUtils;
import com.zerodhatech.models.HistoricalData;

public final class HeikinAshiStrategy
{
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HeikinAshiStrategy.class);

    private static List<CandleStick> dataHeikinAshiCandleSticks = new ArrayList<>();

    static SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public HeikinAshiStrategy()
    {
        // Invoke
    }

    private List<Double> calculateAndReturnCandleStickPrice(final double candleStickPrice,
        final double candleStickHeikinAshiOpen, final double candleStickHeikinAshiClose)
    {
        final List<Double> candleStickPrices;
        candleStickPrices = new ArrayList<>();
        candleStickPrices.add(candleStickPrice);
        candleStickPrices.add(candleStickHeikinAshiOpen);
        candleStickPrices.add(candleStickHeikinAshiClose);
        Collections.sort(candleStickPrices);
        return candleStickPrices;
    }

    public List<CandleStick> calculateAndReturnHeikinAshiCandleStickValue(final String strikeInstrument,
        final DataInterval dateInterval, final List<HistoricalData> candleStickHistoricalData)
    {
        /** Read the historical data. **/
        final DecimalFormat priceDigitFormatter = new DecimalFormat("#.##");
        /** Calculate the candle stick **/
        for (final HistoricalData eachCandleStick : candleStickHistoricalData) {
            /** Variables declaration. **/
            double candleStickHeikinAshiOpen = 0.0;
            final String timeStamp = parseAndReturnKiteTimeStamp(eachCandleStick.timeStamp);
            final boolean isFirstCandleStick = timeStamp.contains("09:15:00");
            /** CandleStick HeikinAshi Open Price */
            if (isFirstCandleStick) {
                // Get previous open and close for today open price calculation.
                final Calendar calInstance = Calendar.getInstance();
                final String dayName =
                    new SimpleDateFormat("EEE").format(calInstance.getTime()).toUpperCase(Locale.getDefault());
                if (dayName.equalsIgnoreCase("MON")) {
                    calInstance.add(Calendar.DAY_OF_WEEK, -3);
                } else {
                    calInstance.add(Calendar.DAY_OF_WEEK, -1);
                }

                final String lastDayCloseTickBeginStamp =
                    new SimpleDateFormat("yyyy-MM-dd").format(calInstance.getTime()) + " 15:29:00";
                final String lastDayCloseTickEndStamp =
                    new SimpleDateFormat("yyyy-MM-dd").format(calInstance.getTime()) + " 15:30:00";
                final Historical candleStickHistoricalInstance = new Historical();
                final List<Date> dataRange =
                    candleStickHistoricalInstance.calculateAndReturnHistoricalCandleStickDataRange(
                        lastDayCloseTickBeginStamp, lastDayCloseTickEndStamp);
                final List<HistoricalData> lastDayHistoricalData = candleStickHistoricalInstance
                    .calculateAndReturnHistoricalCandleStickDataRange(strikeInstrument, dataRange, dateInterval);
                if (!lastDayHistoricalData.isEmpty()) {
                    final double candleStickOpen = lastDayHistoricalData.get(0).open;
                    final double candleStickClose = lastDayHistoricalData.get(0).close;
                    candleStickHeikinAshiOpen =
                        Double.parseDouble(priceDigitFormatter.format((candleStickOpen + candleStickClose) / 2));
                }
            } else {
                final CandleStick lastCandleStick =
                    dataHeikinAshiCandleSticks.get(dataHeikinAshiCandleSticks.size() - 1);
                if (lastCandleStick != null) {
                    candleStickHeikinAshiOpen = Double.parseDouble(priceDigitFormatter
                        .format((lastCandleStick.getTickOpenPrice() + lastCandleStick.getTickClosePrice()) / 2));
                }
            }

            /** CandleStick HeikinAshi Close Price */
            final double candleStickHeikinAshiClose = Double.parseDouble(priceDigitFormatter.format(
                (eachCandleStick.open + eachCandleStick.high + eachCandleStick.low + eachCandleStick.close) / 4));
            /** CandleStick HeikinAshi High Price */
            List<Double> candleStickPrices = calculateAndReturnCandleStickPrice(eachCandleStick.high,
                candleStickHeikinAshiOpen, candleStickHeikinAshiClose);
            final double candleStickHeikinAshiHigh = candleStickPrices.get(candleStickPrices.size() - 1);
            /** CandleStick HeikinAshi Low Price */
            candleStickPrices = calculateAndReturnCandleStickPrice(eachCandleStick.low, candleStickHeikinAshiOpen,
                candleStickHeikinAshiClose);
            final double candleStickHeikinAshiLow = candleStickPrices.get(0);
            /** Insert into List */
            final CandleStick candleStick = new CandleStick();
            candleStick.setTickOpenPrice(candleStickHeikinAshiOpen);
            candleStick.setTickHighPrice(candleStickHeikinAshiHigh);
            candleStick.setTickLowPrice(candleStickHeikinAshiLow);
            candleStick.setTickClosePrice(candleStickHeikinAshiClose);

            dataHeikinAshiCandleSticks.add(candleStick);

        }

        return dataHeikinAshiCandleSticks;
    }

    public void scheduleHeikinAshiCandleStickBasedAlgorithmTradingEngine()
    {
        /** Read the historical data. **/
        final DecimalFormat priceDigitFormatter = new DecimalFormat("#.##");
        final SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
        final SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");
        final SimpleDateFormat secondsOnly = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final SimpleDateFormat seconds = new SimpleDateFormat("ss");
        final Historical dataHistorical = new Historical();
        String fromTimeStamp = dateOnly.format(Calendar.getInstance().getTime()) + " 09:15:00";
        String toTimeStamp = dateTime.format(Calendar.getInstance().getTime());

        /** Get at the money option strike price. **/
        final OptionsStrikePrice optionsStrikePrice = new OptionsStrikePrice();
        final EquityOptionsStrikePrice atmStrikePrice =
            optionsStrikePrice.queryAndReturnEquityFuturePriceEquivalentStrikePrice("NIFTY");

        /** Get Henkin Ashi Candle Stick Calculation. **/
        if (dataHeikinAshiCandleSticks.isEmpty()) {
            final List<Date> candleStickDates =
                dataHistorical.calculateAndReturnHistoricalCandleStickDataRange(fromTimeStamp, toTimeStamp);
            final List<HistoricalData> candleStickHistoricalData =
                dataHistorical.calculateAndReturnHistoricalCandleStickDataRange(
                    String.valueOf(atmStrikePrice.getCallInstrument()), candleStickDates, DataInterval.ONE_MINUTE);
            dataHeikinAshiCandleSticks = calculateAndReturnHeikinAshiCandleStickValue(
                String.valueOf(atmStrikePrice.getCallInstrument()), DataInterval.ONE_MINUTE, candleStickHistoricalData);

        }

        /** Get Current Candle Stick Price and Apply Heinkin Ashi Formula. **/
        while (true) {
            final CandleStick candleStickHeikin = dataHeikinAshiCandleSticks.get(dataHeikinAshiCandleSticks.size() - 1);
            HistoricalData eachCandleStick = null;
            fromTimeStamp = secondsOnly.format(Calendar.getInstance().getTime()) + ":00";
            toTimeStamp = dateTime.format(Calendar.getInstance().getTime());
            final List<Date> candleStickDates =
                dataHistorical.calculateAndReturnHistoricalCandleStickDataRange(fromTimeStamp, toTimeStamp);
            final List<HistoricalData> candleStickHistoricalData =
                dataHistorical.calculateAndReturnHistoricalCandleStickDataRange(
                    String.valueOf(atmStrikePrice.getCallInstrument()), candleStickDates, DataInterval.ONE_MINUTE);
            if (!candleStickHistoricalData.isEmpty()) {
                eachCandleStick = candleStickHistoricalData.get(0);
                /** CandleStick HeikinAshi Close Price */
                final double candleStickHeikinAshiClose = Double.parseDouble(priceDigitFormatter.format(
                    (eachCandleStick.open + eachCandleStick.high + eachCandleStick.low + eachCandleStick.close) / 4));
                /** CandleStick HeikinAshi High Price */
                List<Double> candleStickPrices = calculateAndReturnCandleStickPrice(eachCandleStick.high,
                    candleStickHeikin.getTickOpenPrice(), candleStickHeikinAshiClose);
                final double candleStickHeikinAshiHigh = candleStickPrices.get(candleStickPrices.size() - 1);
                /** CandleStick HeikinAshi Low Price */
                candleStickPrices = calculateAndReturnCandleStickPrice(eachCandleStick.low,
                    candleStickHeikin.getTickOpenPrice(), candleStickHeikinAshiClose);
                final double candleStickHeikinAshiLow = candleStickPrices.get(0);

                /** Insert into List */
                final CandleStick candleStick = new CandleStick();
                candleStick.setTickOpenPrice(candleStickHeikin.getTickOpenPrice());
                candleStick.setTickHighPrice(candleStickHeikinAshiHigh);
                candleStick.setTickLowPrice(candleStickHeikinAshiLow);
                candleStick.setTickClosePrice(candleStickHeikinAshiClose);

                final boolean isEndOfMinute = seconds.format(Calendar.getInstance().getTime()).equalsIgnoreCase("58");
                if (isEndOfMinute) {
                    dataHeikinAshiCandleSticks.add(candleStick);
                } else {
                    LOGGER.info(candleStick.toString());
                }

                TimeUtils.sleepFor(1, TimeUnit.SECONDS);
            }
        }
    }

    private String parseAndReturnKiteTimeStamp(final String timeStamp)
    {
        final DateFormat kiteTimeStampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        final SimpleDateFormat timeStampSimple = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");

        try {
            final Date timeStampParsed = (Date) kiteTimeStampFormat.parseObject(timeStamp);
            return timeStampSimple.format(timeStampParsed);
        } catch (final ParseException e) {
            LOGGER.error(e.getMessage());
        }

        return timeStamp;

    }
}
