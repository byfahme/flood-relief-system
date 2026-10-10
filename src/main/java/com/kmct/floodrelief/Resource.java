package com.kmct.floodrelief;

import jakarta.persistence.*;

@Entity
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;       // food, water, medicine, etc.
    private int received;
    private int used;

    @ManyToOne
    private Camp camp;

    public Resource() {}

    public Resource(String type, int received, int used, Camp camp) {
        this.type = type;
        this.received = received;
        this.used = used;
        this.camp = camp;
    }

    public Long getId() { return id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getReceived() { return received; }
    public void setReceived(int received) { this.received = received; }
    public int getUsed() { return used; }
    public void setUsed(int used) { this.used = used; }
    public Camp getCamp() { return camp; }
    public void setCamp(Camp camp) { this.camp = camp; }

    public int getAvailable() {
        return received - used;
    }
}