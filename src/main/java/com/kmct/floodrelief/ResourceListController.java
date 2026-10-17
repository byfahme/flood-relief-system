package com.kmct.floodrelief;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResourceListController {

    private final ResourceRepository resourceRepository;

    public ResourceListController(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @GetMapping("/resources")
    public String showResources(Model model) {
        model.addAttribute("resources", resourceRepository.findAll());
        return "resources";
    }
}
