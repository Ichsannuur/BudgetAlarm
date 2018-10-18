package com.fatiha.ichsannuur.budgetalarm.Connection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.fatiha.ichsannuur.budgetalarm.Object.Alarm;

import java.util.ArrayList;
import java.util.List;

public class DB_Helper extends SQLiteOpenHelper {

    public DB_Helper(Context context){
        super(context, "budgetdb.db", null ,2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String alarm = "CREATE TABLE alarm(" +
                "id INTEGER PRIMARY KEY," +
                "jumlah_pengeluaran INTEGER," +
                "deskripsi text NOT NULL," +
                "tanggal text NOT NULL," +
                "waktu text NOT NULL," +
                "ulang text NOT NULL," +
                "type text NOT NULL," +
                "status text NOT NULL);";
        db.execSQL(alarm);

        String budget = "CREATE TABLE budget(" +
                "id_budget INTEGER PRIMARY KEY," +
                "budget INTEGER);";

        db.execSQL(budget);

        String add_budget = "INSERT INTO budget VALUES(1,0)";
        db.execSQL(add_budget);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS alarm");
        db.execSQL("DROP TABLE IF EXISTS budget");
    }


    public List<Alarm> getdata(){

        List<Alarm> data=new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM alarm WHERE status = 'pending' ORDER BY id DESC;",null);
        StringBuffer stringBuffer = new StringBuffer();
        Alarm dataModel = null;
        while (cursor.moveToNext()) {
            dataModel= new Alarm();
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String deskripsi = cursor.getString(cursor.getColumnIndexOrThrow("deskripsi"));
            int pengeluaran = cursor.getInt(cursor.getColumnIndexOrThrow("jumlah_pengeluaran"));
            String tanggal = cursor.getString(cursor.getColumnIndexOrThrow("tanggal"));
            String waktu = cursor.getString(cursor.getColumnIndexOrThrow("waktu"));
            String ulang = cursor.getString(cursor.getColumnIndexOrThrow("ulang"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

            dataModel.setId_pengeluaran(id);
            dataModel.setDeskripsi(deskripsi);
            dataModel.setJumlah_pengeluaran(pengeluaran);
            dataModel.setTanggal(tanggal);
            dataModel.setWaktu(waktu);
            dataModel.setUlang(ulang);
            dataModel.setType(type);
            dataModel.setStatus(status);
            stringBuffer.append(dataModel);
            // stringBuffer.append(dataModel);
            data.add(dataModel);
        }

        return data;
    }

    public List<Alarm> getdataOrderBySuccess(){

        List<Alarm> data=new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM alarm WHERE status = 'success' ORDER BY id DESC;",null);
        StringBuffer stringBuffer = new StringBuffer();
        Alarm dataModel = null;
        while (cursor.moveToNext()) {
            dataModel= new Alarm();
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String deskripsi = cursor.getString(cursor.getColumnIndexOrThrow("deskripsi"));
            int pengeluaran = cursor.getInt(cursor.getColumnIndexOrThrow("jumlah_pengeluaran"));
            String tanggal = cursor.getString(cursor.getColumnIndexOrThrow("tanggal"));
            String waktu = cursor.getString(cursor.getColumnIndexOrThrow("waktu"));
            String ulang = cursor.getString(cursor.getColumnIndexOrThrow("ulang"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

            dataModel.setId_pengeluaran(id);
            dataModel.setDeskripsi(deskripsi);
            dataModel.setJumlah_pengeluaran(pengeluaran);
            dataModel.setTanggal(tanggal);
            dataModel.setWaktu(waktu);
            dataModel.setUlang(ulang);
            dataModel.setStatus(status);
            stringBuffer.append(dataModel);
            // stringBuffer.append(dataModel);
            data.add(dataModel);
        }

        return data;
    }


    public List<Alarm> getdataOrderByCancel(){

        List<Alarm> data=new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM alarm WHERE status = 'cancel' ORDER BY id DESC;",null);
        StringBuffer stringBuffer = new StringBuffer();
        Alarm dataModel = null;
        while (cursor.moveToNext()) {
            dataModel= new Alarm();
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String deskripsi = cursor.getString(cursor.getColumnIndexOrThrow("deskripsi"));
            int pengeluaran = cursor.getInt(cursor.getColumnIndexOrThrow("jumlah_pengeluaran"));
            String tanggal = cursor.getString(cursor.getColumnIndexOrThrow("tanggal"));
            String waktu = cursor.getString(cursor.getColumnIndexOrThrow("waktu"));
            String ulang = cursor.getString(cursor.getColumnIndexOrThrow("ulang"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

            dataModel.setId_pengeluaran(id);
            dataModel.setDeskripsi(deskripsi);
            dataModel.setJumlah_pengeluaran(pengeluaran);
            dataModel.setTanggal(tanggal);
            dataModel.setWaktu(waktu);
            dataModel.setUlang(ulang);
            dataModel.setStatus(status);
            stringBuffer.append(dataModel);
            // stringBuffer.append(dataModel);
            data.add(dataModel);
        }

        return data;
    }
}
