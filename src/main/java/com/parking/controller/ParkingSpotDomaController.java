package com.parking.controller;

import com.parking.entity.ParkingSpotDoma;
import com.parking.service.ParkingSpotDomaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doma/parking-spots")
public class ParkingSpotDomaController {
    
    @Autowired
    private ParkingSpotDomaService parkingSpotDomaService;
    
    @GetMapping
    public ResponseEntity<List<ParkingSpotDoma>> getAllParkingSpots() {
        List<ParkingSpotDoma> spots = parkingSpotDomaService.getAllParkingSpots();
        return ResponseEntity.ok(spots);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpotDoma> getParkingSpotById(@PathVariable Long id) {
        ParkingSpotDoma spot = parkingSpotDomaService.getParkingSpotById(id);
        if (spot == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spot);
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<ParkingSpotDoma>> getAvailableSpots() {
        List<ParkingSpotDoma> spots = parkingSpotDomaService.getAvailableSpots();
        return ResponseEntity.ok(spots);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ParkingSpotDoma>> getSpotsByStatus(@PathVariable ParkingSpotDoma.SpotStatus status) {
        List<ParkingSpotDoma> spots = parkingSpotDomaService.getSpotsByStatus(status);
        return ResponseEntity.ok(spots);
    }
    
    @GetMapping("/type/{spotType}")
    public ResponseEntity<List<ParkingSpotDoma>> getSpotsByType(@PathVariable ParkingSpotDoma.SpotType spotType) {
        List<ParkingSpotDoma> spots = parkingSpotDomaService.getSpotsByType(spotType);
        return ResponseEntity.ok(spots);
    }
    
    @PostMapping
    public ResponseEntity<ParkingSpotDoma> createParkingSpot(
            @RequestParam String spotNumber,
            @RequestParam ParkingSpotDoma.SpotType spotType,
            @RequestParam(required = false) Integer floorLevel,
            @RequestParam Double hourlyRate) {
        
        ParkingSpotDoma newSpot = parkingSpotDomaService.createParkingSpot(
            spotNumber, spotType, floorLevel, hourlyRate
        );
        return ResponseEntity.ok(newSpot);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateParkingSpotStatus(
            @PathVariable Long id,
            @RequestParam ParkingSpotDoma.SpotStatus status) {
        
        boolean updated = parkingSpotDomaService.updateParkingSpotStatus(id, status);
        if (updated) {
            return ResponseEntity.ok("Parking spot status updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteParkingSpot(@PathVariable Long id) {
        boolean deleted = parkingSpotDomaService.deleteParkingSpot(id);
        if (deleted) {
            return ResponseEntity.ok("Parking spot deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
} 