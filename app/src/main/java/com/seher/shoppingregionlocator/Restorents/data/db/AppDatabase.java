package com.seher.shoppingregionlocator.Restorents.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.seher.shoppingregionlocator.Restorents.app.Global;
import com.seher.shoppingregionlocator.Restorents.data.dao.RestaurantDao;
import com.seher.shoppingregionlocator.Restorents.data.entity.Places;


@Database(entities = {Places.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{

    private static AppDatabase INSTANCE;

    public abstract RestaurantDao restaurantDao();

    public static AppDatabase getAppDatabase(Context context)
    {
        if (INSTANCE == null)
        {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, Global.DATABASE_NAME)
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }

        return INSTANCE;
    }

    public static void destroyInstance()
    {
        INSTANCE = null;
    }
}