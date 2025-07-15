package com.parking.service;

import com.parking.entity.Notification;
import com.parking.entity.User;
import com.parking.repository.NotificationRepository;
import com.parking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 通知サービス
 * メール、SMS、プッシュ通知の送信と管理を担当
 */
@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    /**
     * 通知を作成して送信
     * @param userId ユーザーID
     * @param type 通知タイプ
     * @param title タイトル
     * @param message メッセージ
     * @param priority 優先度
     * @return 作成された通知
     */
    public Notification createAndSendNotification(Long userId, Notification.NotificationType type, 
                                               String title, String message, Notification.Priority priority) {
        // ユーザーを検索
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

        // 通知を作成
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setPriority(priority);
        notification.setStatus(Notification.Status.PENDING);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());

        // 通知を保存
        Notification savedNotification = notificationRepository.save(notification);

        // 通知を送信
        sendNotification(savedNotification);

        return savedNotification;
    }

    /**
     * 通知を送信
     * @param notification 通知
     */
    private void sendNotification(Notification notification) {
        try {
            switch (notification.getType()) {
                case EMAIL:
                    sendEmailNotification(notification);
                    break;
                case SMS:
                    sendSmsNotification(notification);
                    break;
                case PUSH:
                    sendPushNotification(notification);
                    break;
                case SYSTEM:
                    // システム通知はデータベースに保存するのみ
                    notification.setStatus(Notification.Status.SENT);
                    notification.setSentAt(LocalDateTime.now());
                    break;
            }
            
            // 送信成功時
            notification.setStatus(Notification.Status.SENT);
            notification.setSentAt(LocalDateTime.now());
            notification.setUpdatedAt(LocalDateTime.now());
            
        } catch (Exception e) {
            // 送信失敗時
            notification.setStatus(Notification.Status.FAILED);
            notification.setErrorMessage(e.getMessage());
            notification.setUpdatedAt(LocalDateTime.now());
        }
        
        notificationRepository.save(notification);
    }

    /**
     * メール通知を送信
     * @param notification 通知
     */
    private void sendEmailNotification(Notification notification) {
        User user = notification.getUser();
        emailService.sendEmail(
                user.getEmail(),
                notification.getTitle(),
                notification.getMessage()
        );
    }

    /**
     * SMS通知を送信
     * @param notification 通知
     */
    private void sendSmsNotification(Notification notification) {
        User user = notification.getUser();
        if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
            smsService.sendSms(
                    user.getPhoneNumber(),
                    notification.getMessage()
            );
        }
    }

    /**
     * プッシュ通知を送信
     * @param notification 通知
     */
    private void sendPushNotification(Notification notification) {
        // プッシュ通知の実装（FCM等を使用）
        // ここでは簡略化のため実装を省略
        System.out.println("プッシュ通知を送信: " + notification.getTitle());
    }

    /**
     * ユーザーの通知一覧を取得
     * @param userId ユーザーID
     * @return 通知リスト
     */
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * 未読通知一覧を取得
     * @param userId ユーザーID
     * @return 未読通知リスト
     */
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadAtIsNullOrderByCreatedAtDesc(userId);
    }

    /**
     * 通知を既読にする
     * @param notificationId 通知ID
     * @return 更新された通知
     */
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("通知が見つかりません"));

        notification.setReadAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());
        
        return notificationRepository.save(notification);
    }

    /**
     * 通知を削除
     * @param notificationId 通知ID
     */
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    /**
     * 通知を再送信
     * @param notificationId 通知ID
     * @return 再送信された通知
     */
    public Notification resendNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("通知が見つかりません"));

        notification.setStatus(Notification.Status.PENDING);
        notification.setErrorMessage(null);
        notification.setUpdatedAt(LocalDateTime.now());

        // 通知を再送信
        sendNotification(notification);

        return notification;
    }

    /**
     * 指定期間の通知統計を取得
     * @param startDate 開始日時
     * @param endDate 終了日時
     * @return 統計情報
     */
    public NotificationStatistics getNotificationStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        long totalNotifications = notificationRepository.countByCreatedAtBetween(startDate, endDate);
        long sentNotifications = notificationRepository.countByStatusAndCreatedAtBetween(
                Notification.Status.SENT, startDate, endDate);
        long failedNotifications = notificationRepository.countByStatusAndCreatedAtBetween(
                Notification.Status.FAILED, startDate, endDate);

        return new NotificationStatistics(totalNotifications, sentNotifications, failedNotifications);
    }

    /**
     * 通知統計クラス
     */
    public static class NotificationStatistics {
        private final long totalNotifications;
        private final long sentNotifications;
        private final long failedNotifications;

        public NotificationStatistics(long totalNotifications, long sentNotifications, long failedNotifications) {
            this.totalNotifications = totalNotifications;
            this.sentNotifications = sentNotifications;
            this.failedNotifications = failedNotifications;
        }

        // Getter methods
        public long getTotalNotifications() { return totalNotifications; }
        public long getSentNotifications() { return sentNotifications; }
        public long getFailedNotifications() { return failedNotifications; }
        public long getSuccessRate() {
            return totalNotifications > 0 ? (sentNotifications * 100 / totalNotifications) : 0;
        }
    }
} 