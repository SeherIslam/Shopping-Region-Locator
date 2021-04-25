package com.seher.shoppingregionlocator.Map.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.seher.shoppingregionlocator.Map.app.Global;
import com.seher.shoppingregionlocator.Map.data.dao.PlacesDao;
import com.seher.shoppingregionlocator.Map.data.entity.Places;


@Database(entities = {Places.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{

    private static AppDatabase INSTANCE;

    public abstract PlacesDao restaurantDao();

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