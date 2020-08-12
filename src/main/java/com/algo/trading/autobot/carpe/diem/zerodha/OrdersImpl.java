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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.slf4j.LoggerFactory;

import com.algo.trading.autobot.carpe.diem.config.AppContext;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.LTPQuote;
import com.zerodhatech.models.Margin;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.OrderParams;
import com.zerodhatech.models.Position;

public class OrdersImpl extends OrderParameters implements Orders
{
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OrdersImpl.class);

    @Override
    public Map<String, LTPQuote> getLastTradePrice(final String tradingInstruments)
    {
        Map<String, LTPQuote> lastTradedPrices = new HashMap<>();
        final String[] strikeInstruments = tradingInstruments.split(",");
        try {
            lastTradedPrices = AppContext.getStockBroker().getSession().getLTP(strikeInstruments);
        } catch (JSONException | IOException | KiteException e) {
            LOGGER.error(e.getMessage());
        }

        return lastTradedPrices;
    }

    @Override
    public int getMargins()
    {
        try {
            final Margin netMargin = AppContext.getStockBroker().getSession().getMargins("equity");
            return Integer.parseInt(netMargin.net.split("\\.")[0]);
        } catch (JSONException | IOException | KiteException e) {
            LOGGER.error(e.getMessage());
        }

        return 0;
    }

    @Override
    public List<Order> getOpenOrders(final String transactionType)
    {
        List<Order> allOpenOrders = new ArrayList<>();
        try {
            allOpenOrders = AppContext.getStockBroker().getSession().getOrders().stream()
                .filter(e -> e.status.equalsIgnoreCase("COMPLETE"))
                .filter(e -> e.transactionType.equalsIgnoreCase(transactionType))
                .filter(e -> e.product.equalsIgnoreCase("MIS")).collect(Collectors.toList());
        } catch (JSONException | IOException | KiteException e) {
            LOGGER.error(e.getMessage());
        }

        return allOpenOrders;
    }

    @Override
    public List<Order> getOpenStopLossOrders(final String tradingSymbol)
    {
        List<Order> allOpenOrders = new ArrayList<>();
        try {
            allOpenOrders = AppContext.getStockBroker().getSession().getOrders().stream()
                .filter(e -> e.status.equalsIgnoreCase("TRIGGER PENDING"))
                .filter(e -> e.tradingSymbol.equalsIgnoreCase(tradingSymbol))
                .filter(e -> e.product.equalsIgnoreCase("MIS")).collect(Collectors.toList());
        } catch (JSONException | IOException | KiteException e) {
            LOGGER.error(e.getMessage());
        }

        return allOpenOrders;
    }

    @Override
    public List<Position> getOpenPositions(final String tradingSymbol)
    {
        List<Position> allOpenPositions = new ArrayList<>();
        try {
            allOpenPositions = AppContext.getStockBroker().getSession().getPositions().get("day").stream()
                .filter(p -> p.tradingSymbol.equalsIgnoreCase(tradingSymbol))
                .filter(p -> p.product.equalsIgnoreCase(Constants.PRODUCT_MIS)).filter(p -> p.netQuantity > 0)
                .collect(Collectors.toList());
        } catch (JSONException | IOException | KiteException e) {
            LOGGER.error(e.getMessage());
        }

        return allOpenPositions;
    }

    @Override
    public List<Order> getOrders(final String tradingSymbol, final String orderStatus, final String orderProduct)
    {
        List<Order> filteredOrders = new ArrayList<>();

        try {
            filteredOrders = AppContext.getStockBroker().getSession().getOrders().stream()
                .filter(e -> e.product.equalsIgnoreCase(orderProduct))
                .filter(e -> e.tradingSymbol.equalsIgnoreCase(orderProduct))
                .filter(e -> e.status.equalsIgnoreCase(orderStatus)).collect(Collectors.toList());
        } catch (JSONException | IOException | KiteException e) {
            LOGGER.error(e.getMessage());
        }

        return filteredOrders;
    }

    @Override
    public Order modifyOrder(final String orderId, final OrderParams orderParams)
    {
        Order modifiedOrder = null;
        try {
            modifiedOrder =
                AppContext.getStockBroker().getSession().modifyOrder(orderId, orderParams, Constants.VARIETY_REGULAR);
        } catch (JSONException | IOException | KiteException e) {
            LOGGER.error(e.getMessage());
        }

        return modifiedOrder;
    }

    @Override
    public Order placeOrder(final OrderParams orderParams)
    {
        Order placedOrder = new Order();
        try {
            placedOrder = AppContext.getStockBroker().getSession().placeOrder(orderParams, Constants.VARIETY_REGULAR);
        } catch (final JSONException | IOException | KiteException e) {
            LOGGER.error(e.getMessage());
        }
        return placedOrder;
    }
}
