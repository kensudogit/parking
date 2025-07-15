package com.parking.repository;

import com.parking.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    
    Optional<ParkingSpot> findBySpotNumber(String spotNumber);
    
    List<ParkingSpot> findByStatus(ParkingSpot.SpotStatus status);
    
    List<ParkingSpot> findBySpotType(ParkingSpot.SpotType spotType);
    
    List<ParkingSpot> findByFloorLevel(Integer floorLevel);
    
    @Query("SELECT ps FROM ParkingSpot ps WHERE ps.status = :status AND ps.spotType = :spotType")
    List<ParkingSpot> findByStatusAndSpotType(@Param("status") ParkingSpot.SpotStatus status, 
                                             @Param("spotType") ParkingSpot.SpotType spotType);
    
    @Query("SELECT COUNT(ps) FROM ParkingSpot ps WHERE ps.status = :status")
    long countByStatus(@Param("status") ParkingSpot.SpotStatus status);
    
    @Query("SELECT COUNT(ps) FROM ParkingSpot ps WHERE ps.spotType = :spotType")
    long countBySpotType(@Param("spotType") ParkingSpot.SpotType spotType);
    
    boolean existsBySpotNumber(String spotNumber);
} 