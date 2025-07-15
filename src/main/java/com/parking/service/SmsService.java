package com.parking.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * SMSサービス
 * SMS送信機能を担当
 */
@Service
public class SmsService {

    @Value("${sms.provider:default}")
    private String smsProvider;

    @Value("${sms.api.key:}")
    private String smsApiKey;

    @Value("${sms.api.secret:}")
    private String smsApiSecret;

    /**
     * SMSを送信
     * @param phoneNumber 送信先電話番号
     * @param message メッセージ
     */
    public void sendSms(String phoneNumber, String message) {
        try {
            // 実際のSMS送信実装
            // ここでは簡略化のためコンソール出力
            System.out.println("=== SMS送信 ===");
            System.out.println("送信先: " + phoneNumber);
            System.out.println("メッセージ: " + message);
            System.out.println("プロバイダー: " + smsProvider);
            System.out.println("================");
            
            // 実際の実装では以下のような処理を行う
            // Twilio、AWS SNS、Nexmo等のSMSサービスを使用
            // 例：Twilioの場合
            // Twilio.init(accountSid, authToken);
            // Message.creator(new PhoneNumber(phoneNumber), fromNumber, message).create();
            
        } catch (Exception e) {
            throw new RuntimeException("SMS送信に失敗しました: " + e.getMessage(), e);
        }
    }

    /**
     * 駐車場利用開始通知SMSを送信
     * @param phoneNumber 送信先電話番号
     * @param parkingSessionId 駐車場セッションID
     * @param startTime 開始時刻
     * @param location 場所
     */
    public void sendParkingStartNotification(String phoneNumber, Long parkingSessionId, 
                                           String startTime, String location) {
        String message = String.format(
                "駐車場利用開始\n" +
                "セッションID: %d\n" +
                "開始時刻: %s\n" +
                "場所: %s",
                parkingSessionId, startTime, location
        );
        
        sendSms(phoneNumber, message);
    }

    /**
     * 駐車場利用終了通知SMSを送信
     * @param phoneNumber 送信先電話番号
     * @param parkingSessionId 駐車場セッションID
     * @param endTime 終了時刻
     * @param duration 利用時間
     * @param amount 料金
     */
    public void sendParkingEndNotification(String phoneNumber, Long parkingSessionId, 
                                         String endTime, String duration, String amount) {
        String message = String.format(
                "駐車場利用終了\n" +
                "セッションID: %d\n" +
                "終了時刻: %s\n" +
                "利用時間: %s\n" +
                "料金: %s円",
                parkingSessionId, endTime, duration, amount
        );
        
        sendSms(phoneNumber, message);
    }

    /**
     * 支払い完了通知SMSを送信
     * @param phoneNumber 送信先電話番号
     * @param paymentId 支払いID
     * @param amount 支払い金額
     * @param paymentMethod 支払い方法
     */
    public void sendPaymentCompletionNotification(String phoneNumber, Long paymentId, 
                                               String amount, String paymentMethod) {
        String message = String.format(
                "支払い完了\n" +
                "支払いID: %d\n" +
                "支払い金額: %s円\n" +
                "支払い方法: %s",
                paymentId, amount, paymentMethod
        );
        
        sendSms(phoneNumber, message);
    }

    /**
     * 緊急通知SMSを送信
     * @param phoneNumber 送信先電話番号
     * @param emergencyType 緊急タイプ
     * @param details 詳細
     */
    public void sendEmergencyNotification(String phoneNumber, String emergencyType, String details) {
        String message = String.format(
                "緊急通知\n" +
                "タイプ: %s\n" +
                "詳細: %s\n" +
                "至急対応をお願いします。",
                emergencyType, details
        );
        
        sendSms(phoneNumber, message);
    }

    /**
     * 認証コードSMSを送信
     * @param phoneNumber 送信先電話番号
     * @param verificationCode 認証コード
     * @param expiryMinutes 有効期限（分）
     */
    public void sendVerificationCode(String phoneNumber, String verificationCode, int expiryMinutes) {
        String message = String.format(
                "認証コード: %s\n" +
                "有効期限: %d分\n" +
                "このコードを入力して認証を完了してください。",
                verificationCode, expiryMinutes
        );
        
        sendSms(phoneNumber, message);
    }

    /**
     * SMS送信の有効性をチェック
     * @param phoneNumber 電話番号
     * @return 有効な場合true
     */
    public boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        
        // 基本的な電話番号形式チェック
        // 実際の実装ではより厳密なバリデーションを行う
        String cleanedNumber = phoneNumber.replaceAll("[^0-9+]", "");
        return cleanedNumber.length() >= 10 && cleanedNumber.length() <= 15;
    }

    /**
     * SMS送信統計を取得
     * @return 統計情報
     */
    public SmsStatistics getSmsStatistics() {
        // 実際の実装ではデータベースから統計を取得
        return new SmsStatistics(100, 95, 5);
    }

    /**
     * SMS統計クラス
     */
    public static class SmsStatistics {
        private final long totalSent;
        private final long successfulSent;
        private final long failedSent;

        public SmsStatistics(long totalSent, long successfulSent, long failedSent) {
            this.totalSent = totalSent;
            this.successfulSent = successfulSent;
            this.failedSent = failedSent;
        }

        // Getter methods
        public long getTotalSent() { return totalSent; }
        public long getSuccessfulSent() { return successfulSent; }
        public long getFailedSent() { return failedSent; }
        public long getSuccessRate() {
            return totalSent > 0 ? (successfulSent * 100 / totalSent) : 0;
        }
    }
} 