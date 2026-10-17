package com.kmct.floodrelief;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CampEditController {

    private final CampRepository campRepository;
    private final ResourceRepository resourceRepository;
    private final NeedRepository needRepository;

    public CampEditController(CampRepository campRepository,
                              ResourceRepository resourceRepository,
                              NeedRepository needRepository) {
        this.campRepository = campRepository;
        this.resourceRepository = resourceRepository;
        this.needRepository = needRepository;
    }

    @GetMapping("/camps/{id}/edit")
    public String showEdit(@PathVariable Long id, Model model) {
        Camp camp = campRepository.findById(id).orElseThrow();
        model.addAttribute("camp", camp);
        return "edit-camp";
    }

    @PostMapping("/camps/{id}/edit")
    public String saveEdit(@PathVariable Long id,
                           @RequestParam String name,
                           @RequestParam String location,
                           @RequestParam(defaultValue = "false") boolean received,
                           @RequestParam(defaultValue = "") String updateNote) {
        Camp camp = campRepository.findById(id).orElseThrow();
        camp.setName(name.trim());
        camp.setLocation(location.trim());
        camp.setReceived(received);
        camp.setUpdateNote(updateNote.trim());
        campRepository.save(camp);
        return "redirect:/camps";
    }

    // Block deletion while the camp still has resources or needs
    @PostMapping("/camps/{id}/delete")
    public String delete(@PathVariable Long id) {
        boolean hasResources = resourceRepository.findAll().stream()
                .anyMatch(r -> r.getCamp() != null && r.getCamp().getId().equals(id));
        boolean hasNeeds = needRepository.findAll().stream()
                .anyMatch(n -> n.getCamp() != null && n.getCamp().getId().equals(id));

        if (hasResources || hasNeeds) {
            return "redirect:/camps?error=inuse";
        }
        campRepository.deleteById(id);
        return "redirect:/camps";
    }
}