package com.kmct.floodrelief;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NeedsPageController {

    private final NeedRepository needRepository;

    public NeedsPageController(NeedRepository needRepository) {
        this.needRepository = needRepository;
    }

    @GetMapping("/needs")
    public String showNeeds(Model model) {
        model.addAttribute("needs", needRepository.findAll().stream()
                .filter(n -> n.getStatus() == NeedStatus.OPEN
                          || n.getStatus() == NeedStatus.ESCALATED)
                .toList());
        return "needs";
    }
}
