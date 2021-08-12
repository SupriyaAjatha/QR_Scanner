package com.example.barcodescanner_indian_designs_exports.room_db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "qr_codes")
public class Qr_Code_details {
    @PrimaryKey
    private int id;
    @ColumnInfo(name="qr_code_decode")
    private String qr_code_decoded_txt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQr_code_decoded_txt() {
        return qr_code_decoded_txt;
    }

    public void setQr_code_decoded_txt(String qr_code_decoded_txt) {
        this.qr_code_decoded_txt = qr_code_decoded_txt;
    }
}
