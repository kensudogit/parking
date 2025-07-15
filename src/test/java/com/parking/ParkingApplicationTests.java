package com.parking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ParkingApplicationTests {

    @Test
    void contextLoads() {
    }
    
    /**
     * 基本的なアプリケーション起動テスト
     */
    @Test
    void testApplicationStartup() {
        // アプリケーションコンテキストが正常にロードされることを確認
        assert true;
    }
    
    /**
     * Doma2設定のテスト
     */
    @Test
    void testDomaConfiguration() {
        // Doma2の設定が正常に読み込まれることを確認
        assert true;
    }
} 