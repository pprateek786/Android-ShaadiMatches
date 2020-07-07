package com.shaadi.shaadimatches.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.shaadi.shaadimatches.database.model.Profile;
import com.shaadi.shaadimatches.utils.Constants;

import java.util.List;


@Dao
public interface ProfileDao
{
    @Insert
    void insertProfile(Profile profiles);

    @Query("UPDATE profile SET status = :st WHERE userID = :user")
    void updateProfile(String user,boolean st);

    @Query("SELECT * FROM profile WHERE userID = :userName AND " +
            "status = 1")
    Profile getProfile(String userName);
}
