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

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "strikes")
public class EquityOptionsStrikePrice
{
    private long callInstrument;

    private String callSymbol;

    private int lotSize;

    private String instrumentType;

    private long putInstrument;

    private String putSymbol;

    private String timeStamp;

    public long getCallInstrument()
    {
        return callInstrument;
    }

    public String getCallSymbol()
    {
        return callSymbol;
    }

    public String getInstrumentType()
    {
        return instrumentType;
    }

    public long getPutInstrument()
    {
        return putInstrument;
    }

    public String getPutSymbol()
    {
        return putSymbol;
    }

    public void setCallInstrument(final long callInstrument)
    {
        this.callInstrument = callInstrument;
    }

    public void setCallSymbol(final String callSymbol)
    {
        this.callSymbol = callSymbol;
    }

    public void setInstrumentType(final String instrumentType)
    {
        this.instrumentType = instrumentType;
    }

    public void setPutInstrument(final long putInstrument)
    {
        this.putInstrument = putInstrument;
    }

    public void setPutSymbol(final String putSymbol)
    {
        this.putSymbol = putSymbol;
    }

    public int getLotSize()
    {
        return lotSize;
    }

    public void setLotSize(final int lotSize)
    {
        this.lotSize = lotSize;
    }

    public String getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp)
    {
        this.timeStamp = timeStamp;
    }

}
