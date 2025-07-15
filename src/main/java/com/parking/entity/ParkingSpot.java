package com.parking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_spots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "spot_number", unique = true, nullable = false)
    private String spotNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "spot_type", nullable = false)
    private SpotType spotType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SpotStatus status = SpotStatus.AVAILABLE;
    
    @Column(name = "floor_level")
    private Integer floorLevel;
    
    @Column(name = "hourly_rate", nullable = false)
    private Double hourlyRate;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum SpotType {
        REGULAR, DISABLED, ELECTRIC_CHARGING, MOTORCYCLE, TRUCK
    }
    
    public enum SpotStatus {
        AVAILABLE, OCCUPIED, RESERVED, MAINTENANCE
    }
} 