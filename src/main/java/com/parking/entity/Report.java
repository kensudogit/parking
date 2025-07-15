package com.parking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * レポートエンティティクラス
 * システムの各種レポートを管理する
 * 
 * @author Parking System
 * @version 1.0
 */
@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    
    /** 主キーID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** レポート名 */
    @Column(name = "name", nullable = false)
    private String name;
    
    /** レポート種別 */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ReportType type;
    
    /** レポート期間開始日 */
    @Column(name = "start_date")
    private LocalDate startDate;
    
    /** レポート期間終了日 */
    @Column(name = "end_date")
    private LocalDate endDate;
    
    /** レポートデータ（JSON形式） */
    @Column(name = "data", columnDefinition = "TEXT")
    private String data;
    
    /** レポートファイルURL */
    @Column(name = "file_url")
    private String fileUrl;
    
    /** レポートステータス */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReportStatus status = ReportStatus.PENDING;
    
    /** 生成者ユーザー */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    /** 生成日時 */
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
     * レポート種別の列挙型
     */
    public enum ReportType {
        /** 日次売上レポート */
        DAILY_REVENUE,
        /** 月次売上レポート */
        MONTHLY_REVENUE,
        /** 駐車場使用率レポート */
        PARKING_UTILIZATION,
        /** 決済方法別レポート */
        PAYMENT_METHOD_ANALYSIS,
        /** 顧客分析レポート */
        CUSTOMER_ANALYSIS,
        /** エラー分析レポート */
        ERROR_ANALYSIS,
        /** システムパフォーマンスレポート */
        SYSTEM_PERFORMANCE
    }
    
    /**
     * レポートステータスの列挙型
     */
    public enum ReportStatus {
        /** 生成中 */
        PENDING,
        /** 生成完了 */
        COMPLETED,
        /** 生成失敗 */
        FAILED,
        /** 削除済み */
        DELETED
    }
    
    /**
     * レポートを完了状態にする
     */
    public void markAsCompleted() {
        this.status = ReportStatus.COMPLETED;
    }
    
    /**
     * レポートを失敗状態にする
     */
    public void markAsFailed() {
        this.status = ReportStatus.FAILED;
    }
    
    /**
     * レポートを削除状態にする
     */
    public void markAsDeleted() {
        this.status = ReportStatus.DELETED;
    }
} 