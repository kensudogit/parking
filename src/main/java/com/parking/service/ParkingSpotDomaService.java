package com.parking.service;

import com.parking.dao.ParkingSpotDao;
import com.parking.entity.ParkingSpotDoma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ParkingSpotDomaService {
    
    @Autowired
    private ParkingSpotDao parkingSpotDao;
    
    public List<ParkingSpotDoma> getAllParkingSpots() {
        return parkingSpotDao.selectAll();
    }
    
    public ParkingSpotDoma getParkingSpotById(Long id) {
        return parkingSpotDao.selectById(id);
    }
    
    public List<ParkingSpotDoma> getAvailableSpots() {
        return parkingSpotDao.selectAll().stream()
            .filter(spot -> spot.getStatus() == ParkingSpotDoma.SpotStatus.AVAILABLE)
            .collect(Collectors.toList());
    }
    
    public List<ParkingSpotDoma> getSpotsByStatus(ParkingSpotDoma.SpotStatus status) {
        return parkingSpotDao.selectAll().stream()
            .filter(spot -> spot.getStatus() == status)
            .collect(Collectors.toList());
    }
    
    public List<ParkingSpotDoma> getSpotsByType(ParkingSpotDoma.SpotType spotType) {
        return parkingSpotDao.selectAll().stream()
            .filter(spot -> spot.getSpotType() == spotType)
            .collect(Collectors.toList());
    }
    
    public ParkingSpotDoma createParkingSpot(String spotNumber, 
                                           ParkingSpotDoma.SpotType spotType,
                                           Integer floorLevel,
                                           Double hourlyRate) {
        LocalDateTime now = LocalDateTime.now();
        ParkingSpotDoma parkingSpot = new ParkingSpotDoma();
        parkingSpot.setSpotNumber(spotNumber);
        parkingSpot.setSpotType(spotType);
        parkingSpot.setStatus(ParkingSpotDoma.SpotStatus.AVAILABLE);
        parkingSpot.setFloorLevel(floorLevel);
        parkingSpot.setHourlyRate(hourlyRate);
        parkingSpot.setCreatedAt(now);
        parkingSpot.setUpdatedAt(now);
        
        parkingSpotDao.insert(parkingSpot);
        return parkingSpot;
    }
    
    public boolean updateParkingSpotStatus(Long id, ParkingSpotDoma.SpotStatus status) {
        ParkingSpotDoma existingSpot = parkingSpotDao.selectById(id);
        if (existingSpot == null) {
            return false;
        }
        
        existingSpot.setStatus(status);
        existingSpot.setUpdatedAt(LocalDateTime.now());
        
        return parkingSpotDao.update(existingSpot) > 0;
    }
    
    public boolean deleteParkingSpot(Long id) {
        ParkingSpotDoma existingSpot = parkingSpotDao.selectById(id);
        if (existingSpot == null) {
            return false;
        }
        
        return parkingSpotDao.delete(existingSpot) > 0;
    }
} 