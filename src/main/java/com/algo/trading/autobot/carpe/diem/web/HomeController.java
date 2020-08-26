package com.algo.trading.autobot.carpe.diem.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.algo.trading.autobot.carpe.diem.data.EquityOrdersRepo;

@Controller
public class HomeController
{
    @Autowired
    private EquityOrdersRepo equityOrdersRepo;

    @GetMapping("/home")
    public String getAllOrders(final Model eqOrders)
    {
        eqOrders.addAttribute("home", equityOrdersRepo.findAll());
        return "home";
    }
}
