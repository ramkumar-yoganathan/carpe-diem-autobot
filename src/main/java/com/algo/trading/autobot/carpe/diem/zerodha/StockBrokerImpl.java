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

import org.json.JSONException;
import org.slf4j.LoggerFactory;

import com.algo.trading.autobot.carpe.diem.config.AppContext;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;

public final class StockBrokerImpl implements StockBroker
{
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(StockBrokerImpl.class);

    public StockBrokerImpl()
    {
        // Will call internally.
    }

    @Override
    public KiteConnect createSession()
    {
        LOGGER.info("Creating stock broker session for trade.");
        return new KiteConnect(AppContext.getStockBroker().getKey(), AppContext.getStockBroker().hasLogs());
    }

    @Override
    public void destroySession(final KiteConnect kiteSession)
    {
        try {
            LOGGER.info("Destroying stock broker session.");
            kiteSession.logout();
        } catch (JSONException | IOException | KiteException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
