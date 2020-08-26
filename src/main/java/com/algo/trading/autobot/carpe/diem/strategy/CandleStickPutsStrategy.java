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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.algo.trading.autobot.carpe.diem.config.AppContext;
import com.algo.trading.autobot.carpe.diem.config.Globals;
import com.algo.trading.autobot.carpe.diem.config.RewardProfile;
import com.algo.trading.autobot.carpe.diem.data.EquityOptionsStrikePrice;
import com.algo.trading.autobot.carpe.diem.data.EquityOrders;
import com.algo.trading.autobot.carpe.diem.utils.CommonUtils;
import com.algo.trading.autobot.carpe.diem.zerodha.DataInterval;
import com.algo.trading.autobot.carpe.diem.zerodha.Historical;
import com.algo.trading.autobot.carpe.diem.zerodha.OptionsStrikePrice;
import com.algo.trading.autobot.carpe.diem.zerodha.OrdersImpl;
import com.mgnt.utils.TimeUtils;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.HistoricalData;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.OrderParams;

public final class CandleStickPutsStrategy
{
    private static double desiredBuyPrice = 0.0;

    private static HistoricalData desiredCandleStick = null;

    private static double desiredSellPrice = 0.0;

    private static String desiredStrikeInstrument = "";

    private static int desiredStrikeLotSize = 0;

    private static String desiredStrikeSymbol = "";

    private static boolean hasOpenOrder = false;

    private static final Logger LOGGER = Logger.getLogger(CandleStickPutsStrategy.class.getName());

    private static OrdersImpl ordersInstance = new OrdersImpl();

    private static boolean calculateAndExecuteKiteConnectBuyOrder()
    {
        if (desiredCandleStick != null) {
            final EquityOrders equityOrders = new EquityOrders();
            final RewardProfile rewardProfile =
                RewardProfile.valueOf(AppContext.getStockBroker().getReward().toUpperCase());
            desiredBuyPrice = desiredCandleStick.open;
            desiredSellPrice = CommonUtils.getRoundedPrice(desiredBuyPrice + rewardProfile.getPoint(), 2);
            final OrderParams buyOrderParams = ordersInstance.getBuyOrderParams(desiredStrikeLotSize,
                Constants.ORDER_TYPE_LIMIT, desiredStrikeSymbol, desiredBuyPrice);
            final Order executedOrder = ordersInstance.placeOrder(buyOrderParams);
            if (executedOrder != null) {
                LOGGER.info(String.format(">>>>> BUY     >>>>>>>>>>>>>>>>>  %s %s %s %s <<<<<<<<<<<<<<<<<<<<<<<",
                    desiredStrikeSymbol, getDecimalFormat(2).format(desiredBuyPrice),
                    getDecimalFormat(2).format(desiredSellPrice), desiredStrikeLotSize));
            } else {
                equityOrders.setBuyOrderId("000000000");
            }
            equityOrders.setBuyOrderId(executedOrder.orderId);
            equityOrders
                .setBuyTimeStamp(CommonUtils.getDateFormatOfRelativeSeconds().format(Calendar.getInstance().getTime()));
            equityOrders.setStrikeSymbol(desiredStrikeSymbol);
            equityOrders.setStrikeQuantity(desiredStrikeLotSize);
            equityOrders.setBuyPrice(desiredBuyPrice);
            AppContext.getOrders().save(equityOrders);
            hasOpenOrder = true;
            desiredCandleStick = null;
        }

        return hasOpenOrder;
    }

    private static boolean calculateAndExecuteKiteConnectSellOrder()
    {
        final double lastTradedPrice =
            ordersInstance.getLastTradePrice(desiredStrikeInstrument).get(desiredStrikeInstrument).lastPrice;
        if (lastTradedPrice >= desiredSellPrice) {
            final EquityOrders equityOrders = new EquityOrders();
            final OrderParams sellOrderParams = ordersInstance.getSellOrderParams(desiredStrikeLotSize,
                Constants.ORDER_TYPE_LIMIT, desiredStrikeSymbol, desiredSellPrice);
            final Order executedOrder = ordersInstance.placeOrder(sellOrderParams);
            if (executedOrder != null) {
                LOGGER.info(String.format(">>>>> SELL    >>>>>>>>>>>>>>>>>  %s %s %s %s <<<<<<<<<<<<<<<<<<<<<<<",
                    desiredStrikeSymbol, getDecimalFormat(2).format(desiredSellPrice),
                    getDecimalFormat(2).format(lastTradedPrice), desiredStrikeLotSize));
            } else {
                equityOrders.setSellOrderId("000000000");
            }
            equityOrders.setSellOrderId(executedOrder.orderId);
            equityOrders.setSellTimeStamp(
                CommonUtils.getDateFormatOfRelativeSeconds().format(Calendar.getInstance().getTime()));
            equityOrders.setSellPrice(lastTradedPrice);
            equityOrders.setRealisedPoints(lastTradedPrice - desiredBuyPrice);
            AppContext.getOrders().save(equityOrders);
            desiredCandleStick = null;
            hasOpenOrder = false;
        } else {
            LOGGER.info(String.format(">>>>> LTP     >>>>>>>>>>>>>>>>>  %s %s %s    <<<<<<<<<<<<<<<<<<<<<<<",
                desiredStrikeSymbol, getDecimalFormat(2).format(lastTradedPrice),
                getDecimalFormat(2).format(desiredSellPrice), " "));
        }

        return hasOpenOrder;
    }

