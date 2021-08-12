package com.example.barcodescanner_indian_designs_exports.room_db;

import android.widget.LinearLayout;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface MyDAO {
    //CRUD OPERATIONS PERFORMED HERE
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void addQr_codes_decodes(Qr_Code_details qr_code_details);

    @Query("select * from qr_codes")
    public List<Qr_Code_details>get_all_Qr_code_details();

    @Delete
    public void delete_QrCode(Qr_Code_details qr_code_details);

}
