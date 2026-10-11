package com.kmct.floodrelief;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EscalationService {

    // How long a need can stay OPEN before it is escalated
    private static final Duration ESCALATE_AFTER = Duration.ofHours(24);

    private final NeedRepository needRepository;
    private final EscalationRepository escalationRepository;

    public EscalationService(NeedRepository needRepository,
                             EscalationRepository escalationRepository) {
        this.needRepository = needRepository;
        this.escalationRepository = escalationRepository;
    }

    @Transactional
    public int escalateOpenNeeds() {
        LocalDateTime cutoff = LocalDateTime.now().minus(ESCALATE_AFTER);

        List<Need> overdueNeeds = needRepository.findAll().stream()
                .filter(n -> n.getStatus() == NeedStatus.OPEN)
                .filter(n -> n.getCreatedAt() != null && n.getCreatedAt().isBefore(cutoff))
                .toList();

        for (Need need : overdueNeeds) {
            need.setStatus(NeedStatus.ESCALATED);
            needRepository.save(need);
            escalationRepository.save(new Escalation(need, LocalDateTime.now()));
        }

        return overdueNeeds.size();
    }
}