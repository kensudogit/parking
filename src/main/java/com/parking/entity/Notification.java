package com.parking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 通知エンティティクラス
 * システムからの通知を管理する
 * 
 * @author Parking System
 * @version 1.0
 */
@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    
    /** 主キーID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 通知対象ユーザー */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    /** 通知タイトル */
    @Column(name = "title", nullable = false)
    private String title;
    
    /** 通知内容 */
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;
    
    /** 通知種別 */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;
    
    /** 通知ステータス */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status = NotificationStatus.UNREAD;
    
    /** 通知方法 */
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_method", nullable = false)
    private DeliveryMethod deliveryMethod;
    
    /** 送信日時 */
    @Column(name = "sent_at")
    private LocalDateTime sentAt;
    
    /** 既読日時 */
    @Column(name = "read_at")
    private LocalDateTime readAt;
    
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
     * 通知種別の列挙型
     */
    public enum NotificationType {
        /** 駐車開始通知 */
        PARKING_START,
        /** 駐車終了通知 */
        PARKING_END,
        /** 決済完了通知 */
        PAYMENT_COMPLETED,
        /** 決済失敗通知 */
        PAYMENT_FAILED,
        /** 返金通知 */
        REFUND_PROCESSED,
        /** システム通知 */
        SYSTEM_ALERT,
        /** メンテナンス通知 */
        MAINTENANCE_ALERT
    }
    
    /**
     * 通知ステータスの列挙型
     */
    public enum NotificationStatus {
        /** 未読 */
        UNREAD,
        /** 既読 */
        READ,
        /** 送信済み */
        SENT,
        /** 送信失敗 */
        FAILED
    }
    
    /**
     * 配信方法の列挙型
     */
    public enum DeliveryMethod {
        /** アプリ内通知 */
        IN_APP,
        /** メール通知 */
        EMAIL,
        /** SMS通知 */
        SMS,
        /** プッシュ通知 */
        PUSH
    }
    
    /**
     * 通知を既読にする
     */
    public void markAsRead() {
        this.status = NotificationStatus.READ;
        this.readAt = LocalDateTime.now();
    }
    
    /**
     * 通知を送信済みにする
     */
    public void markAsSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = LocalDateTime.now();
    }
    
    /**
     * 通知を送信失敗にする
     */
    public void markAsFailed() {
        this.status = NotificationStatus.FAILED;
    }
} 