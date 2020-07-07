package com.shaadi.shaadimatches.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.shaadi.shaadimatches.utils.Constants;

@Entity(tableName = Constants.TABLE_NAME)
public class Profile
{
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "userID")
    private String userID;

    @ColumnInfo(name = "status")
    private boolean updatedStatus;

    public String getUserID()
    {
        return userID;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public boolean isUpdatedStatus()
    {
        return updatedStatus;
    }

    public void setUpdatedStatus(boolean updatedStatus)
    {
        this.updatedStatus = updatedStatus;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profile)) return false;

        Profile profile = (Profile) o;

        if (id != profile.id) return false;
        return userID != null ? userID.equals(profile.userID) : profile.userID == null;
    }


    @Override
    public int hashCode() {
        int result = (int) id;
        result = 31 * result + (userID != null ? userID.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", userId='" + userID + '\'' +
                ", status='" + updatedStatus + '\'' +
                '}';
    }
}
