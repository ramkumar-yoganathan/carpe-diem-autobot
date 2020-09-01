package com.algo.trading.autobot.carpe.diem.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algo.trading.autobot.carpe.diem.data.chartink.EquityCrossOver;
import com.algo.trading.autobot.carpe.diem.data.chartink.EquityCrossOverRepo;

@RestController
@RequestMapping("/api/v1")
public class CrossOverController
{
    private static final Sort SORT_ORDER = Sort.by("id").descending();

    @Autowired
    private EquityCrossOverRepo eqCrossOverRepo;

    @GetMapping("/crossovers")
    public Iterable<EquityCrossOver> getAllCrossOverInstruments()
    {
        return eqCrossOverRepo.findAll();
    }

    @PostMapping("/crossovers/add")
    public void setCrossOverInstruments(@RequestBody final String chartinkAlertMessage)
    {
        final String alertMessage = chartinkAlertMessage.substring(chartinkAlertMessage.indexOf("Data:"));
        if (!alertMessage.isEmpty()) {
            final List<String> allInstruments = Arrays.asList(alertMessage.split(","));
            if (!allInstruments.isEmpty()) {
                String eqTimeStamp = allInstruments.get(allInstruments.size() - 1);
                eqTimeStamp = eqTimeStamp.replace("@", "").trim();
                final String eqInstrument = allInstruments.get(1).trim().split("-")[0];
                final String eqPrice = allInstruments.get(1).trim().split("-")[1];

                final EquityCrossOver eqCrossOver = new EquityCrossOver();
                eqCrossOver.setInstrument(eqInstrument);
                eqCrossOver.setPrice(eqPrice);
                eqCrossOver.setTimestamp(eqTimeStamp);
                eqCrossOverRepo.save(eqCrossOver);
            }
        }
    }

    @GetMapping("/crossovers/newest")
    public EquityCrossOver getNewestCrossOverInstruments()
    {
        final Iterable<EquityCrossOver> allCrossOverInstruments = eqCrossOverRepo.findAll(SORT_ORDER);
        final boolean hasRecord = allCrossOverInstruments.iterator().hasNext();
        if (hasRecord)
            return allCrossOverInstruments.iterator().next();

        return null;
    }
}
