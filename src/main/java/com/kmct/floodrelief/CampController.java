package com.kmct.floodrelief;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CampController {

    private final CampRepository campRepository;
    private final ResourceRepository resourceRepository;
    private final NeedRepository needRepository;

    public CampController(CampRepository campRepository,
                          ResourceRepository resourceRepository,
                          NeedRepository needRepository) {
        this.campRepository = campRepository;
        this.resourceRepository = resourceRepository;
        this.needRepository = needRepository;
    }

    @GetMapping("/camps")
    public String showCamps(Model model) {
        List<Resource> allStock = resourceRepository.findAll();
        List<Need> allNeeds = needRepository.findAll();
        List<CampView> views = new ArrayList<>();

        for (Camp camp : campRepository.findAll()) {
            List<Resource> stock = allStock.stream()
                    .filter(r -> r.getCamp() != null && r.getCamp().getId().equals(camp.getId()))
                    .toList();

            List<Need> stillNeeded = allNeeds.stream()
                    .filter(n -> n.getCamp() != null && n.getCamp().getId().equals(camp.getId()))
                    .filter(n -> n.getStatus() == NeedStatus.OPEN
                              || n.getStatus() == NeedStatus.ESCALATED)
                    .filter(n -> n.getRemaining() > 0)
                    .toList();

            views.add(new CampView(camp, stock, stillNeeded));
        }

        model.addAttribute("camps", views);
        return "camps";
    }

    @GetMapping("/camps/add")
    public String showAddCamp(Model model) {
        model.addAttribute("camp", new Camp());
        return "add-camp";
    }

    @PostMapping("/camps")
    public String addCamp(@ModelAttribute Camp camp) {
        campRepository.save(camp);
        return "redirect:/camps";
    }

    // One object per camp, holding only what the card needs
    public record CampView(Camp camp, List<Resource> resources, List<Need> stillNeeded) {}
}