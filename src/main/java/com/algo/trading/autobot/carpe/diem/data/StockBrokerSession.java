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
package com.algo.trading.autobot.carpe.diem.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sessions")
public class StockBrokerSession
{
    private String accessToken;

    private String broker;

    private String loginTime;

    private String publicToken;

    private String timeStamp;

    private String userId;

    public String getAccessToken()
    {
        return accessToken;
    }

    public String getBroker()
    {
        return broker;
    }

    public String getLoginTime()
    {
        return loginTime;
    }

    public String getPublicToken()
    {
        return publicToken;
    }

    public String getTimeStamp()
    {
        return timeStamp;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setAccessToken(final String accessToken)
    {
        this.accessToken = accessToken;
    }

    public void setBroker(final String broker)
    {
        this.broker = broker;
    }

    public void setLoginTime(final Date loginTime)
    {
        this.loginTime = new SimpleDateFormat("yyyy-MM-dd").format(loginTime);
    }

    public void setPublicToken(final String publicToken)
    {
        this.publicToken = publicToken;
    }

    public void setTimeStamp(final String timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public void setUserId(final String userId)
    {
        this.userId = userId;
    }

}
