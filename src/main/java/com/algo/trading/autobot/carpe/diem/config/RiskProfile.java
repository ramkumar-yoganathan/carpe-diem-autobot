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

public enum RiskProfile
{
    AGGRESSIVE(5),
    MODERATE(8),
    CONSERVATIVE(13);

    RiskProfile(final int riskPoint)
    {
        this.riskPoint = riskPoint;
    }

    private int riskPoint;

    public int getPoint()
    {
        return riskPoint;
    }
}
