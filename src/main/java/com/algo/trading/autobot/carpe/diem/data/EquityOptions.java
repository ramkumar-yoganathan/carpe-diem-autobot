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

@Document(collection = "options")
public class EquityOptions
{
    @Id
    private String id;

    private String expiry;

    private String instrument;

    private String quantity;

    private String strike;

    private String symbol;

    public EquityOptions()
    {
    }

    public EquityOptions(final String strike)
    {
        this.strike = strike;
    }

    public EquityOptions(final String strike, final String expiry)
    {
        this.strike = strike;
        this.expiry = expiry;
    }

    public String getExpiry()
    {
        return expiry;
    }

    public String getInstrument()
    {
        return instrument;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public String getStrike()
    {
        return strike;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public void setExpiry(final String expiry)
    {
        this.expiry = expiry;
    }

    public void setInstrument(final String instrument)
    {
        this.instrument = instrument;
    }

    public void setQuantity(final String quantity)
    {
        this.quantity = quantity;
    }

    public void setStrike(final String strike)
    {
        this.strike = strike;
    }

    public void setSymbol(final String symbol)
    {
        this.symbol = symbol;
    }

}
