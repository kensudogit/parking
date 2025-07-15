package com.parking.controller;

import com.parking.entity.Notification;
import com.parking.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通知コントローラー
 * 通知管理のAPIエンドポイントを提供
 */
@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * ユーザーの通知一覧を取得
     * @param userId ユーザーID
     * @return 通知リスト
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        try {
            List<Notification> notifications = notificationService.getUserNotifications(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * ユーザーの未読通知一覧を取得
     * @param userId ユーザーID
     * @return 未読通知リスト
     */
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long userId) {
        try {
            List<Notification> notifications = notificationService.getUnreadNotifications(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 通知を既読にする
     * @param notificationId 通知ID
     * @return 更新された通知
     */
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long notificationId) {
        try {
            Notification notification = notificationService.markAsRead(notificationId);
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 通知を削除
     * @param notificationId 通知ID
     * @return 削除結果
     */
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Map<String, Object>> deleteNotification(@PathVariable Long notificationId) {
        try {
            notificationService.deleteNotification(notificationId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "通知が削除されました");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "通知の削除に失敗しました: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 通知を再送信
     * @param notificationId 通知ID
     * @return 再送信された通知
     */
    @PostMapping("/{notificationId}/resend")
    public ResponseEntity<Notification> resendNotification(@PathVariable Long notificationId) {
        try {
            Notification notification = notificationService.resendNotification(notificationId);
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 通知統計を取得
     * @param startDate 開始日時
     * @param endDate 終了日時
     * @return 統計情報
     */
    @GetMapping("/statistics")
    public ResponseEntity<NotificationService.NotificationStatistics> getNotificationStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            NotificationService.NotificationStatistics statistics = 
                    notificationService.getNotificationStatistics(startDate, endDate);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 駐車場利用開始通知を送信
     * @param request 通知リクエスト
     * @return 作成された通知
     */
    @PostMapping("/parking-start")
    public ResponseEntity<Notification> sendParkingStartNotification(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String startTime = request.get("startTime").toString();
            String location = request.get("location").toString();
            
            Notification notification = notificationService.createAndSendNotification(
                    userId,
                    Notification.NotificationType.EMAIL,
                    "駐車場利用開始のお知らせ",
                    String.format("駐車場の利用が開始されました。\n開始時刻: %s\n場所: %s", startTime, location),
                    Notification.Priority.NORMAL
            );
            
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 駐車場利用終了通知を送信
     * @param request 通知リクエスト
     * @return 作成された通知
     */
    @PostMapping("/parking-end")
    public ResponseEntity<Notification> sendParkingEndNotification(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String endTime = request.get("endTime").toString();
            String duration = request.get("duration").toString();
            String amount = request.get("amount").toString();
            
            Notification notification = notificationService.createAndSendNotification(
                    userId,
                    Notification.NotificationType.EMAIL,
                    "駐車場利用終了のお知らせ",
                    String.format("駐車場の利用が終了しました。\n終了時刻: %s\n利用時間: %s\n料金: %s円", 
                                 endTime, duration, amount),
                    Notification.Priority.NORMAL
            );
            
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 支払い完了通知を送信
     * @param request 通知リクエスト
     * @return 作成された通知
     */
    @PostMapping("/payment-completion")
    public ResponseEntity<Notification> sendPaymentCompletionNotification(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String paymentId = request.get("paymentId").toString();
            String amount = request.get("amount").toString();
            String paymentMethod = request.get("paymentMethod").toString();
            
            Notification notification = notificationService.createAndSendNotification(
                    userId,
                    Notification.NotificationType.EMAIL,
                    "支払い完了のお知らせ",
                    String.format("支払いが完了しました。\n支払いID: %s\n支払い金額: %s円\n支払い方法: %s", 
                                 paymentId, amount, paymentMethod),
                    Notification.Priority.HIGH
            );
            
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 緊急通知を送信
     * @param request 通知リクエスト
     * @return 作成された通知
     */
    @PostMapping("/emergency")
    public ResponseEntity<Notification> sendEmergencyNotification(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String emergencyType = request.get("emergencyType").toString();
            String details = request.get("details").toString();
            
            Notification notification = notificationService.createAndSendNotification(
                    userId,
                    Notification.NotificationType.SMS,
                    "緊急通知",
                    String.format("緊急通知\nタイプ: %s\n詳細: %s\n至急対応をお願いします。", 
                                 emergencyType, details),
                    Notification.Priority.CRITICAL
            );
            
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * ヘルスチェック
     * @return 通知サービスの状態
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "NotificationService");
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
} 