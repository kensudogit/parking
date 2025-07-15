package com.parking.entity;

import org.seasar.doma.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_spots")
public class ParkingSpotDoma {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "spot_number")
    private String spotNumber;
    
    @Column(name = "spot_type")
    private SpotType spotType;
    
    @Column(name = "status")
    private SpotStatus status;
    
    @Column(name = "floor_level")
    private Integer floorLevel;
    
    @Column(name = "hourly_rate")
    private Double hourlyRate;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public ParkingSpotDoma() {
    }
    
    public ParkingSpotDoma(
            Long id,
            String spotNumber,
            SpotType spotType,
            SpotStatus status,
            Integer floorLevel,
            Double hourlyRate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.spotNumber = spotNumber;
        this.spotType = spotType;
        this.status = status;
        this.floorLevel = floorLevel;
        this.hourlyRate = hourlyRate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getSpotNumber() { return spotNumber; }
    public void setSpotNumber(String spotNumber) { this.spotNumber = spotNumber; }
    
    public SpotType getSpotType() { return spotType; }
    public void setSpotType(SpotType spotType) { this.spotType = spotType; }
    
    public SpotStatus getStatus() { return status; }
    public void setStatus(SpotStatus status) { this.status = status; }
    
    public Integer getFloorLevel() { return floorLevel; }
    public void setFloorLevel(Integer floorLevel) { this.floorLevel = floorLevel; }
    
    public Double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(Double hourlyRate) { this.hourlyRate = hourlyRate; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public enum SpotType {
        REGULAR, DISABLED, ELECTRIC_CHARGING, MOTORCYCLE, TRUCK
    }
    
    public enum SpotStatus {
        AVAILABLE, OCCUPIED, RESERVED, MAINTENANCE
    }
} 