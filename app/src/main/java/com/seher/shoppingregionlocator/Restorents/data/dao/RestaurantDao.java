package com.seher.shoppingregionlocator.Restorents.data.dao;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.seher.shoppingregionlocator.Restorents.data.entity.Restaurant;

import java.util.List;


@Dao
public interface RestaurantDao
{
    @Query("SELECT * FROM RESTAURANT")
    List<Restaurant> getAll();

    @Query("SELECT COUNT(*) FROM RESTAURANT WHERE place_id =:id")
    int count(String id);

    @Query("SELECT COUNT(*) FROM RESTAURANT")
    int count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Restaurant restaurant);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Restaurant> restaurants);

    @Delete
    void delete(Restaurant restaurant);
}