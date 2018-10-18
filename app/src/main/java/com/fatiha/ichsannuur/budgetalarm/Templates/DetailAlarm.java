package com.fatiha.ichsannuur.budgetalarm.Templates;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fatiha.ichsannuur.budgetalarm.Connection.DB_Helper;
import com.fatiha.ichsannuur.budgetalarm.DashboardActivity;
import com.fatiha.ichsannuur.budgetalarm.R;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class DetailAlarm extends AppCompatActivity {

    DB_Helper database;
    Cursor cursor;
    TextView deskripsi, pengeluaran, tanggal, waktu, ulang, type_perulangan;
    Button delete, kembali;
    ImageView imageBackground;
    SQLiteDatabase query;
    TextView font;
    int time;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_success_alarm);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_reminder);

        font = (TextView)findViewById(R.id.font);
        //Font
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "font.ttf");
        font.setTypeface(typeFace);

        deskripsi = (TextView)findViewById(R.id.deskripsi);
        pengeluaran = (TextView)findViewById(R.id.pengeluaran);
        tanggal = (TextView)findViewById(R.id.tanggal);
        waktu = (TextView)findViewById(R.id.waktu);
        ulang = (TextView)findViewById(R.id.ulang);
        type_perulangan = (TextView)findViewById(R.id.type_perulangan);
        delete = (Button)findViewById(R.id.delete);
        kembali = (Button)findViewById(R.id.kembali);
        imageBackground = (ImageView)findViewById(R.id.imageBackground);

        i = getIntent();
        database = new DB_Helper(this);


        query = database.getWritableDatabase();
        cursor = query.rawQuery("SELECT * FROM alarm WHERE id = " + i.getStringExtra("id_pengeluaran"),null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0){
            deskripsi.setText(cursor.getString(2).toString());
            pengeluaran.setText("Rp. " + cursor.getString(1).toString());
            tanggal.setText(cursor.getString(3).toString());
            waktu.setText(cursor.getString(4).toString());
            ulang.setText(cursor.getString(5).toString());
            type_perulangan.setText("Every " + cursor.getString(5).toString() + " " + cursor.getString(6).toString() + "(s)");

            if (time >= 6 && time <= 18) {
                imageBackground.setImageResource(R.drawable.day);
            }else{
                imageBackground.setImageResource(R.drawable.night);
            }
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query = database.getWritableDatabase();
                query.execSQL("DELETE FROM alarm WHERE id = " + i.getStringExtra("id_pengeluaran"));
                i = new Intent(DetailAlarm.this, DashboardActivity.class);
                startActivity(i);
                new StyleableToast
                        .Builder(DetailAlarm.this)
                        .text("Berhasil di Delete")
                        .textColor(Color.WHITE)
                        .backgroundColor(Color.parseColor("#01579b"))
                        .show();
                finish();
            }
        });

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailAlarm.this, DashboardActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DetailAlarm.this, DashboardActivity.class));
        finish();
    }
}
