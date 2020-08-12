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

public enum DataRange
{
    FIFTEEN_MINUTE(15),
    FIVE_MINUTE(5),
    ONE_DAY(1),
    ONE_HOUR(60),
    ONE_MINUTE(1),
    TEN_DAYS(10),
    TEN_MINUTE(10),
    THIRTY_MINUTE(30),
    THREE_MINUTE(3),
    TWENTY_DAYS(20),
    TWO_MINUTE(2);

    private int dateRange;

    DataRange(final int dataRange)
    {
        dateRange = dataRange;
    }

    public int get()
    {
        return dateRange;
    }
}
