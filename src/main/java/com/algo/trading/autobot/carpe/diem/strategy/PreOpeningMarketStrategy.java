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

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.json.JSONException;

import com.algo.trading.autobot.carpe.diem.config.AppContext;
import com.algo.trading.autobot.carpe.diem.data.EquityStocks;
import com.mgnt.utils.TimeUtils;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Quote;

public final class PreOpeningMarketStrategy
{

    private static final Logger LOGGER = Logger.getLogger(PreOpeningMarketStrategy.class.getName());

    public static void runBot()
    {
        int counter = -1;
        final Map<String, String> equityInstruments = new HashMap<>();
        final String[] eqStocks = new String[(int) AppContext.getEquityStocks().count()];
        final Iterator<EquityStocks> eqIterator = AppContext.getEquityStocks().findAll().iterator();
        while (eqIterator.hasNext()) {
            final EquityStocks equityStocks = eqIterator.next();
            final String eqInstrument = equityStocks.getInstrument();
            final String eqSymbol = equityStocks.getSymbol();
            eqStocks[++counter] = eqInstrument;
            equityInstruments.put(eqInstrument, eqSymbol);
        }

        while (true) {
            final HashMap<String, Double> stockBuyerStrength = new HashMap<>();
            final HashMap<String, Double> stockSellerStrength = new HashMap<>();
            try {
                final Map<String, Quote> allStocksQuotes = AppContext.getStockBroker().getSession().getQuote(eqStocks);
                if (!allStocksQuotes.isEmpty()) {
                    final Iterator<Entry<String, Quote>> quoteIterator = allStocksQuotes.entrySet().iterator();
                    while (quoteIterator.hasNext()) {
                        final Entry<String, Quote> equityQuote = quoteIterator.next();
                        final double buyQuantity = equityQuote.getValue().buyQuantity;
                        final double sellQuantity = equityQuote.getValue().sellQuantity;
                        final double buyerStrength = (buyQuantity / sellQuantity) * 100;
                        final double sellerStrength = (sellQuantity / buyQuantity) * 100;
                        if (buyerStrength > 0) {
                            stockBuyerStrength.putIfAbsent(equityInstruments.get(equityQuote.getKey()),
                                Double.parseDouble(new DecimalFormat("##.##").format(buyerStrength)));
                        } else {
                            if (sellerStrength > 0) {
                                stockSellerStrength.putIfAbsent(equityInstruments.get(equityQuote.getKey()),
                                    Double.parseDouble(new DecimalFormat("##.##").format(buyerStrength)));
                            }
                        }
                    }
                }
            } catch (JSONException | IOException | KiteException e) {
                LOGGER.info(e.getLocalizedMessage());
            }

            final TreeMap<String, Double> sortedBuyers = sortMapByValue(stockBuyerStrength);
            final TreeMap<String, Double> sortedSellers = sortMapByValue(stockSellerStrength);

            final Iterator<Entry<String, Double>> buyersIterator = sortedBuyers.entrySet().iterator();
            final Iterator<Entry<String, Double>> sellersIterator = sortedSellers.entrySet().iterator();

            int keyCounter = 0;

            LOGGER.info(String.format(">>>>>>>>>>>>>>>>>>>>>>>>>> BUY <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"));
            while (buyersIterator.hasNext() || (keyCounter++ > 10)) {
                final Entry<String, Double> buyerIndex = buyersIterator.next();
                LOGGER.info(String.format(">>>>> %s %2s", getDecimalFormat(2).format(buyerIndex.getValue()),
                    buyerIndex.getKey()));
            }

            keyCounter = 0;

            LOGGER.info(String.format(">>>>>>>>>>>>>>>>>>>>>>>>>> SELL <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"));
            while (sellersIterator.hasNext() || (keyCounter++ > 10)) {
                final Entry<String, Double> sellerIndex = sellersIterator.next();
                LOGGER.info(String.format(">>>>> %s %2s", getDecimalFormat(2).format(sellerIndex.getValue()),
                    sellerIndex.getKey()));
            }

            TimeUtils.sleepFor(10, TimeUnit.SECONDS);
        }
    }

    public static DecimalFormat getDecimalFormat(final int decimals)
    {
        final StringBuilder sb = new StringBuilder(decimals + 2);
        sb.append("#.");
        for (int i = 0; i < decimals; i++) {
            sb.append("0");
        }
        return new DecimalFormat(sb.toString());
    }

    public static TreeMap<String, Double> sortMapByValue(final HashMap<String, Double> map)
    {
        @SuppressWarnings({"unchecked", "rawtypes"})
        final Comparator<String> comparator = new ValueComparator(map);
        final TreeMap<String, Double> result = new TreeMap<>(comparator);
        result.putAll(map);
        return result;
    }

    private PreOpeningMarketStrategy()
    {
    }

}

class ValueComparator<K, V extends Comparable<V>> implements Comparator<K>
{

    HashMap<K, V> map = new HashMap<>();

    public ValueComparator(final HashMap<K, V> map)
    {
        this.map.putAll(map);
    }

    @Override
    public int compare(final K s1, final K s2)
    {
        return -map.get(s1).compareTo(map.get(s2));
    }
}
