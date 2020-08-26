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
package com.algo.trading.autobot.carpe.diem.config;

import com.algo.trading.autobot.carpe.diem.data.EquityFuturesRepo;
import com.algo.trading.autobot.carpe.diem.data.EquityOptionsRepo;
import com.algo.trading.autobot.carpe.diem.data.EquityOptionsStrikePriceRepo;
import com.algo.trading.autobot.carpe.diem.data.EquityOrdersRepo;
import com.algo.trading.autobot.carpe.diem.data.EquityTicksRepo;
import com.algo.trading.autobot.carpe.diem.data.StockBrokerSessionRepo;

public final class AppContext
{
    private static EquityFuturesRepo equityFuturesRepo;

    private static EquityOptionsRepo equityOptionsRepo;

    private static EquityOptionsStrikePriceRepo optionsStrike;

    private static StockBroker stockBroker;

    private static StockBrokerSessionRepo stockBrokerSession;

    private static EquityOrdersRepo equityOrdersRepo;

    private static EquityTicksRepo equityTicksRepo;

    public static EquityFuturesRepo getEquityFutures()
    {
        return equityFuturesRepo;
    }

    public static EquityTicksRepo getEquityTicks()
    {
        return equityTicksRepo;
    }

    public static EquityOptionsRepo getEquityOptions()
    {
        return equityOptionsRepo;
    }

    public static EquityOptionsStrikePriceRepo getOptionsStrike()
    {
        return optionsStrike;
    }

    public static StockBroker getStockBroker()
    {
        return stockBroker;
    }

    public static EquityOrdersRepo getOrders()
    {
        return equityOrdersRepo;
    }

    public static StockBrokerSessionRepo getStockBrokerSession()
    {
        return stockBrokerSession;
    }

    public static void setFuturesRepository(final EquityFuturesRepo equityFuturesRepository)
    {
        AppContext.equityFuturesRepo = equityFuturesRepository;
    }

    public static void setTicksRepository(final EquityTicksRepo equityTicksRepo)
    {
        AppContext.equityTicksRepo = equityTicksRepo;
    }

    public static void setOrdersRepository(final EquityOrdersRepo equityOrdersRepo)
    {
        AppContext.equityOrdersRepo = equityOrdersRepo;
    }

    public static void setOptionsRepository(final EquityOptionsRepo equityOptionsRepo)
    {
        AppContext.equityOptionsRepo = equityOptionsRepo;
    }

    public static void setOptionStrikePrice(final EquityOptionsStrikePriceRepo optionsStrike)
    {
        AppContext.optionsStrike = optionsStrike;
    }

    public static void setStockBroker(final StockBroker stockBroker)
    {
        AppContext.stockBroker = stockBroker;
    }

    public static void setStockBrokerSession(final StockBrokerSessionRepo stockBrokerSession)
    {
        AppContext.stockBrokerSession = stockBrokerSession;
    }

    private AppContext()
    {
    }

}
