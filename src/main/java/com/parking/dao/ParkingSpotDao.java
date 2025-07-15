package com.parking.dao;

import com.parking.entity.ParkingSpotDoma;
import org.seasar.doma.*;
import org.springframework.stereotype.Repository;
import java.util.List;

@Dao
@Repository
public interface ParkingSpotDao {
    
    @Select
    List<ParkingSpotDoma> selectAll();
    
    @Select
    ParkingSpotDoma selectById(Long id);
    
    @Insert
    int insert(ParkingSpotDoma parkingSpot);
    
    @Update
    int update(ParkingSpotDoma parkingSpot);
    
    @Delete
    int delete(ParkingSpotDoma parkingSpot);
} 