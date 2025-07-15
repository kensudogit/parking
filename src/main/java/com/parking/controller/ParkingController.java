package com.parking.controller;

import com.parking.entity.ParkingSpot;
import com.parking.entity.ParkingSession;
import com.parking.service.ParkingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/parking")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ParkingController {
    
    private final ParkingService parkingService;
    
    // Parking Spots endpoints
    @GetMapping("/spots")
    public ResponseEntity<List<ParkingSpot>> getAllParkingSpots() {
        List<ParkingSpot> spots = parkingService.getAllParkingSpots();
        return ResponseEntity.ok(spots);
    }
    
    @GetMapping("/spots/{id}")
    public ResponseEntity<ParkingSpot> getParkingSpotById(@PathVariable Long id) {
        Optional<ParkingSpot> spot = parkingService.getParkingSpotById(id);
        return spot.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/spots/number/{spotNumber}")
    public ResponseEntity<ParkingSpot> getParkingSpotByNumber(@PathVariable String spotNumber) {
        Optional<ParkingSpot> spot = parkingService.getParkingSpotByNumber(spotNumber);
        return spot.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/spots/available")
    public ResponseEntity<List<ParkingSpot>> getAvailableSpots() {
        List<ParkingSpot> spots = parkingService.getAvailableSpots();
        return ResponseEntity.ok(spots);
    }
    
    @GetMapping("/spots/available/{spotType}")
    public ResponseEntity<List<ParkingSpot>> getAvailableSpotsByType(@PathVariable ParkingSpot.SpotType spotType) {
        List<ParkingSpot> spots = parkingService.getAvailableSpotsByType(spotType);
        return ResponseEntity.ok(spots);
    }
    
    @PostMapping("/spots")
    public ResponseEntity<ParkingSpot> createParkingSpot(@Valid @RequestBody ParkingSpot parkingSpot) {
        try {
            ParkingSpot createdSpot = parkingService.createParkingSpot(parkingSpot);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSpot);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/spots/{id}")
    public ResponseEntity<ParkingSpot> updateParkingSpot(@PathVariable Long id, 
                                                        @Valid @RequestBody ParkingSpot parkingSpot) {
        try {
            ParkingSpot updatedSpot = parkingService.updateParkingSpot(id, parkingSpot);
            return ResponseEntity.ok(updatedSpot);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/spots/{id}")
    public ResponseEntity<Void> deleteParkingSpot(@PathVariable Long id) {
        try {
            parkingService.deleteParkingSpot(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Parking Sessions endpoints
    @PostMapping("/sessions/start")
    public ResponseEntity<ParkingSession> startParkingSession(@RequestBody Map<String, Object> request) {
        try {
            Long spotId = Long.valueOf(request.get("spotId").toString());
            String licensePlate = request.get("licensePlate").toString();
            
            ParkingSession session = parkingService.startParkingSession(spotId, licensePlate);
            return ResponseEntity.status(HttpStatus.CREATED).body(session);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/sessions/{sessionId}/end")
    public ResponseEntity<ParkingSession> endParkingSession(@PathVariable Long sessionId) {
        try {
            ParkingSession session = parkingService.endParkingSession(sessionId);
            return ResponseEntity.ok(session);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/sessions/end-by-license")
    public ResponseEntity<ParkingSession> endParkingSessionByLicensePlate(@RequestBody Map<String, String> request) {
        try {
            String licensePlate = request.get("licensePlate");
            ParkingSession session = parkingService.endParkingSessionByLicensePlate(licensePlate);
            return ResponseEntity.ok(session);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/sessions/{sessionId}/payment")
    public ResponseEntity<ParkingSession> updatePaymentStatus(@PathVariable Long sessionId,
                                                             @RequestBody Map<String, String> request) {
        try {
            ParkingSession.PaymentStatus paymentStatus = ParkingSession.PaymentStatus.valueOf(request.get("paymentStatus"));
            ParkingSession session = parkingService.updatePaymentStatus(sessionId, paymentStatus);
            return ResponseEntity.ok(session);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/sessions/license/{licensePlate}")
    public ResponseEntity<List<ParkingSession>> getParkingSessionsByLicensePlate(@PathVariable String licensePlate) {
        List<ParkingSession> sessions = parkingService.getParkingSessionsByLicensePlate(licensePlate);
        return ResponseEntity.ok(sessions);
    }
    
    @GetMapping("/sessions/active")
    public ResponseEntity<List<ParkingSession>> getActiveParkingSessions() {
        List<ParkingSession> sessions = parkingService.getActiveParkingSessions();
        return ResponseEntity.ok(sessions);
    }
    
    // Statistics endpoints
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getParkingStats() {
        Map<String, Object> stats = Map.of(
            "availableSpots", parkingService.getAvailableSpotsCount(),
            "occupiedSpots", parkingService.getOccupiedSpotsCount(),
            "activeSessions", parkingService.getActiveSessionsCount()
        );
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/stats/available-spots")
    public ResponseEntity<Map<String, Long>> getAvailableSpotsCount() {
        Map<String, Long> count = Map.of("availableSpots", parkingService.getAvailableSpotsCount());
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/stats/occupied-spots")
    public ResponseEntity<Map<String, Long>> getOccupiedSpotsCount() {
        Map<String, Long> count = Map.of("occupiedSpots", parkingService.getOccupiedSpotsCount());
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/stats/active-sessions")
    public ResponseEntity<Map<String, Long>> getActiveSessionsCount() {
        Map<String, Long> count = Map.of("activeSessions", parkingService.getActiveSessionsCount());
        return ResponseEntity.ok(count);
    }
} 