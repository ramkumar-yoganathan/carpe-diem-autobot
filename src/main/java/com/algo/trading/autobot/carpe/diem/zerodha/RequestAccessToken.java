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

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.LoggerFactory;

import com.algo.trading.autobot.carpe.diem.config.AppContext;
import com.mgnt.utils.TimeUtils;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Kite Connect Request Access Token Generator. The Kite Connect api requires request access token to generate the
 * access token. Using selenium to does this job.
 *
 * @version $Id$
 */
public final class RequestAccessToken
{
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RequestAccessToken.class);

    private String requestToken = null;

    public RequestAccessToken()
    {
        // Instance will be created from app.
    }

    /**
     * Create request access token.
     *
     * @return
     */
    public RequestAccessToken createToken()
    {
        final String loginUrl = AppContext.getStockBroker().getUrl();
        final String loginUser = AppContext.getStockBroker().getClient();
        final String loginPassword = AppContext.getStockBroker().getPassword();
        final String loginPin = AppContext.getStockBroker().getPin();

        LOGGER.info("Downloading chrome brower server application");
        WebDriverManager.chromedriver().setup();

        LOGGER.info("Downloaded chrome version " + WebDriverManager.chromedriver().getDownloadedVersion());
        final ChromeDriver driverInstance = new ChromeDriver();
        driverInstance.manage().window().maximize();
        driverInstance.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driverInstance.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driverInstance.get(loginUrl);

        LOGGER.info("Chrome browser loaded with the url " + loginUrl);

        ExpectedConditions.presenceOfElementLocated(By.id("userid")).apply(driverInstance).sendKeys(loginUser);
        ExpectedConditions.presenceOfElementLocated(By.id("password")).apply(driverInstance).sendKeys(loginPassword);
        ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='actions']//button")).apply(driverInstance)
            .click();
        LOGGER.info("Username and password entered successfully.");
        ExpectedConditions.presenceOfElementLocated(By.id("pin")).apply(driverInstance).sendKeys(loginPin);
        ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='actions']//button")).apply(driverInstance)
            .click();
        LOGGER.info("Pin entered successfully.");
        ExpectedConditions.jsReturnsValue("return document.readyState");
        LOGGER.info("The current url value is " + driverInstance.getCurrentUrl());
        ExpectedConditions.urlContains("status=success");
        TimeUtils.sleepFor(10, TimeUnit.SECONDS);
        final String currentUrl = driverInstance.getCurrentUrl();
        if (currentUrl.contains("status=success")) {
            requestToken = currentUrl.split("request_token=")[1];
            requestToken = requestToken.replace("&action=login&status=success", "");
            LOGGER.debug("The request access token parsed successfully as  " + requestToken);
            driverInstance.quit();
        }

        return this;
    }

    /**
     * Return the generated access token.
     *
     * @return
     */
    public String returnToken()
    {
        return requestToken;
    }
}
