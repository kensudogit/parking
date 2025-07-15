package com.parking.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * メールサービス
 * メール送信機能を担当
 */
@Service
public class EmailService {

    @Value("${spring.mail.host:localhost}")
    private String mailHost;

    @Value("${spring.mail.port:587}")
    private int mailPort;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    @Value("${spring.mail.password:}")
    private String mailPassword;

    /**
     * メールを送信
     * @param to 送信先メールアドレス
     * @param subject 件名
     * @param content 本文
     */
    public void sendEmail(String to, String subject, String content) {
        try {
            // 実際のメール送信実装
            // ここでは簡略化のためコンソール出力
            System.out.println("=== メール送信 ===");
            System.out.println("送信先: " + to);
            System.out.println("件名: " + subject);
            System.out.println("本文: " + content);
            System.out.println("==================");
            
            // 実際の実装では以下のような処理を行う
            // JavaMailSenderを使用したメール送信
            // SimpleMailMessage message = new SimpleMailMessage();
            // message.setTo(to);
            // message.setSubject(subject);
            // message.setText(content);
            // mailSender.send(message);
            
        } catch (Exception e) {
            throw new RuntimeException("メール送信に失敗しました: " + e.getMessage(), e);
        }
    }

    /**
     * HTMLメールを送信
     * @param to 送信先メールアドレス
     * @param subject 件名
     * @param htmlContent HTML本文
     */
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            // HTMLメール送信の実装
            System.out.println("=== HTMLメール送信 ===");
            System.out.println("送信先: " + to);
            System.out.println("件名: " + subject);
            System.out.println("HTML本文: " + htmlContent);
            System.out.println("=====================");
            
        } catch (Exception e) {
            throw new RuntimeException("HTMLメール送信に失敗しました: " + e.getMessage(), e);
        }
    }

    /**
     * 駐車場利用開始通知メールを送信
     * @param to 送信先メールアドレス
     * @param parkingSessionId 駐車場セッションID
     * @param startTime 開始時刻
     * @param location 場所
     */
    public void sendParkingStartNotification(String to, Long parkingSessionId, 
                                           String startTime, String location) {
        String subject = "駐車場利用開始のお知らせ";
        String content = String.format(
                "駐車場の利用が開始されました。\n\n" +
                "セッションID: %d\n" +
                "開始時刻: %s\n" +
                "場所: %s\n\n" +
                "ご利用ありがとうございます。",
                parkingSessionId, startTime, location
        );
        
        sendEmail(to, subject, content);
    }

    /**
     * 駐車場利用終了通知メールを送信
     * @param to 送信先メールアドレス
     * @param parkingSessionId 駐車場セッションID
     * @param endTime 終了時刻
     * @param duration 利用時間
     * @param amount 料金
     */
    public void sendParkingEndNotification(String to, Long parkingSessionId, 
                                         String endTime, String duration, String amount) {
        String subject = "駐車場利用終了のお知らせ";
        String content = String.format(
                "駐車場の利用が終了しました。\n\n" +
                "セッションID: %d\n" +
                "終了時刻: %s\n" +
                "利用時間: %s\n" +
                "料金: %s円\n\n" +
                "ご利用ありがとうございました。",
                parkingSessionId, endTime, duration, amount
        );
        
        sendEmail(to, subject, content);
    }

    /**
     * 支払い完了通知メールを送信
     * @param to 送信先メールアドレス
     * @param paymentId 支払いID
     * @param amount 支払い金額
     * @param paymentMethod 支払い方法
     */
    public void sendPaymentCompletionNotification(String to, Long paymentId, 
                                               String amount, String paymentMethod) {
        String subject = "支払い完了のお知らせ";
        String content = String.format(
                "支払いが完了しました。\n\n" +
                "支払いID: %d\n" +
                "支払い金額: %s円\n" +
                "支払い方法: %s\n\n" +
                "ご利用ありがとうございました。",
                paymentId, amount, paymentMethod
        );
        
        sendEmail(to, subject, content);
    }

    /**
     * パスワードリセットメールを送信
     * @param to 送信先メールアドレス
     * @param resetToken リセットトークン
     * @param resetUrl リセットURL
     */
    public void sendPasswordResetEmail(String to, String resetToken, String resetUrl) {
        String subject = "パスワードリセットのお知らせ";
        String content = String.format(
                "パスワードリセットのリクエストを受け付けました。\n\n" +
                "以下のリンクをクリックしてパスワードをリセットしてください：\n" +
                "%s\n\n" +
                "このリンクは24時間有効です。\n" +
                "リクエストしていない場合は、このメールを無視してください。",
                resetUrl + "?token=" + resetToken
        );
        
        sendEmail(to, subject, content);
    }
} 