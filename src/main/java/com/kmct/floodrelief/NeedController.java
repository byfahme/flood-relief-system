package com.kmct.floodrelief;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class NeedController {

    private static final List<String> TYPES = List.of("food", "water", "medicine", "other");

    private final CampRepository campRepository;
    private final NeedRepository needRepository;

    public NeedController(CampRepository campRepository,
                          NeedRepository needRepository) {
        this.campRepository = campRepository;
        this.needRepository = needRepository;
    }

    @GetMapping("/needs/add")
    public String showForm(Model model) {
        loadFormData(model);
        return "add-need";
    }

    @PostMapping("/needs/add")
    public String save(@RequestParam(required = false) Long campId,
                       @RequestParam(required = false) String type,
                       @RequestParam(required = false) Integer quantity,
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
            errors.add("Please choose a need type.");
        } else if (!TYPES.contains(type)) {
            errors.add("Unknown need type.");
        }

        if (quantity == null) {
            errors.add("Quantity needed is required.");
        } else if (quantity <= 0) {
            errors.add("Quantity needed must be more than zero.");
        }

        if (!errors.isEmpty()) {
            loadFormData(model);
            model.addAttribute("errors", errors);
            model.addAttribute("selectedCampId", campId);
            model.addAttribute("selectedType", type);
            model.addAttribute("enteredQuantity", quantity);
            return "add-need";
        }

        Need need = new Need(type, quantity, camp);
        need.setStatus(NeedStatus.OPEN);
        needRepository.save(need);

        return "redirect:/needs/add?saved";
    }

    private void loadFormData(Model model) {
        model.addAttribute("camps", campRepository.findAll());
        model.addAttribute("types", TYPES);
    }
}