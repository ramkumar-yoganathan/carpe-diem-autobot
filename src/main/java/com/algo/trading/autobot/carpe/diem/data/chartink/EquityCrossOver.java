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
package com.algo.trading.autobot.carpe.diem.data.chartink;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "crossovers")
public class EquityCrossOver
{
    @Id
    private String id;

    private String instrument;

    private String price;

    private String timestamp;

    public String getId()
    {
        return id;
    }

    public String getInstrument()
    {
        return instrument;
    }

    public String getPrice()
    {
        return price;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    public void setInstrument(final String instrument)
    {
        this.instrument = instrument;
    }

    public void setPrice(final String price)
    {
        this.price = price;
    }

    public void setTimestamp(final String timestamp)
    {
        this.timestamp = timestamp;
    }

}
