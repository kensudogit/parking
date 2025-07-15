package com.parking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * ユーザーエンティティクラス
 * システムユーザーの認証・認可情報を管理する
 * 
 * @author Parking System
 * @version 1.0
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    /** 主キーID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** ユーザー名（一意） */
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    
    /** メールアドレス（一意） */
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    /** パスワード（ハッシュ化） */
    @Column(name = "password", nullable = false)
    private String password;
    
    /** 姓 */
    @Column(name = "first_name")
    private String firstName;
    
    /** 名 */
    @Column(name = "last_name")
    private String lastName;
    
    /** 電話番号 */
    @Column(name = "phone_number")
    private String phoneNumber;
    
    /** ユーザー種別 */
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType = UserType.CUSTOMER;
    
    /** アカウント有効フラグ */
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;
    
    /** 最終ログイン日時 */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
    /** 作成日時 */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /** 更新日時 */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /** ユーザーの役割（多対多の関係） */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    
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
     * ユーザー種別の列挙型
     */
    public enum UserType {
        /** 管理者 */
        ADMIN,
        /** スタッフ */
        STAFF,
        /** 顧客 */
        CUSTOMER
    }
    
    /**
     * フルネームを取得
     * @return フルネーム
     */
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (lastName != null) {
            return lastName;
        } else {
            return username;
        }
    }
    
    /**
     * 管理者かどうかを判定
     * @return 管理者の場合true
     */
    public boolean isAdmin() {
        return userType == UserType.ADMIN;
    }
    
    /**
     * スタッフかどうかを判定
     * @return スタッフの場合true
     */
    public boolean isStaff() {
        return userType == UserType.STAFF || userType == UserType.ADMIN;
    }
} 