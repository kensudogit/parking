package com.parking.repository;

import com.parking.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知リポジトリ
 * 通知エンティティのデータベース操作を担当
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * ユーザーIDで通知を検索（作成日時降順）
     * @param userId ユーザーID
     * @return 通知リスト
     */
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * ユーザーIDと未読で通知を検索（作成日時降順）
     * @param userId ユーザーID
     * @return 未読通知リスト
     */
    List<Notification> findByUserIdAndReadAtIsNullOrderByCreatedAtDesc(Long userId);

    /**
     * ユーザーIDと通知タイプで通知を検索
     * @param userId ユーザーID
     * @param type 通知タイプ
     * @return 通知リスト
     */
    List<Notification> findByUserIdAndTypeOrderByCreatedAtDesc(Long userId, Notification.NotificationType type);

    /**
     * ユーザーIDとステータスで通知を検索
     * @param userId ユーザーID
     * @param status ステータス
     * @return 通知リスト
     */
    List<Notification> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, Notification.Status status);

    /**
     * ユーザーIDと優先度で通知を検索
     * @param userId ユーザーID
     * @param priority 優先度
     * @return 通知リスト
     */
    List<Notification> findByUserIdAndPriorityOrderByCreatedAtDesc(Long userId, Notification.Priority priority);

    /**
     * 指定期間の通知を検索
     * @param userId ユーザーID
     * @param startDate 開始日時
     * @param endDate 終了日時
     * @return 通知リスト
     */
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.createdAt BETWEEN :startDate AND :endDate ORDER BY n.createdAt DESC")
    List<Notification> findByUserIdAndCreatedAtBetween(@Param("userId") Long userId, 
                                                      @Param("startDate") LocalDateTime startDate, 
                                                      @Param("endDate") LocalDateTime endDate);

    /**
     * 指定期間の通知数をカウント
     * @param startDate 開始日時
     * @param endDate 終了日時
     * @return 通知数
     */
    long countByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 指定期間とステータスの通知数をカウント
     * @param status ステータス
     * @param startDate 開始日時
     * @param endDate 終了日時
     * @return 通知数
     */
    long countByStatusAndCreatedAtBetween(Notification.Status status, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 指定期間とタイプの通知数をカウント
     * @param type 通知タイプ
     * @param startDate 開始日時
     * @param endDate 終了日時
     * @return 通知数
     */
    long countByTypeAndCreatedAtBetween(Notification.NotificationType type, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * ユーザーの未読通知数をカウント
     * @param userId ユーザーID
     * @return 未読通知数
     */
    long countByUserIdAndReadAtIsNull(Long userId);

    /**
     * ユーザーの通知数をカウント
     * @param userId ユーザーID
     * @return 通知数
     */
    long countByUserId(Long userId);

    /**
     * 指定期間の通知統計を取得
     * @param startDate 開始日時
     * @param endDate 終了日時
     * @return 統計情報
     */
    @Query("SELECT n.type, COUNT(n) FROM Notification n WHERE n.createdAt BETWEEN :startDate AND :endDate GROUP BY n.type")
    List<Object[]> getNotificationCountByType(@Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate);

    /**
     * 指定期間のステータス別通知統計を取得
     * @param startDate 開始日時
     * @param endDate 終了日時
     * @return 統計情報
     */
    @Query("SELECT n.status, COUNT(n) FROM Notification n WHERE n.createdAt BETWEEN :startDate AND :endDate GROUP BY n.status")
    List<Object[]> getNotificationCountByStatus(@Param("startDate") LocalDateTime startDate, 
                                               @Param("endDate") LocalDateTime endDate);

    /**
     * 失敗した通知を検索
     * @return 失敗した通知リスト
     */
    List<Notification> findByStatusOrderByCreatedAtDesc(Notification.Status status);

    /**
     * 指定期間の失敗した通知を検索
     * @param startDate 開始日時
     * @param endDate 終了日時
     * @return 失敗した通知リスト
     */
    @Query("SELECT n FROM Notification n WHERE n.status = 'FAILED' AND n.createdAt BETWEEN :startDate AND :endDate ORDER BY n.createdAt DESC")
    List<Notification> findFailedNotificationsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                                         @Param("endDate") LocalDateTime endDate);
} 