package com.parking.repository;

import com.parking.entity.ParkingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {
    
    List<ParkingSession> findByLicensePlate(String licensePlate);
    
    List<ParkingSession> findByStatus(ParkingSession.SessionStatus status);
    
    List<ParkingSession> findByPaymentStatus(ParkingSession.PaymentStatus paymentStatus);
    
    @Query("SELECT ps FROM ParkingSession ps WHERE ps.parkingSpot.id = :spotId AND ps.status = 'ACTIVE'")
    Optional<ParkingSession> findActiveSessionBySpotId(@Param("spotId") Long spotId);
    
    @Query("SELECT ps FROM ParkingSession ps WHERE ps.entryTime >= :startDate AND ps.entryTime <= :endDate")
    List<ParkingSession> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT ps FROM ParkingSession ps WHERE ps.licensePlate = :licensePlate AND ps.status = 'ACTIVE'")
    Optional<ParkingSession> findActiveSessionByLicensePlate(@Param("licensePlate") String licensePlate);
    
    @Query("SELECT COUNT(ps) FROM ParkingSession ps WHERE ps.status = 'ACTIVE'")
    long countActiveSessions();
    
    @Query("SELECT COUNT(ps) FROM ParkingSession ps WHERE ps.paymentStatus = :paymentStatus")
    long countByPaymentStatus(@Param("paymentStatus") ParkingSession.PaymentStatus paymentStatus);
} 