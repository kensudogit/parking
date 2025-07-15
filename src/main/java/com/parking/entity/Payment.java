package com.parking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 決済エンティティクラス
 * 駐車場の決済情報を管理する
 * 
 * @author Parking System
 * @version 1.0
 */
@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    
    /** 主キーID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 駐車セッション（1対1の関係） */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_session_id", nullable = false)
    private ParkingSession parkingSession;
    
    /** 決済金額 */
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    
    /** 決済方法 */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;
    
    /** 決済ステータス */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;
    
    /** 取引ID（一意） */
    @Column(name = "transaction_id")
    private String transactionId;
    
    /** カード番号の下4桁 */
    @Column(name = "card_last_four")
    private String cardLastFour;
    
    /** カードブランド */
    @Column(name = "card_brand")
    private String cardBrand;
    
    /** 領収書URL */
    @Column(name = "receipt_url")
    private String receiptUrl;
    
    /** 失敗理由 */
    @Column(name = "failure_reason")
    private String failureReason;
    
    /** 処理日時 */
    @Column(name = "processed_at")
    private LocalDateTime processedAt;
    
    /** 作成日時 */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /** 更新日時 */
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
     * 決済方法の列挙型
     */
    public enum PaymentMethod {
        /** クレジットカード */
        CREDIT_CARD, 
        /** デビットカード */
        DEBIT_CARD, 
        /** 現金 */
        CASH, 
        /** 電子ウォレット */
        ELECTRONIC_WALLET, 
        /** モバイル決済 */
        MOBILE_PAYMENT, 
        /** QRコード決済 */
        QR_CODE
    }
    
    /**
     * 決済ステータスの列挙型
     */
    public enum PaymentStatus {
        /** 保留中 */
        PENDING, 
        /** 処理中 */
        PROCESSING, 
        /** 完了 */
        COMPLETED, 
        /** 失敗 */
        FAILED, 
        /** 返金済み */
        REFUNDED, 
        /** キャンセル */
        CANCELLED
    }
} 