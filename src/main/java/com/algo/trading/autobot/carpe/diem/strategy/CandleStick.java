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
package com.algo.trading.autobot.carpe.diem.strategy;

import java.util.Calendar;

public class CandleStick
{
    private double tickClosePrice;

    private double tickHighPrice;

    private double tickLowPrice;

    private double tickOpenPrice;

    public double getTickClosePrice()
    {
        return tickClosePrice;
    }

    public double getTickHighPrice()
    {
        return tickHighPrice;
    }

    public double getTickLowPrice()
    {
        return tickLowPrice;
    }

    public double getTickOpenPrice()
    {
        return tickOpenPrice;
    }

    public void setTickClosePrice(final double tickClosePrice)
    {
        this.tickClosePrice = tickClosePrice;
    }

    public void setTickHighPrice(final double tickHighPrice)
    {
        this.tickHighPrice = tickHighPrice;
    }

    public void setTickLowPrice(final double tickLowPrice)
    {
        this.tickLowPrice = tickLowPrice;
    }

    public void setTickOpenPrice(final double tickOpenPrice)
    {
        this.tickOpenPrice = tickOpenPrice;
    }

    @Override
    public String toString()
    {
        return Calendar.getInstance().getTime() + " -> " + tickOpenPrice + " | " + tickHighPrice + " | " + tickLowPrice
            + " | " + tickClosePrice;

    }

}
