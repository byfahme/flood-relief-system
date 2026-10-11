package com.kmct.floodrelief;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {

    @Autowired
    private CampRepository campRepository;

    @Autowired
    private ResourceLogRepository resourceLogRepository;

    @GetMapping("/reports")
    public String getReportsPage(Model model) {
        model.addAttribute("camps", campRepository.findAll());
        model.addAttribute("logs", resourceLogRepository.findAll());
        return "reports";
    }
}