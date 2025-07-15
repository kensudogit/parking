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

/**
 * 駐車場管理コントローラー
 * 駐車場スペットとセッションの管理APIを提供
 */
@RestController
@RequestMapping("/api/parking")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ParkingController {
    
    private final ParkingService parkingService;
    
    // ==================== 駐車場スペット管理エンドポイント ====================
    
    /**
     * すべての駐車場スペットを取得
     * @return 駐車場スペットのリスト
     */
    @GetMapping("/spots")
    public ResponseEntity<List<ParkingSpot>> getAllParkingSpots() {
        List<ParkingSpot> spots = parkingService.getAllParkingSpots();
        return ResponseEntity.ok(spots);
    }
    
    /**
     * IDで駐車場スペットを取得
     * @param id スペットID
     * @return 駐車場スペット（存在しない場合は404）
     */
    @GetMapping("/spots/{id}")
    public ResponseEntity<ParkingSpot> getParkingSpotById(@PathVariable Long id) {
        Optional<ParkingSpot> spot = parkingService.getParkingSpotById(id);
        return spot.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * スペット番号で駐車場スペットを取得
     * @param spotNumber スペット番号
     * @return 駐車場スペット（存在しない場合は404）
     */
    @GetMapping("/spots/number/{spotNumber}")
    public ResponseEntity<ParkingSpot> getParkingSpotByNumber(@PathVariable String spotNumber) {
        Optional<ParkingSpot> spot = parkingService.getParkingSpotByNumber(spotNumber);
        return spot.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 利用可能な駐車場スペットを取得
     * @return 利用可能なスペットのリスト
     */
    @GetMapping("/spots/available")
    public ResponseEntity<List<ParkingSpot>> getAvailableSpots() {
        List<ParkingSpot> spots = parkingService.getAvailableSpots();
        return ResponseEntity.ok(spots);
    }
    
    /**
     * 指定タイプの利用可能な駐車場スペットを取得
     * @param spotType スペットタイプ
     * @return 利用可能なスペットのリスト
     */
    @GetMapping("/spots/available/{spotType}")
    public ResponseEntity<List<ParkingSpot>> getAvailableSpotsByType(@PathVariable ParkingSpot.SpotType spotType) {
        List<ParkingSpot> spots = parkingService.getAvailableSpotsByType(spotType);
        return ResponseEntity.ok(spots);
    }
    
    /**
     * 新しい駐車場スペットを作成
     * @param parkingSpot 作成するスペット情報
     * @return 作成されたスペット
     */
    @PostMapping("/spots")
    public ResponseEntity<ParkingSpot> createParkingSpot(@Valid @RequestBody ParkingSpot parkingSpot) {
        try {
            ParkingSpot createdSpot = parkingService.createParkingSpot(parkingSpot);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSpot);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 駐車場スペットを更新
     * @param id スペットID
     * @param parkingSpot 更新するスペット情報
     * @return 更新されたスペット
     */
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
    
    /**
     * 駐車場スペットを削除
     * @param id スペットID
     * @return 削除成功時は204
     */
    @DeleteMapping("/spots/{id}")
    public ResponseEntity<Void> deleteParkingSpot(@PathVariable Long id) {
        try {
            parkingService.deleteParkingSpot(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // ==================== 駐車場セッション管理エンドポイント ====================
    
    /**
     * 駐車場セッションを開始
     * @param request セッション開始リクエスト（spotId, licensePlate）
     * @return 作成されたセッション
     */
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
    
    /**
     * 駐車場セッションを終了
     * @param sessionId セッションID
     * @return 終了されたセッション
     */
    @PostMapping("/sessions/{sessionId}/end")
    public ResponseEntity<ParkingSession> endParkingSession(@PathVariable Long sessionId) {
        try {
            ParkingSession session = parkingService.endParkingSession(sessionId);
            return ResponseEntity.ok(session);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * ナンバープレートで駐車場セッションを終了
     * @param request 終了リクエスト（licensePlate）
     * @return 終了されたセッション
     */
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
    
    /**
     * セッションの支払い状態を更新
     * @param sessionId セッションID
     * @param request 支払い状態更新リクエスト（paymentStatus）
     * @return 更新されたセッション
     */
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
    
    /**
     * ナンバープレートでセッション履歴を取得
     * @param licensePlate ナンバープレート
     * @return セッション履歴のリスト
     */
    @GetMapping("/sessions/license/{licensePlate}")
    public ResponseEntity<List<ParkingSession>> getParkingSessionsByLicensePlate(@PathVariable String licensePlate) {
        List<ParkingSession> sessions = parkingService.getParkingSessionsByLicensePlate(licensePlate);
        return ResponseEntity.ok(sessions);
    }
    
    /**
     * アクティブなセッションを取得
     * @return アクティブなセッションのリスト
     */
    @GetMapping("/sessions/active")
    public ResponseEntity<List<ParkingSession>> getActiveParkingSessions() {
        List<ParkingSession> sessions = parkingService.getActiveParkingSessions();
        return ResponseEntity.ok(sessions);
    }
    
    // ==================== 統計エンドポイント ====================
    
    /**
     * 駐車場の統計情報を取得
     * @return 利用可能スペット数、使用中スペット数、アクティブセッション数
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getParkingStats() {
        Map<String, Object> stats = Map.of(
            "availableSpots", parkingService.getAvailableSpotsCount(),
            "occupiedSpots", parkingService.getOccupiedSpotsCount(),
            "activeSessions", parkingService.getActiveSessionsCount()
        );
        return ResponseEntity.ok(stats);
    }
    
    /**
     * 利用可能なスペット数を取得
     * @return 利用可能スペット数
     */
    @GetMapping("/stats/available-spots")
    public ResponseEntity<Map<String, Long>> getAvailableSpotsCount() {
        Map<String, Long> count = Map.of("availableSpots", parkingService.getAvailableSpotsCount());
        return ResponseEntity.ok(count);
    }
    
    /**
     * 使用中のスペット数を取得
     * @return 使用中スペット数
     */
    @GetMapping("/stats/occupied-spots")
    public ResponseEntity<Map<String, Long>> getOccupiedSpotsCount() {
        Map<String, Long> count = Map.of("occupiedSpots", parkingService.getOccupiedSpotsCount());
        return ResponseEntity.ok(count);
    }
    
    /**
     * アクティブなセッション数を取得
     * @return アクティブセッション数
     */
    @GetMapping("/stats/active-sessions")
    public ResponseEntity<Map<String, Long>> getActiveSessionsCount() {
        Map<String, Long> count = Map.of("activeSessions", parkingService.getActiveSessionsCount());
        return ResponseEntity.ok(count);
    }
} 