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
package com.algo.trading.autobot.carpe.diem;

import java.util.List;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.algo.trading.autobot.carpe.diem.config.AppContext;
import com.algo.trading.autobot.carpe.diem.config.StockBroker;
import com.algo.trading.autobot.carpe.diem.data.EquityFuturesRepo;
import com.algo.trading.autobot.carpe.diem.data.EquityOptionsRepo;
import com.algo.trading.autobot.carpe.diem.data.EquityOptionsStrikePriceRepo;
import com.algo.trading.autobot.carpe.diem.data.EquityOrdersRepo;
import com.algo.trading.autobot.carpe.diem.data.EquityTicksRepo;
import com.algo.trading.autobot.carpe.diem.data.StockBrokerSession;
import com.algo.trading.autobot.carpe.diem.data.StockBrokerSessionRepo;
import com.algo.trading.autobot.carpe.diem.jobs.NiftyCallBuyJobScheduler;
import com.algo.trading.autobot.carpe.diem.jobs.NiftyPutBuyJobScheduler;
import com.algo.trading.autobot.carpe.diem.utils.CommonUtils;
import com.zerodhatech.kiteconnect.KiteConnect;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class CarpeDiamApp implements CommandLineRunner
{

    @Autowired
    StockBroker stockBroker;

    @Autowired
    StockBrokerSessionRepo stockBrokerSession;

    @Autowired
    EquityOptionsStrikePriceRepo optionsStrike;

    @Autowired
    EquityFuturesRepo equityFuturesRepo;

    @Autowired
    EquityOptionsRepo equityOptionsRepo;

    @Autowired
    EquityOrdersRepo equityOrdersRepo;

    @Autowired
    EquityTicksRepo equityTicksRepo;

    private static final String DEFAULT_GROUP = "AUTOBOT";

    private static final String BUY_CALL = "BUY_CALL";

    private static final String BUY_PUT = "BUY_PUT";

    public static void main(final String[] args)
    {
        SpringApplication.run(CarpeDiamApp.class, args);
    }

    @Override
    public void run(final String... args) throws Exception
    {
        /** App Configuration Load. **/
        AppContext.setStockBroker(stockBroker);
        AppContext.setStockBrokerSession(stockBrokerSession);
        AppContext.setOptionStrikePrice(optionsStrike);
        AppContext.setOptionsRepository(equityOptionsRepo);
        AppContext.setFuturesRepository(equityFuturesRepo);
        AppContext.setOrdersRepository(equityOrdersRepo);
        AppContext.setTicksRepository(equityTicksRepo);

        /** Stock Broker Connect. **/
        final KiteConnect kiteConnect =
            new KiteConnect(AppContext.getStockBroker().getKey(), AppContext.getStockBroker().hasLogs());
        final List<StockBrokerSession> brokerSession =
            AppContext.getStockBrokerSession().findByLoginTime(CommonUtils.getToday());
        if (!brokerSession.isEmpty()) {
            kiteConnect.setAccessToken(brokerSession.get(0).getAccessToken());
            AppContext.getStockBroker().setSession(kiteConnect);
        }

        /** Quartz Scheduler. **/
        final SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        final Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();

        /** Nifty CE Buy. **/
        final Trigger runCallBuyTrigger = TriggerBuilder.newTrigger().withIdentity(BUY_CALL).build();
        final JobDetail algoNiftyCallBuyJob =
            JobBuilder.newJob(NiftyCallBuyJobScheduler.class).withIdentity(BUY_CALL, DEFAULT_GROUP).build();
        scheduler.scheduleJob(algoNiftyCallBuyJob, runCallBuyTrigger);

        /** Nifty PE Buy. **/
        final Trigger runPutBuyTrigger = TriggerBuilder.newTrigger().withIdentity(BUY_PUT).build();
        final JobDetail algoNiftyPutBuyJob =
            JobBuilder.newJob(NiftyPutBuyJobScheduler.class).withIdentity(BUY_PUT, DEFAULT_GROUP).build();
        scheduler.scheduleJob(algoNiftyPutBuyJob, runPutBuyTrigger);
    }
}
