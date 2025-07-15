package com.parking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 役割（ロール）エンティティクラス
 * ユーザーの権限を管理する
 * 
 * @author Parking System
 * @version 1.0
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    
    /** 主キーID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 役割名（一意） */
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    /** 役割の説明 */
    @Column(name = "description")
    private String description;
    
    /** 作成日時 */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /** 更新日時 */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /** この役割を持つユーザー（多対多の関係） */
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
    
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
     * 役割名の定数
     */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_STAFF = "ROLE_STAFF";
    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
} 