package com.shaadi.shaadimatches.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.shaadi.shaadimatches.database.dao.ProfileDao;
import com.shaadi.shaadimatches.database.model.Profile;
import com.shaadi.shaadimatches.utils.Constants;

@Database(entities = { Profile.class }, version = 1)
public abstract class ProfileDatabase extends RoomDatabase
{
    public abstract ProfileDao getProfileDao();


    private static ProfileDatabase profileDatabase;

    // synchronized is use to avoid concurrent access in multithred environment
    public static /*synchronized*/ ProfileDatabase getInstance(Context context) {
        if (null == profileDatabase) {
            profileDatabase = buildDatabaseInstance(context);
        }
        return profileDatabase;
    }

    private static ProfileDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                ProfileDatabase.class,
                Constants.DB_NAME).allowMainThreadQueries().build();
    }

    public  void cleanUp(){
        profileDatabase = null;
    }
}
