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
package com.algo.trading.autobot.carpe.diem.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.algo.trading.autobot.carpe.diem.config.AppContext;
import com.algo.trading.autobot.carpe.diem.config.Globals;
import com.algo.trading.autobot.carpe.diem.strategy.CandleStickCallStrategy;

public class BankNiftyCallBuyJobScheduler implements Job
{
    @Override
    public void execute(final JobExecutionContext context) throws JobExecutionException
    {
        AppContext.getOptionsStrike().deleteAll();
        CandleStickCallStrategy.runBot(Globals.BANKNIFTY, Globals.CALL);
    }
}
