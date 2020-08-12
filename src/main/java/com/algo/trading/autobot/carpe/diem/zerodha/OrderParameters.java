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

import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.OrderParams;

public class OrderParameters
{
    public OrderParameters()
    {
        // Will invoke
    }

    public OrderParams getBuyOrderParams(final int orderQuanity, final String orderType, final String tradingSymbol,
        final double orderPrice)
    {
        final OrderParams orderParams = new OrderParams();
        orderParams.quantity = orderQuanity;
        orderParams.orderType = orderType;
        orderParams.tradingsymbol = tradingSymbol;
        orderParams.product = Constants.PRODUCT_MIS;
        orderParams.exchange = Constants.EXCHANGE_NFO;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.price = orderPrice;
        orderParams.triggerPrice = 0.0;
        orderParams.tag = "ZEPHYRUS";
        return orderParams;
    }

    public OrderParams getSellOrderParams(final int orderQuanity, final String orderType, final String tradingSymbol,
        final double orderPrice)
    {
        final OrderParams orderParams = new OrderParams();
        orderParams.quantity = orderQuanity;
        orderParams.orderType = orderType;
        orderParams.tradingsymbol = tradingSymbol;
        orderParams.product = Constants.PRODUCT_MIS;
        orderParams.exchange = Constants.EXCHANGE_NFO;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.price = orderPrice;
        orderParams.triggerPrice = 0.0;
        orderParams.tag = "ZEPHYRUS";
        return orderParams;
    }

    public OrderParams getStopLossParams(final OrderParams buyOrderParams, final double stopLossPrice)
    {
        final OrderParams orderParams = new OrderParams();
        orderParams.quantity = buyOrderParams.quantity;
        orderParams.orderType = Constants.ORDER_TYPE_SLM;
        orderParams.tradingsymbol = buyOrderParams.tradingsymbol;
        orderParams.product = Constants.PRODUCT_MIS;
        orderParams.exchange = Constants.EXCHANGE_NFO;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.price = 0.0;
        orderParams.triggerPrice = stopLossPrice;
        orderParams.tag = "ZEPHYRUS";
        return orderParams;
    }
}
