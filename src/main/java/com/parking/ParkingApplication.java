package com.parking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 駐車場管理システムのメインアプリケーションクラス
 * Spring Bootアプリケーションのエントリーポイント
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.parking")
public class ParkingApplication {

    /**
     * アプリケーションのメインエントリーポイント
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        SpringApplication.run(ParkingApplication.class, args);
    }
    
    /**
     * アプリケーションを起動するメソッド
     * IDEで▷マークが表示されるようにするためのメソッド
     */
    public static void run() {
        SpringApplication.run(ParkingApplication.class);
    }
    
    /**
     * 開発環境用の起動メソッド
     * 開発プロファイルを有効にしてアプリケーションを起動
     */
    public static void runDev() {
        System.setProperty("spring.profiles.active", "dev");
        SpringApplication.run(ParkingApplication.class);
    }
    
    /**
     * デバッグモードで起動するメソッド
     * 開発プロファイルとデバッグモードを有効にしてアプリケーションを起動
     */
    public static void runDebug() {
        System.setProperty("spring.profiles.active", "dev");
        System.setProperty("debug", "true");
        SpringApplication.run(ParkingApplication.class);
    }
} 