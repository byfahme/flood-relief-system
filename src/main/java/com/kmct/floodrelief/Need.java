package com.kmct.floodrelief;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Need {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private int quantity;

    @Enumerated(EnumType.STRING)
    private NeedStatus status = NeedStatus.OPEN;

    @ManyToOne
    private Camp camp;

    private LocalDateTime createdAt;

    // How much has been given so far. Nullable so existing rows still load.
    private Integer delivered;

    public Need() {}

    public Need(String type, int quantity, Camp camp) {
        this.type = type;
        this.quantity = quantity;
        this.camp = camp;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public NeedStatus getStatus() { return status; }
    public void setStatus(NeedStatus status) { this.status = status; }
    public Camp getCamp() { return camp; }
    public void setCamp(Camp camp) { this.camp = camp; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getDelivered() { return delivered == null ? 0 : delivered; }
    public void setDelivered(int delivered) { this.delivered = delivered; }

    public int getRemaining() { return quantity - getDelivered(); }
}