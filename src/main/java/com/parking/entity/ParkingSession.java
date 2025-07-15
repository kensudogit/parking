package com.parking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 駐車場セッションエンティティ
 * 駐車場の利用セッション情報を管理する
 */
@Entity
@Table(name = "parking_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSession {
    
    /**
     * 主キーID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 駐車場スペット（多対一の関係）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_spot_id", nullable = false)
    private ParkingSpot parkingSpot;
    
    /**
     * 車両ナンバープレート
     */
    @Column(name = "license_plate", nullable = false)
    private String licensePlate;
    
    /**
     * 入庫時刻
     */
    @Column(name = "entry_time", nullable = false)
    private LocalDateTime entryTime;
    
    /**
     * 出庫時刻
     */
    @Column(name = "exit_time")
    private LocalDateTime exitTime;
    
    /**
     * 総料金
     */
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    
    /**
     * セッション状態（アクティブ、完了、キャンセル）
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SessionStatus status = SessionStatus.ACTIVE;
    
    /**
     * 支払い状態（未払い、支払い済み、支払い失敗）
     */
    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    
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
     * 作成日時、更新日時、入庫時刻を設定
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (entryTime == null) {
            entryTime = LocalDateTime.now();
        }
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
     * セッション状態の列挙型
     */
    public enum SessionStatus {
        /** アクティブ（利用中） */
        ACTIVE, 
        /** 完了 */
        COMPLETED, 
        /** キャンセル */
        CANCELLED
    }
    
    /**
     * 支払い状態の列挙型
     */
    public enum PaymentStatus {
        /** 未払い */
        PENDING, 
        /** 支払い済み */
        PAID, 
        /** 支払い失敗 */
        FAILED
    }
} 