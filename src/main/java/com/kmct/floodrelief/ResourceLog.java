package com.kmct.floodrelief;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ResourceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private LocalDateTime timestamp;

    @ManyToOne
    private Resource resource;

    public ResourceLog() {}

    public ResourceLog(String action, Resource resource) {
        this.action = action;
        this.resource = resource;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getAction() { return action; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Resource getResource() { return resource; }
}