    private static boolean calculateAndUpdateOptionStrikePrice(final Globals optionIndex, final Globals optionType)
    {
        /** Object Initialization. **/
        final OptionsStrikePrice optionsStrikePrice = new OptionsStrikePrice();
        final SimpleDateFormat nowInSecondsParser = new SimpleDateFormat("ss");
        /** Check, If we are at new minute. If so, query the future price and update the strike price. **/
        final int inSeconds = Integer.parseInt(nowInSecondsParser.format(Calendar.getInstance().getTime()));
        if ((inSeconds == 01)) {
            final EquityOptionsStrikePrice atmStrikePrice =
                optionsStrikePrice.queryAndReturnEquityFuturePriceEquivalentStrikePrice(optionIndex.name());
            /** Check, If we have call or put scheduler. **/
            if (optionType.equals(Globals.CALL)) {
                desiredStrikeSymbol = atmStrikePrice.getCallSymbol();
                desiredStrikeInstrument = String.valueOf(atmStrikePrice.getCallInstrument());
                desiredStrikeLotSize = atmStrikePrice.getLotSize();
            } else if (optionType.equals(Globals.PUT)) {
                desiredStrikeSymbol = atmStrikePrice.getPutSymbol();
                desiredStrikeInstrument = String.valueOf(atmStrikePrice.getPutInstrument());
                desiredStrikeLotSize = atmStrikePrice.getLotSize();
            }

            checkAndReturnIfTheEntryRuleConditionIsMet();

        } else if (!hasOpenOrder) {
            LOGGER.info(String
                .format(">>>>> SIGNAL  >>>>>>>>>>>>>>>>>         Waiting for Signal          <<<<<<<<<<<<<<<<<<<<<<<"));
        }

        return false;
    }

    private static void checkAndReturnIfTheEntryRuleConditionIsMet()
    {
        final Historical dataHistorical = new Historical();
        final List<HistoricalData> candleStickHistoricalData =
            dataHistorical.calculateAndReturnHistoricalCandleStickDataRange(desiredStrikeInstrument,
                getHistoricalDataRange(), DataInterval.ONE_MINUTE);
        if (!candleStickHistoricalData.isEmpty()) {
            final HistoricalData candleStickData = candleStickHistoricalData.get(0);
            if ((candleStickData.open == candleStickData.low) && !hasOpenOrder) {
                desiredCandleStick = candleStickData;
            }
        }
    }

    public static DecimalFormat getDecimalFormat(final int decimals)
    {
        final StringBuilder sb = new StringBuilder(decimals + 2);
        sb.append("#.");
        for (int i = 0; i < decimals; i++) {
            sb.append("0");
        }
        return new DecimalFormat(sb.toString());
    }

    public static List<Date> getHistoricalDataRange()
    {
        /** Parse the date and time stamp. **/
        final SimpleDateFormat zeroTimeStampParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final SimpleDateFormat nowTimeStampParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final List<Date> candleStickDates = new ArrayList<>();
        final String fromTimeStamp = zeroTimeStampParser.format(Calendar.getInstance().getTime()) + ":00";
        final String toTimeStamp = nowTimeStampParser.format(Calendar.getInstance().getTime());
        try {
            final Date fromSecond = zeroTimeStampParser.parse(fromTimeStamp);
            final Date toSecond = nowTimeStampParser.parse(toTimeStamp);
            candleStickDates.add(fromSecond);
            candleStickDates.add(toSecond);
        } catch (final ParseException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

        return candleStickDates;
    }

    public static void runBot(final Globals optionIndex, final Globals optionType)
    {
        /** Start the price monitor for every second. **/
        while (true) {
            /** Check, If we are at new minute. If so, query the future price and update the strike price. **/
            calculateAndUpdateOptionStrikePrice(optionIndex, optionType);
            /** Check, If we have a chance for buy option. **/
            final boolean isOrderLive = calculateAndExecuteKiteConnectBuyOrder();
            if (isOrderLive) {
                /** Check, If we have a chance for sell option. **/
                calculateAndExecuteKiteConnectSellOrder();
            }
            /** Sleep for 1 Second. **/
            TimeUtils.sleepFor(1, TimeUnit.SECONDS);
        }
    }

    private CandleStickPutsStrategy()
    {
    }

}
