package com.parking.entity;

import org.seasar.doma.*;
import java.time.LocalDateTime;

/**
 * 駐車場スペットDomaエンティティ
 * Doma2フレームワークを使用した駐車場スペットのデータマッピング
 */
@Entity
@Table(name = "parking_spots")
public class ParkingSpotDoma {
    
    /**
     * 主キーID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * スペット番号（一意）
     */
    @Column(name = "spot_number")
    private String spotNumber;
    
    /**
     * スペットタイプ（通常、障害者用、充電器付き、バイク、トラック）
     */
    @Column(name = "spot_type")
    private SpotType spotType;
    
    /**
     * スペットの状態（利用可能、使用中、予約済み、メンテナンス中）
     */
    @Column(name = "status")
    private SpotStatus status;
    
    /**
     * 階層レベル
     */
    @Column(name = "floor_level")
    private Integer floorLevel;
    
    /**
     * 時間料金（円/時間）
     */
    @Column(name = "hourly_rate")
    private Double hourlyRate;
    
    /**
     * 作成日時
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /**
     * 更新日時
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * デフォルトコンストラクタ
     */
    public ParkingSpotDoma() {
    }
    
    /**
     * 全引数コンストラクタ
     * @param id 主キーID
     * @param spotNumber スペット番号
     * @param spotType スペットタイプ
     * @param status スペット状態
     * @param floorLevel 階層レベル
     * @param hourlyRate 時間料金
     * @param createdAt 作成日時
     * @param updatedAt 更新日時
     */
    public ParkingSpotDoma(
            Long id,
            String spotNumber,
            SpotType spotType,
            SpotStatus status,
            Integer floorLevel,
            Double hourlyRate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.spotNumber = spotNumber;
        this.spotType = spotType;
        this.status = status;
        this.floorLevel = floorLevel;
        this.hourlyRate = hourlyRate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getter and Setter methods
    /**
     * IDを取得
     * @return ID
     */
    public Long getId() { return id; }
    
    /**
     * IDを設定
     * @param id ID
     */
    public void setId(Long id) { this.id = id; }
    
    /**
     * スペット番号を取得
     * @return スペット番号
     */
    public String getSpotNumber() { return spotNumber; }
    
    /**
     * スペット番号を設定
     * @param spotNumber スペット番号
     */
    public void setSpotNumber(String spotNumber) { this.spotNumber = spotNumber; }
    
    /**
     * スペットタイプを取得
     * @return スペットタイプ
     */
    public SpotType getSpotType() { return spotType; }
    
    /**
     * スペットタイプを設定
     * @param spotType スペットタイプ
     */
    public void setSpotType(SpotType spotType) { this.spotType = spotType; }
    
    /**
     * スペット状態を取得
     * @return スペット状態
     */
    public SpotStatus getStatus() { return status; }
    
    /**
     * スペット状態を設定
     * @param status スペット状態
     */
    public void setStatus(SpotStatus status) { this.status = status; }
    
    /**
     * 階層レベルを取得
     * @return 階層レベル
     */
    public Integer getFloorLevel() { return floorLevel; }
    
    /**
     * 階層レベルを設定
     * @param floorLevel 階層レベル
     */
    public void setFloorLevel(Integer floorLevel) { this.floorLevel = floorLevel; }
    
    /**
     * 時間料金を取得
     * @return 時間料金
     */
    public Double getHourlyRate() { return hourlyRate; }
    
    /**
     * 時間料金を設定
     * @param hourlyRate 時間料金
     */
    public void setHourlyRate(Double hourlyRate) { this.hourlyRate = hourlyRate; }
    
    /**
     * 作成日時を取得
     * @return 作成日時
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    /**
     * 作成日時を設定
     * @param createdAt 作成日時
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    /**
     * 更新日時を取得
     * @return 更新日時
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    /**
     * 更新日時を設定
     * @param updatedAt 更新日時
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    /**
     * スペットタイプの列挙型
     */
    public enum SpotType {
        /** 通常スペット */
        REGULAR, 
        /** 障害者用スペット */
        DISABLED, 
        /** 充電器付きスペット */
        ELECTRIC_CHARGING, 
        /** バイク用スペット */
        MOTORCYCLE, 
        /** トラック用スペット */
        TRUCK
    }
    
    /**
     * スペット状態の列挙型
     */
    public enum SpotStatus {
        /** 利用可能 */
        AVAILABLE, 
        /** 使用中 */
        OCCUPIED, 
        /** 予約済み */
        RESERVED, 
        /** メンテナンス中 */
        MAINTENANCE
    }
} 