package com.parking.dao;

import com.parking.entity.ParkingSpotDoma;
import org.seasar.doma.*;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 駐車場スペットDAO
 * Doma2フレームワークを使用した駐車場スペットのデータアクセス
 */
@Dao
@Repository
public interface ParkingSpotDao {
    
    /**
     * すべての駐車場スペットを取得
     * @return 駐車場スペットのリスト
     */
    @Select
    List<ParkingSpotDoma> selectAll();
    
    /**
     * IDで駐車場スペットを取得
     * @param id スペットID
     * @return 駐車場スペット
     */
    @Select
    ParkingSpotDoma selectById(Long id);
    
    /**
     * 駐車場スペットを挿入
     * @param parkingSpot 挿入するスペット
     * @return 更新された行数
     */
    @Insert
    int insert(ParkingSpotDoma parkingSpot);
    
    /**
     * 駐車場スペットを更新
     * @param parkingSpot 更新するスペット
     * @return 更新された行数
     */
    @Update
    int update(ParkingSpotDoma parkingSpot);
    
    /**
     * 駐車場スペットを削除
     * @param parkingSpot 削除するスペット
     * @return 削除された行数
     */
    @Delete
    int delete(ParkingSpotDoma parkingSpot);
} 