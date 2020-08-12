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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.algo.trading.autobot.carpe.diem.zerodha.AccessToken;
import com.zerodhatech.kiteconnect.KiteConnect;

@ConfigurationProperties(prefix = "spring.boot.config.stock.broker")
@Component
public class StockBroker
{

    private String client;

    private String key;

    private String expiry;

    private boolean logs;

    private String password;

    private String pin;

    private String secret;

    private KiteConnect session;

    private String url;

    private String risk;

    private String reward;

    public String getClient()
    {
        return client;
    }

    public String getKey()
    {
        return key;
    }

    public String getPassword()
    {
        return password;
    }

    public String getPin()
    {
        return pin;
    }

    public String getSecret()
    {
        return secret;
    }

    public KiteConnect getSession()
    {
        if (session == null) {
            final AccessToken token = new AccessToken();
            token.createToken();
        }
        return session;
    }

    public String getUrl()
    {
        return url;
    }

    public boolean hasLogs()
    {
        return logs;
    }

    public void setClient(final String client)
    {
        this.client = client;
    }

    public void setKey(final String key)
    {
        this.key = key;
    }

    public void setLogs(final boolean logs)
    {
        this.logs = logs;
    }

    public void setPassword(final String password)
    {
        this.password = password;
    }

    public void setPin(final String pin)
    {
        this.pin = pin;
    }

    public void setSecret(final String secret)
    {
        this.secret = secret;
    }

    public void setSession(final KiteConnect session)
    {
        this.session = session;
    }

    public void setUrl(final String url)
    {
        this.url = url;
    }

    public String getExpiry()
    {
        return expiry;
    }

    public void setExpiry(final String expiry)
    {
        this.expiry = expiry;
    }

    public String getRisk()
    {
        return risk;
    }

    public void setRisk(String risk)
    {
        this.risk = risk;
    }

    public String getReward()
    {
        return reward;
    }

    public void setReward(String reward)
    {
        this.reward = reward;
    }

}
