package com.seher.shoppingregionlocator.Restorents.data.dao;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.seher.shoppingregionlocator.Restorents.data.entity.Places;

import java.util.List;


@Dao
public interface RestaurantDao
{
    @Query("SELECT * FROM Places")
    List<Places> getAll();

    @Query("SELECT COUNT(*) FROM Places WHERE place_id =:id")
    int count(String id);

    @Query("SELECT COUNT(*) FROM Places")
    int count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Places restaurant);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Places> restaurants);

    @Delete
    void delete(Places restaurant);
}