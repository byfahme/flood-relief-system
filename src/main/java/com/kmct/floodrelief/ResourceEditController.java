package com.kmct.floodrelief;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ResourceEditController {

    private final ResourceRepository resourceRepository;
    private final ResourceLogRepository resourceLogRepository;

    public ResourceEditController(ResourceRepository resourceRepository,
                                  ResourceLogRepository resourceLogRepository) {
        this.resourceRepository = resourceRepository;
        this.resourceLogRepository = resourceLogRepository;
    }

    @GetMapping("/resources/{id}/edit")
    public String showEdit(@PathVariable Long id, Model model) {
        Resource resource = resourceRepository.findById(id).orElseThrow();
        model.addAttribute("resource", resource);
        return "edit-resource";
    }

    @PostMapping("/resources/{id}/edit")
    public String saveEdit(@PathVariable Long id,
                           @RequestParam String type,
                           @RequestParam int received,
                           @RequestParam int used) {
        Resource resource = resourceRepository.findById(id).orElseThrow();
        if (received < 0 || used < 0 || used > received) {
            return "redirect:/resources/" + id + "/edit?error=invalid";
        }
        resource.setType(type.trim());
        resource.setReceived(received);
        resource.setUsed(used);
        resourceRepository.save(resource);
        return "redirect:/resources";
    }

    // Block deletion while the resource has delivery history
    @PostMapping("/resources/{id}/delete")
    public String delete(@PathVariable Long id) {
        boolean hasLogs = resourceLogRepository.findAll().stream()
                .anyMatch(l -> l.getResource() != null && l.getResource().getId().equals(id));
        if (hasLogs) {
            return "redirect:/resources?error=inuse";
        }
        resourceRepository.deleteById(id);
        return "redirect:/resources";
    }
}