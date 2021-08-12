package com.example.barcodescanner_indian_designs_exports.room_db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Qr_Code_details.class},version = 1,exportSchema = true)
public abstract class MyAppRoomDataBase extends RoomDatabase {
    public abstract MyDAO myDAO();
}
