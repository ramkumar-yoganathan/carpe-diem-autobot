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

import java.util.List;
import java.util.Map;

import com.zerodhatech.models.LTPQuote;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.OrderParams;
import com.zerodhatech.models.Position;

public interface Orders
{
    Map<String, LTPQuote> getLastTradePrice(final String tradingInstruments);

    int getMargins();

    List<Order> getOpenOrders(final String orderType);

    List<Position> getOpenPositions(final String tradingSymbol);

    List<Order> getOrders(final String tradingSymbol, String orderStatus, String orderProduct);

    Order modifyOrder(String orderId, OrderParams orderParams);

    Order placeOrder(OrderParams orderParams);

    List<Order> getOpenStopLossOrders(final String tradingSymbol);
}
