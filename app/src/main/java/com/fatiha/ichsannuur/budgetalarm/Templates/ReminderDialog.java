package com.fatiha.ichsannuur.budgetalarm.Templates;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fatiha.ichsannuur.budgetalarm.AddAlarm;
import com.fatiha.ichsannuur.budgetalarm.Connection.DB_Helper;
import com.fatiha.ichsannuur.budgetalarm.DashboardActivity;
import com.fatiha.ichsannuur.budgetalarm.R;
import com.fatiha.ichsannuur.budgetalarm.Reminder.AlarmScheduler;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class ReminderDialog extends AppCompatActivity {
    TextView text;
    Button accept, ignore;
    DB_Helper database;
    Intent i;
    Uri currentUri;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_dialog);

        database = new DB_Helper(this);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(false);
        text = (TextView)dialog.findViewById(R.id.subtitle);
        accept = (Button)dialog.findViewById(R.id.accept);
        ignore = (Button)dialog.findViewById(R.id.ignore);
        dialog.show();

        i = getIntent();
        currentUri = i.getData();

        text.setText("Hai Kami ingatkan bahwa Anda memiliki catatan "+ i.getStringExtra("deskripsi").toUpperCase() + "" +
                " sebesar Rp. " + i.getIntExtra("jumlah_pengeluaran",0));


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase query = database.getWritableDatabase();
                try {
                    query.execSQL("UPDATE alarm SET status = 'success'" +
                            "WHERE id = " + i.getIntExtra("id",0));
                    cursor = query.rawQuery("SELECT * FROM budget WHERE id_budget = 1",null);
                    cursor.moveToFirst();

                    int budget = Integer.parseInt(cursor.getString(1).toString());
                    int pengeluaran = i.getIntExtra("jumlah_pengeluaran",0);

                    int subtitute_budget = budget - pengeluaran;

                    query.execSQL("UPDATE budget SET budget = "+ subtitute_budget +" WHERE id_budget = 1");
                    new AlarmScheduler().cancelAlarm(getApplicationContext(), currentUri);

                    new StyleableToast
                            .Builder(ReminderDialog.this)
                            .text("Sukses di Bayar")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#01579b"))
                            .show();
                    finish();
                    startActivity(new Intent(ReminderDialog.this, DashboardActivity.class));
                    dialog.dismiss();

                }catch (SQLiteException e){
                    e.printStackTrace();
                }
            }
        });

        ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase query = database.getWritableDatabase();
                try {
                    query.execSQL("UPDATE alarm SET status = 'cancel'" +
                            "WHERE id = " + i.getIntExtra("id",0));
                    new StyleableToast
                            .Builder(ReminderDialog.this)
                            .text("Catatan di Cancel")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#01579b"))
                            .show();
                    new AlarmScheduler().cancelAlarm(getApplicationContext(), currentUri);

                    finish();
                    startActivity(new Intent(ReminderDialog.this, DashboardActivity.class));
                    dialog.dismiss();

                }catch (SQLiteException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
