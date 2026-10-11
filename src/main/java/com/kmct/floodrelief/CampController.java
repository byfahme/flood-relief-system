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
        List<CampView> views = new ArrayList<>();

        for (Camp camp : campRepository.findAll()) {
            List<Resource> resources = resourceRepository.findAll().stream()
                    .filter(r -> r.getCamp() != null && r.getCamp().getId().equals(camp.getId()))
                    .toList();

            List<Need> needs = needRepository.findAll().stream()
                    .filter(n -> n.getCamp() != null && n.getCamp().getId().equals(camp.getId()))
                    .toList();

            views.add(new CampView(camp, resources, needs));
        }

        model.addAttribute("camps", views);
        model.addAttribute("camp", new Camp());
        return "camps";
    }

    @PostMapping("/camps")
    public String addCamp(@ModelAttribute Camp camp) {
        campRepository.save(camp);
        return "redirect:/camps";
    }

    // Simple holder so the template gets one object per camp
    public record CampView(Camp camp, List<Resource> resources, List<Need> needs) {}
}