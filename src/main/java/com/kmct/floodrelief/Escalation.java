package com.kmct.floodrelief;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Escalation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Need need;

    private LocalDateTime escalatedAt;

    public Escalation() {}

    public Escalation(Need need, LocalDateTime escalatedAt) {
        this.need = need;
        this.escalatedAt = escalatedAt;
    }

    public Long getId() { return id; }
    public Need getNeed() { return need; }
    public void setNeed(Need need) { this.need = need; }
    public LocalDateTime getEscalatedAt() { return escalatedAt; }
    public void setEscalatedAt(LocalDateTime escalatedAt) { this.escalatedAt = escalatedAt; }
}