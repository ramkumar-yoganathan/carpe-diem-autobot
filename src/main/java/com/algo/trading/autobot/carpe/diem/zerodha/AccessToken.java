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
import java.util.Iterator;

import org.json.JSONException;
import org.slf4j.LoggerFactory;

import com.algo.trading.autobot.carpe.diem.config.AppContext;
import com.algo.trading.autobot.carpe.diem.data.StockBrokerSession;
import com.algo.trading.autobot.carpe.diem.utils.DateTimeUtils;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;

public final class AccessToken
{
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AccessToken.class);

    public AccessToken()
    {
        // Instance will be created.
    }

    public void createToken()
    {
        final StockBrokerImpl stockBroker = new StockBrokerImpl();
        final KiteConnect kiteSession = stockBroker.createSession();

        final Iterator<StockBrokerSession> allSessions = AppContext.getStockBrokerSession().findAll().iterator();

        while (allSessions.hasNext()) {
            final StockBrokerSession eachSession = allSessions.next();
            if (eachSession.getLoginTime().equalsIgnoreCase(DateTimeUtils.getToday())) {
                final String accessToken = eachSession.getAccessToken();
                kiteSession.setAccessToken(accessToken);
                AppContext.getStockBroker().setSession(kiteSession);
                return;
            }
        }

        final RequestAccessToken requestTokenGerenator = new RequestAccessToken();

        final String requestToken = requestTokenGerenator.createToken().returnToken();
        if (!requestToken.isEmpty()) {
            try {
                final User kiteUser =
                    kiteSession.generateSession(requestToken, AppContext.getStockBroker().getSecret());
                if (!kiteUser.accessToken.isEmpty()) {
                    kiteSession.setAccessToken(kiteUser.accessToken);
                    AppContext.getStockBroker().setSession(kiteSession);
                    saveSessionInformation(kiteUser);
                } else {
                    LOGGER.info("Access token is empty. [" + requestToken + "]. Fatal error occured.");
                    Thread.currentThread().interrupt();
                }
            } catch (JSONException | IOException | KiteException e) {
                LOGGER.error(e.getMessage());
            }
        } else {
            LOGGER.info("Request token is empty. [" + requestToken + "]. Fatal error occured.");
            Thread.currentThread().interrupt();
        }
    }

    private void saveSessionInformation(final User kiteUser)
    {
        final StockBrokerSession newSession = new StockBrokerSession();
        newSession.setUserId(kiteUser.userId);
        newSession.setAccessToken(kiteUser.accessToken);
        newSession.setPublicToken(kiteUser.publicToken);
        newSession.setBroker(kiteUser.broker);
        newSession.setLoginTime(kiteUser.loginTime);
        AppContext.getStockBrokerSession().save(newSession);
    }

    public String getToken()
    {
        final StockBrokerImpl stockBroker = new StockBrokerImpl();
        final KiteConnect kiteSession = stockBroker.createSession();

        final Iterator<StockBrokerSession> allSessions = AppContext.getStockBrokerSession().findAll().iterator();

        while (allSessions.hasNext()) {
            final StockBrokerSession eachSession = allSessions.next();
            if (eachSession.getLoginTime().equalsIgnoreCase(DateTimeUtils.getToday())) {
                final String accessToken = eachSession.getAccessToken();
                kiteSession.setAccessToken(accessToken);
                AppContext.getStockBroker().setSession(kiteSession);
                return accessToken;
            }
        }
        return null;
    }
}
