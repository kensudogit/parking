package com.parking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.parking")
public class ParkingApplication {

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
     */
    public static void runDev() {
        System.setProperty("spring.profiles.active", "dev");
        SpringApplication.run(ParkingApplication.class);
    }
    
    /**
     * デバッグモードで起動するメソッド
     */
    public static void runDebug() {
        System.setProperty("spring.profiles.active", "dev");
        System.setProperty("debug", "true");
        SpringApplication.run(ParkingApplication.class);
    }
} 