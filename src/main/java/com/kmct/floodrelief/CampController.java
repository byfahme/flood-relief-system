package com.kmct.floodrelief;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CampController {

    @Autowired
    private CampRepository campRepository;

    @GetMapping("/camps")
    public String getCampsPage(Model model) {
        model.addAttribute("camps", campRepository.findAll());
        model.addAttribute("camp", new Camp());
        return "camps";
    }

    @PostMapping("/camps")
    public String addCamp(@ModelAttribute Camp camp) {
        campRepository.save(camp);
        return "redirect:/camps";
    }
}