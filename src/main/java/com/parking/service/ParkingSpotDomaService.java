package com.parking.service;

import com.parking.dao.ParkingSpotDao;
import com.parking.entity.ParkingSpotDoma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 駐車場スペットDomaサービス
 * Doma2フレームワークを使用した駐車場スペットのビジネスロジックを管理
 */
@Service
@Transactional
public class ParkingSpotDomaService {
    
    @Autowired
    private ParkingSpotDao parkingSpotDao;
    
    /**
     * すべての駐車場スペットを取得
     * @return 駐車場スペットのリスト
     */
    public List<ParkingSpotDoma> getAllParkingSpots() {
        return parkingSpotDao.selectAll();
    }
    
    /**
     * IDで駐車場スペットを取得
     * @param id スペットID
     * @return 駐車場スペット（存在しない場合はnull）
     */
    public ParkingSpotDoma getParkingSpotById(Long id) {
        return parkingSpotDao.selectById(id);
    }
    
    /**
     * 利用可能な駐車場スペットを取得
     * @return 利用可能なスペットのリスト
     */
    public List<ParkingSpotDoma> getAvailableSpots() {
        return parkingSpotDao.selectAll().stream()
            .filter(spot -> spot.getStatus() == ParkingSpotDoma.SpotStatus.AVAILABLE)
            .collect(Collectors.toList());
    }
    
    /**
     * 状態で駐車場スペットを取得
     * @param status スペット状態
     * @return 指定状態のスペットリスト
     */
    public List<ParkingSpotDoma> getSpotsByStatus(ParkingSpotDoma.SpotStatus status) {
        return parkingSpotDao.selectAll().stream()
            .filter(spot -> spot.getStatus() == status)
            .collect(Collectors.toList());
    }
    
    /**
     * タイプで駐車場スペットを取得
     * @param spotType スペットタイプ
     * @return 指定タイプのスペットリスト
     */
    public List<ParkingSpotDoma> getSpotsByType(ParkingSpotDoma.SpotType spotType) {
        return parkingSpotDao.selectAll().stream()
            .filter(spot -> spot.getSpotType() == spotType)
            .collect(Collectors.toList());
    }
    
    /**
     * 新しい駐車場スペットを作成
     * @param spotNumber スペット番号
     * @param spotType スペットタイプ
     * @param floorLevel 階層レベル
     * @param hourlyRate 時間料金
     * @return 作成されたスペット
     */
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
    
    /**
     * 駐車場スペットの状態を更新
     * @param id スペットID
     * @param status 新しい状態
     * @return 更新成功時はtrue
     */
    public boolean updateParkingSpotStatus(Long id, ParkingSpotDoma.SpotStatus status) {
        ParkingSpotDoma existingSpot = parkingSpotDao.selectById(id);
        if (existingSpot == null) {
            return false;
        }
        
        existingSpot.setStatus(status);
        existingSpot.setUpdatedAt(LocalDateTime.now());
        
        return parkingSpotDao.update(existingSpot) > 0;
    }
    
    /**
     * 駐車場スペットを削除
     * @param id スペットID
     * @return 削除成功時はtrue
     */
    public boolean deleteParkingSpot(Long id) {
        ParkingSpotDoma existingSpot = parkingSpotDao.selectById(id);
        if (existingSpot == null) {
            return false;
        }
        
        return parkingSpotDao.delete(existingSpot) > 0;
    }
} 