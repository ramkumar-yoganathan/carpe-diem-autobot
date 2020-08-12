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

@Document(collection = "orders")
public class EquityOrders
{

    private double buyPrice;

    @Id
    private String id;

    private double realisedPoints;

    private String sellOrderId;

    private String buyOrderId;

    private double sellPrice;

    private int strikeQuantity;

    private String strikeSymbol;

    private String buyTimeStamp;

    private String sellTimeStamp;

    public double getBuyPrice()
    {
        return buyPrice;
    }

    public String getId()
    {
        return id;
    }

    public double getRealisedPoints()
    {
        return realisedPoints;
    }

    public String getSellOrderId()
    {
        return sellOrderId;
    }

    public double getSellPrice()
    {
        return sellPrice;
    }

    public int getStrikeQuantity()
    {
        return strikeQuantity;
    }

    public String getStrikeSymbol()
    {
        return strikeSymbol;
    }

    public void setBuyPrice(final double buyPrice)
    {
        this.buyPrice = buyPrice;
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    public void setRealisedPoints(final double realisedPoints)
    {
        this.realisedPoints = realisedPoints;
    }

    public void setSellOrderId(final String sellOrderId)
    {
        this.sellOrderId = sellOrderId;
    }

    public void setSellPrice(final double sellPrice)
    {
        this.sellPrice = sellPrice;
    }

    public void setStrikeQuantity(final int strikeQuantity)
    {
        this.strikeQuantity = strikeQuantity;
    }

    public void setStrikeSymbol(final String strikeSymbol)
    {
        this.strikeSymbol = strikeSymbol;
    }

    public String getBuyOrderId()
    {
        return buyOrderId;
    }

    public void setBuyOrderId(final String buyOrderId)
    {
        this.buyOrderId = buyOrderId;
    }

    public String getBuyTimeStamp()
    {
        return buyTimeStamp;
    }

    public void setBuyTimeStamp(String buyTimeStamp)
    {
        this.buyTimeStamp = buyTimeStamp;
    }

    public String getSellTimeStamp()
    {
        return sellTimeStamp;
    }

    public void setSellTimeStamp(String sellTimeStamp)
    {
        this.sellTimeStamp = sellTimeStamp;
    }

}
