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

/**
 * 駐車場管理サービス
 * 駐車場スペットとセッションのビジネスロジックを管理
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ParkingService {
    
    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingSessionRepository parkingSessionRepository;
    
    // ==================== 駐車場スペット管理メソッド ====================
    
    /**
     * すべての駐車場スペットを取得
     * @return 駐車場スペットのリスト
     */
    public List<ParkingSpot> getAllParkingSpots() {
        return parkingSpotRepository.findAll();
    }
    
    /**
     * IDで駐車場スペットを取得
     * @param id スペットID
     * @return 駐車場スペット（存在しない場合は空）
     */
    public Optional<ParkingSpot> getParkingSpotById(Long id) {
        return parkingSpotRepository.findById(id);
    }
    
    /**
     * スペット番号で駐車場スペットを取得
     * @param spotNumber スペット番号
     * @return 駐車場スペット（存在しない場合は空）
     */
    public Optional<ParkingSpot> getParkingSpotByNumber(String spotNumber) {
        return parkingSpotRepository.findBySpotNumber(spotNumber);
    }
    
    /**
     * 利用可能な駐車場スペットを取得
     * @return 利用可能なスペットのリスト
     */
    public List<ParkingSpot> getAvailableSpots() {
        return parkingSpotRepository.findByStatus(ParkingSpot.SpotStatus.AVAILABLE);
    }
    
    /**
     * 指定タイプの利用可能な駐車場スペットを取得
     * @param spotType スペットタイプ
     * @return 利用可能なスペットのリスト
     */
    public List<ParkingSpot> getAvailableSpotsByType(ParkingSpot.SpotType spotType) {
        return parkingSpotRepository.findByStatusAndSpotType(ParkingSpot.SpotStatus.AVAILABLE, spotType);
    }
    
    /**
     * 新しい駐車場スペットを作成
     * @param parkingSpot 作成するスペット情報
     * @return 作成されたスペット
     * @throws IllegalArgumentException スペット番号が重複している場合
     */
    public ParkingSpot createParkingSpot(ParkingSpot parkingSpot) {
        if (parkingSpotRepository.existsBySpotNumber(parkingSpot.getSpotNumber())) {
            throw new IllegalArgumentException("Parking spot number already exists: " + parkingSpot.getSpotNumber());
        }
        return parkingSpotRepository.save(parkingSpot);
    }
    
    /**
     * 駐車場スペットを更新
     * @param id スペットID
     * @param parkingSpotDetails 更新するスペット情報
     * @return 更新されたスペット
     * @throws IllegalArgumentException スペットが見つからない場合
     */
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
    
    /**
     * 駐車場スペットを削除
     * @param id スペットID
     * @throws IllegalArgumentException スペットが見つからない場合
     */
    public void deleteParkingSpot(Long id) {
        if (!parkingSpotRepository.existsById(id)) {
            throw new IllegalArgumentException("Parking spot not found with id: " + id);
        }
        parkingSpotRepository.deleteById(id);
    }
    
    // ==================== 駐車場セッション管理メソッド ====================
    
    /**
     * 駐車場セッションを開始
     * @param spotId スペットID
     * @param licensePlate ナンバープレート
     * @return 作成されたセッション
     * @throws IllegalArgumentException スペットが見つからない場合
     * @throws IllegalStateException スペットが利用不可、または車両が既に駐車中の場合
     */
    public ParkingSession startParkingSession(Long spotId, String licensePlate) {
        ParkingSpot parkingSpot = parkingSpotRepository.findById(spotId)
            .orElseThrow(() -> new IllegalArgumentException("Parking spot not found with id: " + spotId));
        
        if (parkingSpot.getStatus() != ParkingSpot.SpotStatus.AVAILABLE) {
            throw new IllegalStateException("Parking spot is not available: " + parkingSpot.getSpotNumber());
        }
        
        // 車両が既に駐車中かチェック
        Optional<ParkingSession> existingSession = parkingSessionRepository
            .findActiveSessionByLicensePlate(licensePlate);
        if (existingSession.isPresent()) {
            throw new IllegalStateException("Vehicle is already parked: " + licensePlate);
        }
        
        // 新しい駐車場セッションを作成
        ParkingSession session = new ParkingSession();
        session.setParkingSpot(parkingSpot);
        session.setLicensePlate(licensePlate);
        session.setEntryTime(LocalDateTime.now());
        session.setStatus(ParkingSession.SessionStatus.ACTIVE);
        session.setPaymentStatus(ParkingSession.PaymentStatus.PENDING);
        
        // 駐車場スペットの状態を更新
        parkingSpot.setStatus(ParkingSpot.SpotStatus.OCCUPIED);
        parkingSpotRepository.save(parkingSpot);
        
        return parkingSessionRepository.save(session);
    }
    
    /**
     * 駐車場セッションを終了
     * @param sessionId セッションID
     * @return 終了されたセッション
     * @throws IllegalArgumentException セッションが見つからない場合
     * @throws IllegalStateException セッションがアクティブでない場合
     */
    public ParkingSession endParkingSession(Long sessionId) {
        ParkingSession session = parkingSessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Parking session not found with id: " + sessionId));
        
        if (session.getStatus() != ParkingSession.SessionStatus.ACTIVE) {
            throw new IllegalStateException("Parking session is not active: " + sessionId);
        }
        
        session.setExitTime(LocalDateTime.now());
        session.setStatus(ParkingSession.SessionStatus.COMPLETED);
        session.setTotalAmount(calculateParkingFee(session));
        
        // 駐車場スペットの状態を更新
        ParkingSpot parkingSpot = session.getParkingSpot();
        parkingSpot.setStatus(ParkingSpot.SpotStatus.AVAILABLE);
        parkingSpotRepository.save(parkingSpot);
        
        return parkingSessionRepository.save(session);
    }
    
    /**
     * ナンバープレートで駐車場セッションを終了
     * @param licensePlate ナンバープレート
     * @return 終了されたセッション
     * @throws IllegalArgumentException アクティブなセッションが見つからない場合
     */
    public ParkingSession endParkingSessionByLicensePlate(String licensePlate) {
        ParkingSession session = parkingSessionRepository.findActiveSessionByLicensePlate(licensePlate)
            .orElseThrow(() -> new IllegalArgumentException("No active parking session found for: " + licensePlate));
        
        return endParkingSession(session.getId());
    }
    
    /**
     * セッションの支払い状態を更新
     * @param sessionId セッションID
     * @param paymentStatus 新しい支払い状態
     * @return 更新されたセッション
     * @throws IllegalArgumentException セッションが見つからない場合
     */
    public ParkingSession updatePaymentStatus(Long sessionId, ParkingSession.PaymentStatus paymentStatus) {
        ParkingSession session = parkingSessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Parking session not found with id: " + sessionId));
        
        session.setPaymentStatus(paymentStatus);
        return parkingSessionRepository.save(session);
    }
    
    /**
     * ナンバープレートでセッション履歴を取得
     * @param licensePlate ナンバープレート
     * @return セッション履歴のリスト
     */
    public List<ParkingSession> getParkingSessionsByLicensePlate(String licensePlate) {
        return parkingSessionRepository.findByLicensePlate(licensePlate);
    }
    
    /**
     * アクティブなセッションを取得
     * @return アクティブなセッションのリスト
     */
    public List<ParkingSession> getActiveParkingSessions() {
        return parkingSessionRepository.findByStatus(ParkingSession.SessionStatus.ACTIVE);
    }
    
    // ==================== 料金計算・統計メソッド ====================
    
    /**
     * 駐車場料金を計算
     * @param session 駐車場セッション
     * @return 計算された料金
     * @throws IllegalArgumentException 出庫時刻が設定されていない場合
     */
    public BigDecimal calculateParkingFee(ParkingSession session) {
        if (session.getExitTime() == null) {
            throw new IllegalArgumentException("Exit time is not set for session: " + session.getId());
        }
        
        Duration duration = Duration.between(session.getEntryTime(), session.getExitTime());
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        
        // 分がある場合は1時間に切り上げ
        if (minutes > 0) {
            hours++;
        }
        
        // 最低1時間の料金
        if (hours == 0) {
            hours = 1;
        }
        
        BigDecimal hourlyRate = BigDecimal.valueOf(session.getParkingSpot().getHourlyRate());
        return hourlyRate.multiply(BigDecimal.valueOf(hours)).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * 利用可能なスペット数を取得
     * @return 利用可能なスペット数
     */
    public long getAvailableSpotsCount() {
        return parkingSpotRepository.countByStatus(ParkingSpot.SpotStatus.AVAILABLE);
    }
    
    /**
     * 使用中のスペット数を取得
     * @return 使用中のスペット数
     */
    public long getOccupiedSpotsCount() {
        return parkingSpotRepository.countByStatus(ParkingSpot.SpotStatus.OCCUPIED);
    }
    
    /**
     * アクティブなセッション数を取得
     * @return アクティブなセッション数
     */
    public long getActiveSessionsCount() {
        return parkingSessionRepository.countActiveSessions();
    }
} 