package com.parking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 駐車場スペットエンティティ
 * 駐車場の各スペットの情報を管理する
 */
@Entity
@Table(name = "parking_spots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpot {
    
    /**
     * 主キーID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * スペット番号（一意）
     */
    @Column(name = "spot_number", unique = true, nullable = false)
    private String spotNumber;
    
    /**
     * スペットタイプ（通常、障害者用、充電器付き、バイク、トラック）
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "spot_type", nullable = false)
    private SpotType spotType;
    
    /**
     * スペットの状態（利用可能、使用中、予約済み、メンテナンス中）
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SpotStatus status = SpotStatus.AVAILABLE;
    
    /**
     * 階層レベル
     */
    @Column(name = "floor_level")
    private Integer floorLevel;
    
    /**
     * 時間料金（円/時間）
     */
    @Column(name = "hourly_rate", nullable = false)
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
     * エンティティ作成時の処理
     * 作成日時と更新日時を設定
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * エンティティ更新時の処理
     * 更新日時を設定
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
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