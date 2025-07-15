package com.parking.config;

import com.parking.entity.ParkingSpot;
import com.parking.repository.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * データローダー
 * アプリケーション起動時にサンプルデータを自動挿入
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final ParkingSpotRepository parkingSpotRepository;

    /**
     * アプリケーション起動時に実行されるメソッド
     * データベースが空の場合、サンプルデータを挿入
     * @param args コマンドライン引数
     * @throws Exception 例外
     */
    @Override
    public void run(String... args) throws Exception {
        // サンプルデータが既に存在するかチェック
        if (parkingSpotRepository.count() == 0) {
            log.info("サンプルデータを挿入しています...");
            
            // 通常スペットのサンプルデータを挿入
            ParkingSpot spot1 = new ParkingSpot();
            spot1.setSpotNumber("A-001");
            spot1.setSpotType(ParkingSpot.SpotType.REGULAR);
            spot1.setStatus(ParkingSpot.SpotStatus.AVAILABLE);
            spot1.setFloorLevel(1);
            spot1.setHourlyRate(5.0);
            parkingSpotRepository.save(spot1);

            // 通常スペット（続き）
            ParkingSpot spot2 = new ParkingSpot();
            spot2.setSpotNumber("A-002");
            spot2.setSpotType(ParkingSpot.SpotType.REGULAR);
            spot2.setStatus(ParkingSpot.SpotStatus.AVAILABLE);
            spot2.setFloorLevel(1);
            spot2.setHourlyRate(5.0);
            parkingSpotRepository.save(spot2);

            ParkingSpot spot3 = new ParkingSpot();
            spot3.setSpotNumber("A-003");
            spot3.setSpotType(ParkingSpot.SpotType.REGULAR);
            spot3.setStatus(ParkingSpot.SpotStatus.AVAILABLE);
            spot3.setFloorLevel(1);
            spot3.setHourlyRate(5.0);
            parkingSpotRepository.save(spot3);

            // 障害者用スペットのサンプルデータを挿入
            ParkingSpot spot4 = new ParkingSpot();
            spot4.setSpotNumber("B-001");
            spot4.setSpotType(ParkingSpot.SpotType.DISABLED);
            spot4.setStatus(ParkingSpot.SpotStatus.AVAILABLE);
            spot4.setFloorLevel(1);
            spot4.setHourlyRate(3.0);
            parkingSpotRepository.save(spot4);

            ParkingSpot spot5 = new ParkingSpot();
            spot5.setSpotNumber("B-002");
            spot5.setSpotType(ParkingSpot.SpotType.DISABLED);
            spot5.setStatus(ParkingSpot.SpotStatus.AVAILABLE);
            spot5.setFloorLevel(1);
            spot5.setHourlyRate(3.0);
            parkingSpotRepository.save(spot5);

            // 充電器付きスペットのサンプルデータを挿入
            ParkingSpot spot6 = new ParkingSpot();
            spot6.setSpotNumber("C-001");
            spot6.setSpotType(ParkingSpot.SpotType.ELECTRIC_CHARGING);
            spot6.setStatus(ParkingSpot.SpotStatus.AVAILABLE);
            spot6.setFloorLevel(1);
            spot6.setHourlyRate(7.0);
            parkingSpotRepository.save(spot6);

            ParkingSpot spot7 = new ParkingSpot();
            spot7.setSpotNumber("C-002");
            spot7.setSpotType(ParkingSpot.SpotType.ELECTRIC_CHARGING);
            spot7.setStatus(ParkingSpot.SpotStatus.AVAILABLE);
            spot7.setFloorLevel(1);
            spot7.setHourlyRate(7.0);
            parkingSpotRepository.save(spot7);

            // バイク用スペットのサンプルデータを挿入
            ParkingSpot spot8 = new ParkingSpot();
            spot8.setSpotNumber("D-001");
            spot8.setSpotType(ParkingSpot.SpotType.MOTORCYCLE);
            spot8.setStatus(ParkingSpot.SpotStatus.AVAILABLE);
            spot8.setFloorLevel(1);
            spot8.setHourlyRate(2.0);
            parkingSpotRepository.save(spot8);

            ParkingSpot spot9 = new ParkingSpot();
            spot9.setSpotNumber("D-002");
            spot9.setSpotType(ParkingSpot.SpotType.MOTORCYCLE);
            spot9.setStatus(ParkingSpot.SpotStatus.AVAILABLE);
            spot9.setFloorLevel(1);
            spot9.setHourlyRate(2.0);
            parkingSpotRepository.save(spot9);

            // トラック用スペットのサンプルデータを挿入
            ParkingSpot spot10 = new ParkingSpot();
            spot10.setSpotNumber("E-001");
            spot10.setSpotType(ParkingSpot.SpotType.TRUCK);
            spot10.setStatus(ParkingSpot.SpotStatus.AVAILABLE);
            spot10.setFloorLevel(1);
            spot10.setHourlyRate(10.0);
            parkingSpotRepository.save(spot10);

            log.info("サンプルデータの挿入が完了しました。");
        } else {
            log.info("サンプルデータは既に存在します。");
        }
    }
} 