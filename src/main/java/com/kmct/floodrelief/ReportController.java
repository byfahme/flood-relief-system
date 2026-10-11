package com.kmct.floodrelief;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Collections;

@Controller
public class ReportController {

    @GetMapping("/reports")
    public String getReportsPage(Model model) {
        model.addAttribute("camps", Collections.emptyList());
        model.addAttribute("logs", Collections.emptyList());
        return "reports";
    }
}