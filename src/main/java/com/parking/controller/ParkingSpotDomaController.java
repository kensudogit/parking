package com.parking.controller;

import com.parking.entity.ParkingSpotDoma;
import com.parking.service.ParkingSpotDomaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 駐車場スペットDomaコントローラー
 * Doma2フレームワークを使用した駐車場スペットの管理APIを提供
 */
@RestController
@RequestMapping("/api/doma/parking-spots")
public class ParkingSpotDomaController {
    
    @Autowired
    private ParkingSpotDomaService parkingSpotDomaService;
    
    /**
     * すべての駐車場スペットを取得
     * @return 駐車場スペットのリスト
     */
    @GetMapping
    public ResponseEntity<List<ParkingSpotDoma>> getAllParkingSpots() {
        List<ParkingSpotDoma> spots = parkingSpotDomaService.getAllParkingSpots();
        return ResponseEntity.ok(spots);
    }
    
    /**
     * IDで駐車場スペットを取得
     * @param id スペットID
     * @return 駐車場スペット（存在しない場合は404）
     */
    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpotDoma> getParkingSpotById(@PathVariable Long id) {
        ParkingSpotDoma spot = parkingSpotDomaService.getParkingSpotById(id);
        if (spot == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spot);
    }
    
    /**
     * 利用可能な駐車場スペットを取得
     * @return 利用可能なスペットのリスト
     */
    @GetMapping("/available")
    public ResponseEntity<List<ParkingSpotDoma>> getAvailableSpots() {
        List<ParkingSpotDoma> spots = parkingSpotDomaService.getAvailableSpots();
        return ResponseEntity.ok(spots);
    }
    
    /**
     * 状態で駐車場スペットを取得
     * @param status スペット状態
     * @return 指定状態のスペットリスト
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ParkingSpotDoma>> getSpotsByStatus(@PathVariable ParkingSpotDoma.SpotStatus status) {
        List<ParkingSpotDoma> spots = parkingSpotDomaService.getSpotsByStatus(status);
        return ResponseEntity.ok(spots);
    }
    
    /**
     * タイプで駐車場スペットを取得
     * @param spotType スペットタイプ
     * @return 指定タイプのスペットリスト
     */
    @GetMapping("/type/{spotType}")
    public ResponseEntity<List<ParkingSpotDoma>> getSpotsByType(@PathVariable ParkingSpotDoma.SpotType spotType) {
        List<ParkingSpotDoma> spots = parkingSpotDomaService.getSpotsByType(spotType);
        return ResponseEntity.ok(spots);
    }
    
    /**
     * 新しい駐車場スペットを作成
     * @param spotNumber スペット番号
     * @param spotType スペットタイプ
     * @param floorLevel 階層レベル
     * @param hourlyRate 時間料金
     * @return 作成されたスペット
     */
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
    
    /**
     * 駐車場スペットの状態を更新
     * @param id スペットID
     * @param status 新しい状態
     * @return 更新結果
     */
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
    
    /**
     * 駐車場スペットを削除
     * @param id スペットID
     * @return 削除結果
     */
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