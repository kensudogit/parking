package com.parking.repository;

import com.parking.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * 駐車場スペットリポジトリ
 * 駐車場スペットのデータアクセスを管理
 */
@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    
    /**
     * スペット番号で駐車場スペットを検索
     * @param spotNumber スペット番号
     * @return 駐車場スペット（存在しない場合は空）
     */
    Optional<ParkingSpot> findBySpotNumber(String spotNumber);
    
    /**
     * 状態で駐車場スペットを検索
     * @param status スペット状態
     * @return 指定状態のスペットリスト
     */
    List<ParkingSpot> findByStatus(ParkingSpot.SpotStatus status);
    
    /**
     * タイプで駐車場スペットを検索
     * @param spotType スペットタイプ
     * @return 指定タイプのスペットリスト
     */
    List<ParkingSpot> findBySpotType(ParkingSpot.SpotType spotType);
    
    /**
     * 階層で駐車場スペットを検索
     * @param floorLevel 階層レベル
     * @return 指定階層のスペットリスト
     */
    List<ParkingSpot> findByFloorLevel(Integer floorLevel);
    
    /**
     * 状態とタイプで駐車場スペットを検索
     * @param status スペット状態
     * @param spotType スペットタイプ
     * @return 指定条件のスペットリスト
     */
    @Query("SELECT ps FROM ParkingSpot ps WHERE ps.status = :status AND ps.spotType = :spotType")
    List<ParkingSpot> findByStatusAndSpotType(@Param("status") ParkingSpot.SpotStatus status, 
                                             @Param("spotType") ParkingSpot.SpotType spotType);
    
    /**
     * 指定状態のスペット数をカウント
     * @param status スペット状態
     * @return 指定状態のスペット数
     */
    @Query("SELECT COUNT(ps) FROM ParkingSpot ps WHERE ps.status = :status")
    long countByStatus(@Param("status") ParkingSpot.SpotStatus status);
    
    /**
     * 指定タイプのスペット数をカウント
     * @param spotType スペットタイプ
     * @return 指定タイプのスペット数
     */
    @Query("SELECT COUNT(ps) FROM ParkingSpot ps WHERE ps.spotType = :spotType")
    long countBySpotType(@Param("spotType") ParkingSpot.SpotType spotType);
    
    /**
     * スペット番号の存在確認
     * @param spotNumber スペット番号
     * @return 存在する場合はtrue
     */
    boolean existsBySpotNumber(String spotNumber);
} 