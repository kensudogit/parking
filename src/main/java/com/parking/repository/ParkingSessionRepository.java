package com.parking.repository;

import com.parking.entity.ParkingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 駐車場セッションリポジトリ
 * 駐車場セッションのデータアクセスを管理
 */
@Repository
public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {
    
    /**
     * ナンバープレートでセッションを検索
     * @param licensePlate ナンバープレート
     * @return セッションリスト
     */
    List<ParkingSession> findByLicensePlate(String licensePlate);
    
    /**
     * 状態でセッションを検索
     * @param status セッション状態
     * @return 指定状態のセッションリスト
     */
    List<ParkingSession> findByStatus(ParkingSession.SessionStatus status);
    
    /**
     * 支払い状態でセッションを検索
     * @param paymentStatus 支払い状態
     * @return 指定支払い状態のセッションリスト
     */
    List<ParkingSession> findByPaymentStatus(ParkingSession.PaymentStatus paymentStatus);
    
    /**
     * スペットIDでアクティブなセッションを検索
     * @param spotId スペットID
     * @return アクティブなセッション（存在しない場合は空）
     */
    @Query("SELECT ps FROM ParkingSession ps WHERE ps.parkingSpot.id = :spotId AND ps.status = 'ACTIVE'")
    Optional<ParkingSession> findActiveSessionBySpotId(@Param("spotId") Long spotId);
    
    /**
     * 日付範囲でセッションを検索
     * @param startDate 開始日時
     * @param endDate 終了日時
     * @return 指定期間のセッションリスト
     */
    @Query("SELECT ps FROM ParkingSession ps WHERE ps.entryTime >= :startDate AND ps.entryTime <= :endDate")
    List<ParkingSession> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);
    
    /**
     * ナンバープレートでアクティブなセッションを検索
     * @param licensePlate ナンバープレート
     * @return アクティブなセッション（存在しない場合は空）
     */
    @Query("SELECT ps FROM ParkingSession ps WHERE ps.licensePlate = :licensePlate AND ps.status = 'ACTIVE'")
    Optional<ParkingSession> findActiveSessionByLicensePlate(@Param("licensePlate") String licensePlate);
    
    /**
     * アクティブなセッション数をカウント
     * @return アクティブなセッション数
     */
    @Query("SELECT COUNT(ps) FROM ParkingSession ps WHERE ps.status = 'ACTIVE'")
    long countActiveSessions();
    
    /**
     * 指定支払い状態のセッション数をカウント
     * @param paymentStatus 支払い状態
     * @return 指定支払い状態のセッション数
     */
    @Query("SELECT COUNT(ps) FROM ParkingSession ps WHERE ps.paymentStatus = :paymentStatus")
    long countByPaymentStatus(@Param("paymentStatus") ParkingSession.PaymentStatus paymentStatus);
} 