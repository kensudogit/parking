package com.parking.repository;

import com.parking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * ユーザーリポジトリ
 * ユーザーエンティティのデータベース操作を担当
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * ユーザー名でユーザーを検索
     * @param username ユーザー名
     * @return ユーザー（存在しない場合は空）
     */
    Optional<User> findByUsername(String username);

    /**
     * メールアドレスでユーザーを検索
     * @param email メールアドレス
     * @return ユーザー（存在しない場合は空）
     */
    Optional<User> findByEmail(String email);

    /**
     * 有効なユーザーを検索
     * @param enabled 有効フラグ
     * @return ユーザーリスト
     */
    List<User> findByEnabled(boolean enabled);

    /**
     * ユーザー名またはメールアドレスでユーザーを検索
     * @param username ユーザー名
     * @param email メールアドレス
     * @return ユーザー（存在しない場合は空）
     */
    Optional<User> findByUsernameOrEmail(String username, String email);

    /**
     * 最終ログイン日時でユーザーを検索
     * @param lastLoginAt 最終ログイン日時
     * @return ユーザーリスト
     */
    List<User> findByLastLoginAtBefore(LocalDateTime lastLoginAt);

    /**
     * 作成日時でユーザーを検索
     * @param createdAt 作成日時
     * @return ユーザーリスト
     */
    List<User> findByCreatedAtAfter(LocalDateTime createdAt);

    /**
     * ユーザー名の存在チェック
     * @param username ユーザー名
     * @return 存在する場合true
     */
    boolean existsByUsername(String username);

    /**
     * メールアドレスの存在チェック
     * @param email メールアドレス
     * @return 存在する場合true
     */
    boolean existsByEmail(String email);

    /**
     * ユーザー数をカウント
     * @return ユーザー数
     */
    @Query("SELECT COUNT(u) FROM User u")
    long countAllUsers();

    /**
     * 有効なユーザー数をカウント
     * @return 有効なユーザー数
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.enabled = true")
    long countEnabledUsers();

    /**
     * 指定期間内に作成されたユーザー数をカウント
     * @param startDate 開始日時
     * @param endDate 終了日時
     * @return ユーザー数
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    long countUsersByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);

    /**
     * 指定期間内にログインしたユーザーを検索
     * @param startDate 開始日時
     * @param endDate 終了日時
     * @return ユーザーリスト
     */
    @Query("SELECT u FROM User u WHERE u.lastLoginAt BETWEEN :startDate AND :endDate")
    List<User> findUsersByLastLoginAtBetween(@Param("startDate") LocalDateTime startDate, 
                                            @Param("endDate") LocalDateTime endDate);
} 