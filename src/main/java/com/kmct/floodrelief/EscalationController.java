package com.kmct.floodrelief;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EscalationController {

    private final EscalationService escalationService;

    public EscalationController(EscalationService escalationService) {
        this.escalationService = escalationService;
    }

    @GetMapping("/escalate")
    public String escalate() {
        escalationService.escalateOpenNeeds();
        return "redirect:/camps";
    }
}