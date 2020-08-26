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

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

import com.algo.trading.autobot.carpe.diem.config.AppContext;
import com.algo.trading.autobot.carpe.diem.data.EquityOptions;
import com.algo.trading.autobot.carpe.diem.data.EquityOptionsStrikePrice;
import com.algo.trading.autobot.carpe.diem.utils.CommonUtils;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.LTPQuote;

public final class OptionsStrikePrice
{

    private static final String BANK_NIFTY_MONTH = "BANKNIFTY20";

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OptionsStrikePrice.class);

    private static final String MONTHLY_FUTURE = "FUT";

    private static final String NIFTY_MONTH = "NIFTY20";

    private static final Sort SORT_ORDER = Sort.by("id").descending();

    public OptionsStrikePrice()
    {
        // Will invoke from main
    }

    public EquityOptionsStrikePrice queryAndReturnEquityFuturePriceEquivalentStrikePrice(final String tradingSymbol)
    {
        EquityOptionsStrikePrice targetDocument = null;
        queryAndUpdateRecentStrikePrice();
        final Iterator<EquityOptionsStrikePrice> documentIterator =
            AppContext.getOptionsStrike().findAll(SORT_ORDER).iterator();
        while (documentIterator.hasNext()) {
            targetDocument = documentIterator.next();
            final boolean hasMatchFound = targetDocument.getInstrumentType().equalsIgnoreCase(tradingSymbol);
            if (hasMatchFound) {
                break;
            }
        }
        return targetDocument;
    }

    public EquityOptionsStrikePrice queryAndReturnEquityFuturePriceEquivalentWeeklyStrikePrice(
        final String tradingSymbol)
    {
        EquityOptionsStrikePrice targetDocument = null;
        CommonUtils.getCurrentWeekExpiryDate();
        final Iterator<EquityOptionsStrikePrice> documentIterator =
            AppContext.getOptionsStrike().findAll(SORT_ORDER).iterator();
        while (documentIterator.hasNext()) {
            targetDocument = documentIterator.next();
            final boolean hasMatchFound = targetDocument.getInstrumentType().equalsIgnoreCase(tradingSymbol);
            if (hasMatchFound) {
                break;
            }
        }
        return targetDocument;
    }

    public void queryAndUpdateRecentStrikePrice()
    {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        final String monthContract =
            Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()).toUpperCase();

        final String tokenNifty = AppContext.getEquityFutures()
            .findBySymbol(
                new StringBuilder().append(NIFTY_MONTH).append(monthContract).append(MONTHLY_FUTURE).toString())
            .stream().collect(Collectors.toList()).get(0).getInstrument();

        final String tokenBankNifty = AppContext.getEquityFutures()
            .findBySymbol(
                new StringBuilder().append(BANK_NIFTY_MONTH).append(monthContract).append(MONTHLY_FUTURE).toString())
            .stream().collect(Collectors.toList()).get(0).getInstrument();

        if (!tokenNifty.isEmpty() && !tokenBankNifty.isEmpty()) {
            final String[] futureTokens = new String[2];
            futureTokens[0] = tokenNifty;
            futureTokens[1] = tokenBankNifty;
            try {
                final Map<String, LTPQuote> futurePrices =
                    AppContext.getStockBroker().getSession().getLTP(futureTokens);
                if (!futurePrices.isEmpty()) {
                    final double ltpNifty = futurePrices.get(tokenNifty).lastPrice;
                    final String niftyStrike = new DecimalFormat("#").format(ltpNifty - (ltpNifty % 100));
                    final double ltpBankNifty = futurePrices.get(tokenBankNifty).lastPrice;
                    final String bankNiftyStrike = new DecimalFormat("#").format(ltpBankNifty - (ltpBankNifty % 100));

                    final boolean hasWeeklyExpiry = AppContext.getStockBroker().getExpiry().equalsIgnoreCase("WEEKLY");

                    final List<EquityOptions> niftyStrikes;
                    final List<EquityOptions> bankNiftyStrikes;

                    if (hasWeeklyExpiry) {
                        niftyStrikes = AppContext.getEquityOptions().findByStrikeAndExpiry(niftyStrike,
                            CommonUtils.getCurrentWeekExpiryDate());
                        bankNiftyStrikes = AppContext.getEquityOptions().findByStrikeAndExpiry(bankNiftyStrike,
                            CommonUtils.getCurrentWeekExpiryDate());
                    } else {
                        niftyStrikes = AppContext.getEquityOptions().findByStrikeAndExpiry(niftyStrike,
                            CommonUtils.getCurrentMonthExpiryDate());
                        bankNiftyStrikes = AppContext.getEquityOptions().findByStrikeAndExpiry(bankNiftyStrike,
                            CommonUtils.getCurrentMonthExpiryDate());
                    }

                    AppContext.getOptionsStrike().deleteAll();

                    EquityOptionsStrikePrice optionsStrikePrice = new EquityOptionsStrikePrice();
                    optionsStrikePrice.setInstrumentType("NIFTY");
                    optionsStrikePrice.setTimeStamp(dateFormatter.format(Calendar.getInstance().getTime()));
                    optionsStrikePrice.setCallSymbol(niftyStrikes.get(0).getSymbol());
                    optionsStrikePrice.setCallInstrument(Long.parseLong(niftyStrikes.get(0).getInstrument()));
                    optionsStrikePrice.setPutSymbol(niftyStrikes.get(1).getSymbol());
                    optionsStrikePrice.setPutInstrument(Long.parseLong(niftyStrikes.get(1).getInstrument()));
                    optionsStrikePrice.setLotSize(Integer.parseInt(niftyStrikes.get(0).getQuantity()));

                    AppContext.getOptionsStrike().save(optionsStrikePrice);

                    optionsStrikePrice = new EquityOptionsStrikePrice();
                    optionsStrikePrice.setInstrumentType("BANKNIFTY");
                    optionsStrikePrice.setTimeStamp(dateFormatter.format(Calendar.getInstance().getTime()));
                    optionsStrikePrice.setCallSymbol(bankNiftyStrikes.get(0).getSymbol());
                    optionsStrikePrice.setCallInstrument(Long.parseLong(bankNiftyStrikes.get(0).getInstrument()));
                    optionsStrikePrice.setPutSymbol(bankNiftyStrikes.get(1).getSymbol());
                    optionsStrikePrice.setPutInstrument(Long.parseLong(bankNiftyStrikes.get(1).getInstrument()));
                    optionsStrikePrice.setLotSize(Integer.parseInt(bankNiftyStrikes.get(0).getQuantity()));

                    AppContext.getOptionsStrike().save(optionsStrikePrice);

                }
            } catch (JSONException | IOException | KiteException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }
}
