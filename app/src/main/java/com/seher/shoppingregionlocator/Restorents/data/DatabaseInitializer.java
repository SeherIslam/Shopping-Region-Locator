package com.seher.shoppingregionlocator.Restorents.data;


import com.seher.shoppingregionlocator.Restorents.data.db.AppDatabase;
import com.seher.shoppingregionlocator.Restorents.data.entity.Restaurant;

import java.util.List;

public class DatabaseInitializer
{
    private static final String TAG = DatabaseInitializer.class.getName();

    public static long insertRestaurant(final AppDatabase db, final Restaurant restaurant)
    {
        return db.restaurantDao().insert(restaurant);
    }

    public static List<Restaurant> getAllRestaurant(final AppDatabase db)
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

    public static void delete(final AppDatabase db, final Restaurant restaurant)
    {
        db.restaurantDao().delete(restaurant);
    }
}