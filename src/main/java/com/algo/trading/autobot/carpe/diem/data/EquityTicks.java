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

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ticks")
public class EquityTicks
{
    @Id
    private String id;

    private String strikeSymbol;

    private double tickClose;

    private double tickHigh;

    private double tickLow;

    private double tickOpen;

    private String timeStamp;

    public String getStrikeSymbol()
    {
        return strikeSymbol;
    }

    public double getTickClose()
    {
        return tickClose;
    }

    public double getTickHigh()
    {
        return tickHigh;
    }

    public double getTickLow()
    {
        return tickLow;
    }

    public double getTickOpen()
    {
        return tickOpen;
    }

    public String getTimeStamp()
    {
        return timeStamp;
    }

    public void setStrikeSymbol(final String strikeSymbol)
    {
        this.strikeSymbol = strikeSymbol;
    }

    public void setTickClose(final double tickClose)
    {
        this.tickClose = tickClose;
    }

    public void setTickHigh(final double tickHigh)
    {
        this.tickHigh = tickHigh;
    }

    public void setTickLow(final double tickLow)
    {
        this.tickLow = tickLow;
    }

    public void setTickOpen(final double tickOpen)
    {
        this.tickOpen = tickOpen;
    }

    public void setTimeStamp(final String timeStamp)
    {
        this.timeStamp = timeStamp;
    }
}
