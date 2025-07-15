package com.parking.service;

import com.parking.entity.ParkingSpot;
import com.parking.entity.ParkingSession;
import com.parking.repository.ParkingSpotRepository;
import com.parking.repository.ParkingSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.Duration;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ParkingService {
    
    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingSessionRepository parkingSessionRepository;
    
    public List<ParkingSpot> getAllParkingSpots() {
        return parkingSpotRepository.findAll();
    }
    
    public Optional<ParkingSpot> getParkingSpotById(Long id) {
        return parkingSpotRepository.findById(id);
    }
    
    public Optional<ParkingSpot> getParkingSpotByNumber(String spotNumber) {
        return parkingSpotRepository.findBySpotNumber(spotNumber);
    }
    
    public List<ParkingSpot> getAvailableSpots() {
        return parkingSpotRepository.findByStatus(ParkingSpot.SpotStatus.AVAILABLE);
    }
    
    public List<ParkingSpot> getAvailableSpotsByType(ParkingSpot.SpotType spotType) {
        return parkingSpotRepository.findByStatusAndSpotType(ParkingSpot.SpotStatus.AVAILABLE, spotType);
    }
    
    public ParkingSpot createParkingSpot(ParkingSpot parkingSpot) {
        if (parkingSpotRepository.existsBySpotNumber(parkingSpot.getSpotNumber())) {
            throw new IllegalArgumentException("Parking spot number already exists: " + parkingSpot.getSpotNumber());
        }
        return parkingSpotRepository.save(parkingSpot);
    }
    
    public ParkingSpot updateParkingSpot(Long id, ParkingSpot parkingSpotDetails) {
        ParkingSpot parkingSpot = parkingSpotRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Parking spot not found with id: " + id));
        
        parkingSpot.setSpotNumber(parkingSpotDetails.getSpotNumber());
        parkingSpot.setSpotType(parkingSpotDetails.getSpotType());
        parkingSpot.setStatus(parkingSpotDetails.getStatus());
        parkingSpot.setFloorLevel(parkingSpotDetails.getFloorLevel());
        parkingSpot.setHourlyRate(parkingSpotDetails.getHourlyRate());
        
        return parkingSpotRepository.save(parkingSpot);
    }
    
    public void deleteParkingSpot(Long id) {
        if (!parkingSpotRepository.existsById(id)) {
            throw new IllegalArgumentException("Parking spot not found with id: " + id);
        }
        parkingSpotRepository.deleteById(id);
    }
    
    public ParkingSession startParkingSession(Long spotId, String licensePlate) {
        ParkingSpot parkingSpot = parkingSpotRepository.findById(spotId)
            .orElseThrow(() -> new IllegalArgumentException("Parking spot not found with id: " + spotId));
        
        if (parkingSpot.getStatus() != ParkingSpot.SpotStatus.AVAILABLE) {
            throw new IllegalStateException("Parking spot is not available: " + parkingSpot.getSpotNumber());
        }
        
        // Check if vehicle is already parked
        Optional<ParkingSession> existingSession = parkingSessionRepository
            .findActiveSessionByLicensePlate(licensePlate);
        if (existingSession.isPresent()) {
            throw new IllegalStateException("Vehicle is already parked: " + licensePlate);
        }
        
        // Create new parking session
        ParkingSession session = new ParkingSession();
        session.setParkingSpot(parkingSpot);
        session.setLicensePlate(licensePlate);
        session.setEntryTime(LocalDateTime.now());
        session.setStatus(ParkingSession.SessionStatus.ACTIVE);
        session.setPaymentStatus(ParkingSession.PaymentStatus.PENDING);
        
        // Update parking spot status
        parkingSpot.setStatus(ParkingSpot.SpotStatus.OCCUPIED);
        parkingSpotRepository.save(parkingSpot);
        
        return parkingSessionRepository.save(session);
    }
    
    public ParkingSession endParkingSession(Long sessionId) {
        ParkingSession session = parkingSessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Parking session not found with id: " + sessionId));
        
        if (session.getStatus() != ParkingSession.SessionStatus.ACTIVE) {
            throw new IllegalStateException("Parking session is not active: " + sessionId);
        }
        
        session.setExitTime(LocalDateTime.now());
        session.setStatus(ParkingSession.SessionStatus.COMPLETED);
        session.setTotalAmount(calculateParkingFee(session));
        
        // Update parking spot status
        ParkingSpot parkingSpot = session.getParkingSpot();
        parkingSpot.setStatus(ParkingSpot.SpotStatus.AVAILABLE);
        parkingSpotRepository.save(parkingSpot);
        
        return parkingSessionRepository.save(session);
    }
    
    public ParkingSession endParkingSessionByLicensePlate(String licensePlate) {
        ParkingSession session = parkingSessionRepository.findActiveSessionByLicensePlate(licensePlate)
            .orElseThrow(() -> new IllegalArgumentException("No active parking session found for: " + licensePlate));
        
        return endParkingSession(session.getId());
    }
    
    public ParkingSession updatePaymentStatus(Long sessionId, ParkingSession.PaymentStatus paymentStatus) {
        ParkingSession session = parkingSessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Parking session not found with id: " + sessionId));
        
        session.setPaymentStatus(paymentStatus);
        return parkingSessionRepository.save(session);
    }
    
    public List<ParkingSession> getParkingSessionsByLicensePlate(String licensePlate) {
        return parkingSessionRepository.findByLicensePlate(licensePlate);
    }
    
    public List<ParkingSession> getActiveParkingSessions() {
        return parkingSessionRepository.findByStatus(ParkingSession.SessionStatus.ACTIVE);
    }
    
    public BigDecimal calculateParkingFee(ParkingSession session) {
        if (session.getExitTime() == null) {
            throw new IllegalArgumentException("Exit time is not set for session: " + session.getId());
        }
        
        Duration duration = Duration.between(session.getEntryTime(), session.getExitTime());
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        
        // Round up to the nearest hour
        if (minutes > 0) {
            hours++;
        }
        
        // Minimum 1 hour charge
        if (hours == 0) {
            hours = 1;
        }
        
        BigDecimal hourlyRate = BigDecimal.valueOf(session.getParkingSpot().getHourlyRate());
        return hourlyRate.multiply(BigDecimal.valueOf(hours)).setScale(2, RoundingMode.HALF_UP);
    }
    
    public long getAvailableSpotsCount() {
        return parkingSpotRepository.countByStatus(ParkingSpot.SpotStatus.AVAILABLE);
    }
    
    public long getOccupiedSpotsCount() {
        return parkingSpotRepository.countByStatus(ParkingSpot.SpotStatus.OCCUPIED);
    }
    
    public long getActiveSessionsCount() {
        return parkingSessionRepository.countActiveSessions();
    }
} 