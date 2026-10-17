package com.kmct.floodrelief;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    // Show the edit form with the current values
    @GetMapping("/needs/{id}/edit")
    public String showEdit(@PathVariable Long id, Model model) {
        Need need = needRepository.findById(id).orElseThrow();
        model.addAttribute("need", need);
        return "edit-need";
    }

    // Save the changed type and quantity
    @PostMapping("/needs/{id}/edit")
    public String saveEdit(@PathVariable Long id,
                           @RequestParam String type,
                           @RequestParam int quantity) {
        if (quantity < 1) {
            return "redirect:/needs/" + id + "/edit";
        }
        Need need = needRepository.findById(id).orElseThrow();
        need.setType(type.trim());
        need.setQuantity(quantity);
        needRepository.save(need);
        return "redirect:/needs";
    }

    // Mark a need as met. It leaves the list because the list shows only OPEN and ESCALATED.
    @PostMapping("/needs/{id}/resolve")
    public String resolve(@PathVariable Long id) {
        Need need = needRepository.findById(id).orElseThrow();
        need.setStatus(NeedStatus.RESOLVED);
        needRepository.save(need);
        return "redirect:/needs";
    }

    // Remove a mistaken or duplicate need
    @PostMapping("/needs/{id}/delete")
    public String delete(@PathVariable Long id) {
        needRepository.deleteById(id);
        return "redirect:/needs";
    }
}