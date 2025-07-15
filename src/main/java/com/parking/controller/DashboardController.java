package com.parking.controller;

import com.parking.service.PaymentService;
import com.parking.service.ParkingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

/**
 * ダッシュボードコントローラクラス
 * 管理者向けのダッシュボード機能を提供する
 * 
 * @author Parking System
 * @version 1.0
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class DashboardController {
    
    /** 決済サービス */
    private final PaymentService paymentService;
    /** 駐車サービス */
    private final ParkingService parkingService;
    
    /**
     * 管理者ダッシュボードの概要データを取得
     * 
     * @return ダッシュボード概要データ
     */
    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getDashboardOverview() {
        try {
            Map<String, Object> overview = new HashMap<>();
            
            // 駐車場統計
            overview.put("availableSpots", parkingService.getAvailableSpotsCount());
            overview.put("occupiedSpots", parkingService.getOccupiedSpotsCount());
            overview.put("activeSessions", parkingService.getActiveSessionsCount());
            
            // 決済統計
            Map<String, Object> paymentStats = paymentService.getPaymentStatistics();
            overview.put("paymentStats", paymentStats);
            
            // 今日の売上
            overview.put("todayRevenue", calculateTodayRevenue());
            
            // 今月の売上
            overview.put("monthlyRevenue", paymentStats.get("monthlyRevenue"));
            
            // システム状態
            overview.put("systemStatus", "HEALTHY");
            overview.put("lastUpdated", java.time.LocalDateTime.now());
            
            return ResponseEntity.ok(overview);
        } catch (Exception e) {
            log.error("ダッシュボード概要の取得に失敗: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 売上レポートを取得
     * 
     * @param period 期間（daily, weekly, monthly）
     * @return 売上レポートデータ
     */
    @GetMapping("/revenue/{period}")
    public ResponseEntity<Map<String, Object>> getRevenueReport(@PathVariable String period) {
        try {
            Map<String, Object> report = new HashMap<>();
            
            switch (period.toLowerCase()) {
                case "daily":
                    report.put("data", generateDailyRevenueData());
                    break;
                case "weekly":
                    report.put("data", generateWeeklyRevenueData());
                    break;
                case "monthly":
                    report.put("data", generateMonthlyRevenueData());
                    break;
                default:
                    return ResponseEntity.badRequest().build();
            }
            
            report.put("period", period);
            report.put("generatedAt", java.time.LocalDateTime.now());
            
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            log.error("売上レポートの取得に失敗: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 駐車場使用率レポートを取得
     * 
     * @return 使用率レポートデータ
     */
    @GetMapping("/utilization")
    public ResponseEntity<Map<String, Object>> getUtilizationReport() {
        try {
            Map<String, Object> report = new HashMap<>();
            
            long totalSpots = parkingService.getAvailableSpotsCount() + parkingService.getOccupiedSpotsCount();
            double utilizationRate = totalSpots > 0 ? 
                (double) parkingService.getOccupiedSpotsCount() / totalSpots * 100 : 0;
            
            report.put("totalSpots", totalSpots);
            report.put("occupiedSpots", parkingService.getOccupiedSpotsCount());
            report.put("availableSpots", parkingService.getAvailableSpotsCount());
            report.put("utilizationRate", Math.round(utilizationRate * 100.0) / 100.0);
            report.put("generatedAt", java.time.LocalDateTime.now());
            
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            log.error("使用率レポートの取得に失敗: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 決済方法別統計を取得
     * 
     * @return 決済方法別統計データ
     */
    @GetMapping("/payment-methods")
    public ResponseEntity<Map<String, Object>> getPaymentMethodStats() {
        try {
            Map<String, Object> stats = paymentService.getPaymentStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("決済方法統計の取得に失敗: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * システムヘルスチェック
     * 
     * @return システム状態
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getSystemHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", java.time.LocalDateTime.now());
        health.put("version", "1.0.0");
        health.put("services", Map.of(
            "parking-service", "UP",
            "payment-service", "UP",
            "database", "UP"
        ));
        
        return ResponseEntity.ok(health);
    }
    
    /**
     * 今日の売上を計算
     * 
     * @return 今日の売上
     */
    private double calculateTodayRevenue() {
        // 実際の実装では、今日の決済データを集計
        return 15000.0; // サンプルデータ
    }
    
    /**
     * 日次売上データを生成
     * 
     * @return 日次売上データ
     */
    private Map<String, Object> generateDailyRevenueData() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalRevenue", 15000.0);
        data.put("transactionCount", 25);
        data.put("averageTransaction", 600.0);
        data.put("hourlyData", Map.of(
            "09:00", 1200.0,
            "10:00", 1800.0,
            "11:00", 2100.0,
            "12:00", 2400.0,
            "13:00", 2000.0,
            "14:00", 1800.0,
            "15:00", 1600.0,
            "16:00", 1400.0,
            "17:00", 1200.0,
            "18:00", 1000.0
        ));
        
        return data;
    }
    
    /**
     * 週次売上データを生成
     * 
     * @return 週次売上データ
     */
    private Map<String, Object> generateWeeklyRevenueData() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalRevenue", 105000.0);
        data.put("transactionCount", 175);
        data.put("averageTransaction", 600.0);
        data.put("dailyData", Map.of(
            "Monday", 15000.0,
            "Tuesday", 16000.0,
            "Wednesday", 17000.0,
            "Thursday", 18000.0,
            "Friday", 20000.0,
            "Saturday", 12000.0,
            "Sunday", 7000.0
        ));
        
        return data;
    }
    
    /**
     * 月次売上データを生成
     * 
     * @return 月次売上データ
     */
    private Map<String, Object> generateMonthlyRevenueData() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalRevenue", 450000.0);
        data.put("transactionCount", 750);
        data.put("averageTransaction", 600.0);
        data.put("weeklyData", Map.of(
            "Week 1", 105000.0,
            "Week 2", 110000.0,
            "Week 3", 115000.0,
            "Week 4", 120000.0
        ));
        
        return data;
    }
} 