package com.kmct.floodrelief;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ResourceController {

    private static final List<String> TYPES = List.of("food", "water", "medicine", "other");

    private final CampRepository campRepository;
    private final ResourceRepository resourceRepository;
    private final ResourceLogRepository resourceLogRepository;

    public ResourceController(CampRepository campRepository,
                              ResourceRepository resourceRepository,
                              ResourceLogRepository resourceLogRepository) {
        this.campRepository = campRepository;
        this.resourceRepository = resourceRepository;
        this.resourceLogRepository = resourceLogRepository;
    }

    @GetMapping("/resources/add")
    public String showForm(Model model) {
        loadFormData(model);
        return "add-resource";
    }

    @PostMapping("/resources/add")
    public String save(@RequestParam(required = false) Long campId,
                       @RequestParam(required = false) String type,
                       @RequestParam(required = false) Integer received,
                       Model model) {

        List<String> errors = new ArrayList<>();
        Camp camp = null;

        if (campId == null) {
            errors.add("Please choose a camp.");
        } else {
            camp = campRepository.findById(campId).orElse(null);
            if (camp == null) {
                errors.add("The selected camp does not exist.");
            }
        }

        if (type == null || type.isBlank()) {
            errors.add("Please choose a resource type.");
        } else if (!TYPES.contains(type)) {
            errors.add("Unknown resource type.");
        }

        if (received == null) {
            errors.add("Quantity received is required.");
        } else if (received <= 0) {
            errors.add("Quantity received must be more than zero.");
        }

        if (!errors.isEmpty()) {
            loadFormData(model);
            model.addAttribute("errors", errors);
            model.addAttribute("selectedCampId", campId);
            model.addAttribute("selectedType", type);
            model.addAttribute("enteredQuantity", received);
            return "add-resource";
        }

        Resource resource = resourceRepository.save(new Resource(type, received, 0, camp));
        resourceLogRepository.save(new ResourceLog(
                "ADDED " + received + " " + type + " to " + camp.getName(), resource));

        return "redirect:/resources/add?saved";
    }

    private void loadFormData(Model model) {
        model.addAttribute("camps", campRepository.findAll());
        model.addAttribute("types", TYPES);
    }
}