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

@Document(collection = "fostocks")
public class EquityStocks
{
    @Id
    private String id;

    private String instrument;

    private String symbol;

    public String getInstrument()
    {
        return instrument;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public void setInstrument(final String instrument)
    {
        this.instrument = instrument;
    }

    public void setSymbol(final String symbol)
    {
        this.symbol = symbol;
    }

}
