package com.kmct.floodrelief;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;

@Controller
public class DeliveryController {

    private final NeedRepository needRepository;
    private final ResourceRepository resourceRepository;
    private final ResourceLogRepository resourceLogRepository;

    public DeliveryController(NeedRepository needRepository,
                              ResourceRepository resourceRepository,
                              ResourceLogRepository resourceLogRepository) {
        this.needRepository = needRepository;
        this.resourceRepository = resourceRepository;
        this.resourceLogRepository = resourceLogRepository;
    }

    @PostMapping("/needs/{id}/give")
    public String give(@PathVariable Long id,
                       @RequestParam int amount,
                       @RequestParam(defaultValue = "false") boolean confirmed) {
        if (amount < 1) {
            return "redirect:/needs?error=amount";
        }

        Need need = needRepository.findById(id).orElseThrow();
        if (need.getCamp() == null) {
            return "redirect:/needs?error=nocamp";
        }
        if (amount > need.getRemaining()) {
            return "redirect:/needs?error=toomuch";
        }

        // Find stock for the same camp and item type with enough left
        Resource stock = resourceRepository.findAll().stream()
                .filter(r -> r.getCamp() != null && r.getCamp().getId().equals(need.getCamp().getId()))
                .filter(r -> r.getType() != null && r.getType().equalsIgnoreCase(need.getType()))
                .filter(r -> r.getAvailable() >= amount)
                .findFirst()
                .orElse(null);

        if (stock == null) {
            return "redirect:/needs?error=stock";
        }

        // Take the amount out of stock
        stock.setUsed(stock.getUsed() + amount);
        resourceRepository.save(stock);

        // Record progress on the need. The quantity stays as originally requested.
        need.setDelivered(need.getDelivered() + amount);
        if (need.getDelivered() >= need.getQuantity()) {
            need.setStatus(NeedStatus.RESOLVED);
        }
        needRepository.save(need);

        // Write the history entry
        String action = "Gave " + amount + " " + need.getType()
                + " to " + need.getCamp().getName()
                + (confirmed ? " (confirmed received)" : " (not confirmed)");
        resourceLogRepository.save(new ResourceLog(action, stock));

        return "redirect:/needs?given";
    }

    @GetMapping("/deliveries")
    public String showDeliveries(Model model) {
        model.addAttribute("logs", resourceLogRepository.findAll().stream()
                .filter(l -> l.getAction() != null && l.getAction().startsWith("Gave "))
                .sorted(Comparator.comparing(ResourceLog::getTimestamp).reversed())
                .toList());
        return "deliveries";
    }
}