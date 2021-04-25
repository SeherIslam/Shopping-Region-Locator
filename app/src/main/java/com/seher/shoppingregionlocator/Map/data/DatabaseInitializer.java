package com.seher.shoppingregionlocator.Map.data;


import com.seher.shoppingregionlocator.Map.data.db.AppDatabase;
import com.seher.shoppingregionlocator.Map.data.entity.Places;

import java.util.List;

public class DatabaseInitializer
{
    private static final String TAG = DatabaseInitializer.class.getName();

    public static long insertRestaurant(final AppDatabase db, final Places places)
    {
        return db.restaurantDao().insert(places);
    }

    public static List<Places> getAllRestaurant(final AppDatabase db)
    {
        return db.restaurantDao().getAll();
    }

    public static int count(final AppDatabase db)
    {
        return db.restaurantDao().count();
    }

    public static int count(final AppDatabase db, final String place_id)
    {
        return db.restaurantDao().count(place_id);
    }

    public static void delete(final AppDatabase db, final Places places)
    {
        db.restaurantDao().delete(places);
    }
}