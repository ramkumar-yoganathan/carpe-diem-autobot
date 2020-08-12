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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;

import com.algo.trading.autobot.carpe.diem.config.AppContext;
import com.algo.trading.autobot.carpe.diem.config.Globals;
import com.algo.trading.autobot.carpe.diem.data.EquityOptionsStrikePrice;
import com.algo.trading.autobot.carpe.diem.data.EquityOrders;
import com.algo.trading.autobot.carpe.diem.zerodha.DataInterval;
import com.algo.trading.autobot.carpe.diem.zerodha.Historical;
import com.algo.trading.autobot.carpe.diem.zerodha.OptionsStrikePrice;
import com.algo.trading.autobot.carpe.diem.zerodha.OrdersImpl;
import com.mgnt.utils.TimeUtils;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.HistoricalData;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.OrderParams;

public final class CandleStickStrategy
{
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CandleStickStrategy.class);

    static SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private CandleStickStrategy()
    {
    }

    public static void runBot(final Globals optionIndex, final Globals optionType)
    {
        /** Read the historical data. **/
        final SimpleDateFormat zeroTimeStampParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final SimpleDateFormat nowTimeStampParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final SimpleDateFormat nowInSecondsParser = new SimpleDateFormat("ss");
        final Historical dataHistorical = new Historical();
        /** Primitive Initialization. **/
        double tradeBoughtAt = 0.0;
        double desiredSellPrice = 0.0;
        boolean isOrderPlaced = false;
        String strikeSymbol = "";
        long strikeInstrument = 0;
        int strikeLotsize = 0;
        /** Object Initialization. **/
        final OrdersImpl ordersInstance = new OrdersImpl();
        final OptionsStrikePrice optionsStrikePrice = new OptionsStrikePrice();
        /** Get at the money option strike price. **/
        EquityOptionsStrikePrice atmStrikePrice =
            optionsStrikePrice.queryAndReturnEquityFuturePriceEquivalentStrikePrice(optionIndex.name());
        /** Start the price monitor for every second. **/
        while (true) {
            /** Check, If we are at new minute. If so, query the future price and update the strike price. **/
            final int inSeconds = Integer.parseInt(nowInSecondsParser.format(Calendar.getInstance().getTime()));
            if (inSeconds == 01) {
                atmStrikePrice =
                    optionsStrikePrice.queryAndReturnEquityFuturePriceEquivalentStrikePrice(optionIndex.name());
            }
            /** Check, If we have call or put scheduler. **/
            if (optionType.equals(Globals.CALL)) {
                strikeSymbol = atmStrikePrice.getCallSymbol();
                strikeInstrument = atmStrikePrice.getCallInstrument();
                strikeLotsize = atmStrikePrice.getLotSize();
            } else if (optionType.equals(Globals.PUT)) {
                strikeSymbol = atmStrikePrice.getPutSymbol();
                strikeInstrument = atmStrikePrice.getPutInstrument();
                strikeLotsize = atmStrikePrice.getLotSize();
            }
            /** Parse the date and time stamp. **/
            final List<Date> candleStickDates = new ArrayList<>();
            final String fromTimeStamp = zeroTimeStampParser.format(Calendar.getInstance().getTime()) + ":00";
            final String toTimeStamp = nowTimeStampParser.format(Calendar.getInstance().getTime());
            try {
                final Date fromSecond = zeroTimeStampParser.parse(fromTimeStamp);
                final Date toSecond = nowTimeStampParser.parse(toTimeStamp);
                candleStickDates.add(fromSecond);
                candleStickDates.add(toSecond);
            } catch (final ParseException e) {
                LOGGER.error(e.getMessage());
            }
            /** Parse the date and time stamp. **/
            final List<HistoricalData> candleStickHistoricalData =
                dataHistorical.calculateAndReturnHistoricalCandleStickDataRange(String.valueOf(strikeInstrument),
                    candleStickDates, DataInterval.ONE_MINUTE);
            /** Get the historical data. **/
            if (!candleStickHistoricalData.isEmpty()) {
                final HistoricalData lastCandleStick = candleStickHistoricalData.get(0);
                final EquityOrders equityOrders = new EquityOrders();
                if ((lastCandleStick.open == lastCandleStick.low) && (!isOrderPlaced)
                    && (tradeBoughtAt != lastCandleStick.open)) {
                    tradeBoughtAt = lastCandleStick.open;
                    desiredSellPrice = tradeBoughtAt + (tradeBoughtAt * 0.02);
                    isOrderPlaced = true;
                    final OrderParams buyOrderParams = ordersInstance.getBuyOrderParams(strikeLotsize,
                        Constants.ORDER_TYPE_LIMIT, strikeSymbol, tradeBoughtAt);
                    final Order executedOrder = ordersInstance.placeOrder(buyOrderParams);
                    equityOrders.setBuyTimeStamp(nowTimeStampParser.format(Calendar.getInstance().getTime()));
                    equityOrders.setStrikeSymbol(strikeSymbol);
                    equityOrders.setStrikeQuantity(strikeLotsize);
                    equityOrders.setBuyPrice(tradeBoughtAt);
                    equityOrders.setBuyOrderId(executedOrder.orderId);
                }
                if ((lastCandleStick.high > desiredSellPrice) && isOrderPlaced) {
                    isOrderPlaced = false;
                    desiredSellPrice = 0.0;
                    final OrderParams sellOrderParams = ordersInstance.getSellOrderParams(strikeLotsize,
                        Constants.ORDER_TYPE_LIMIT, strikeSymbol, lastCandleStick.high);
                    final Order executedOrder = ordersInstance.placeOrder(sellOrderParams);
                    equityOrders.setSellTimeStamp(nowTimeStampParser.format(Calendar.getInstance().getTime()));
                    equityOrders.setSellPrice(lastCandleStick.high);
                    equityOrders.setSellOrderId(executedOrder.orderId);
                    equityOrders.setRealisedPoints(lastCandleStick.high - tradeBoughtAt);
                    AppContext.getOrders().save(equityOrders);
                }

                TimeUtils.sleepFor(1, TimeUnit.SECONDS);
            }
        }
    }

}
