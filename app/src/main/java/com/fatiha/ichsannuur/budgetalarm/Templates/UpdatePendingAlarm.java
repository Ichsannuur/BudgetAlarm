package com.fatiha.ichsannuur.budgetalarm.Templates;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fatiha.ichsannuur.budgetalarm.Connection.AlarmReminderContract;
import com.fatiha.ichsannuur.budgetalarm.Connection.DB_Helper;
import com.fatiha.ichsannuur.budgetalarm.DashboardActivity;
import com.fatiha.ichsannuur.budgetalarm.R;
import com.fatiha.ichsannuur.budgetalarm.Reminder.AlarmScheduler;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class    UpdatePendingAlarm extends AppCompatActivity {

    DB_Helper alarm;
    TextView uri;
    EditText deskripsi, pengeluaran, tanggal, waktu, ulang, type_perulangan;
    Button update,hapus,selesai;
    ContentValues values;
    Calendar mCalendar;
    TextView font;
    Cursor cursor;
    Uri updateUri;
    SQLiteDatabase db;
    long mRepeatTime;
    int mYear, mMonth, mHour, mMinute, mDay;
    String mDate, mTime, mRepeatType;
    Intent i;

    // Constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pending_alarm);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_reminder);

        font = (TextView)findViewById(R.id.font);
        //Font
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "font.ttf");
        font.setTypeface(typeFace);


        i = getIntent();
        updateUri = i.getData();

        uri = (TextView)findViewById(R.id.uri);
        deskripsi = (EditText)findViewById(R.id.deskripsi);
        pengeluaran = (EditText)findViewById(R.id.pengeluaran);
        tanggal = (EditText)findViewById(R.id.tanggal);
        waktu = (EditText)findViewById(R.id.waktu);
        ulang = (EditText)findViewById(R.id.ulang);
        type_perulangan = (EditText)findViewById(R.id.type_perulangan);
        update = (Button)findViewById(R.id.update);
        hapus = (Button)findViewById(R.id.hapus);
        selesai = (Button)findViewById(R.id.selesai);

        alarm = new DB_Helper(this);
        db = alarm.getWritableDatabase();
        values = new ContentValues();


        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);


        // GET DATA
        cursor = db.rawQuery("SELECT * FROM alarm WHERE id = "+ Integer.parseInt(i.getStringExtra("id_pengeluaran")),null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0){
            deskripsi.setText(cursor.getString(2).toString());
            pengeluaran.setText(cursor.getString(1).toString());
            tanggal.setText(cursor.getString(3).toString());
            waktu.setText(cursor.getString(4).toString());
            ulang.setText(cursor.getString(5).toString());
            type_perulangan.setText("Every " + cursor.getString(5).toString() + " " + cursor.getString(6).toString() + "(s)");
            mRepeatType = cursor.getString(6).toString();
        }


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                mYear = year;
                mMonth = monthOfYear + 1;
                mDay = dayOfMonth;

                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                tanggal.setText(sdf.format(mCalendar.getTime()));
            }

        };

        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdatePendingAlarm.this, date,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        waktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = mCalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(UpdatePendingAlarm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mHour = selectedHour;
                        mMinute = selectedMinute;
                        waktu.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        type_perulangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = new String[5];

                items[0] = "Minute";
                items[1] = "Hour";
                items[2] = "Day";
                items[3] = "Week";
                items[4] = "Month";

                // Create List Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePendingAlarm.this);
                builder.setTitle("Select Type");
                builder.setItems(items, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int item) {

                        mRepeatType = items[item];
                        type_perulangan.setText(mRepeatType);
                        type_perulangan.setText("Every " + ulang.getText().toString() + " " + mRepeatType + "(s)");
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAlarm();
            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    db = alarm.getWritableDatabase();
                    db.execSQL("DELETE FROM alarm WHERE id ="+i.getStringExtra("id_pengeluaran"));
                    new AlarmScheduler().cancelAlarm(UpdatePendingAlarm.this, updateUri);
                    getContentResolver().notifyChange(updateUri, null);
                    i = new Intent(UpdatePendingAlarm.this, DashboardActivity.class);
                    startActivity(i);
                    finish();
                    new StyleableToast
                            .Builder(UpdatePendingAlarm.this)
                            .text("Sukses di Hapus")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#01579b"))
                            .show();
                }catch (SQLException e){
                    e.printStackTrace();
                }
//                int rowsDelete = getContentResolver().delete(updateUri,null, null);
//                if (rowsDelete != 0){
//                    new AlarmScheduler().cancelAlarm(UpdatePendingAlarm.this, updateUri);
//                    i = new Intent(UpdatePendingAlarm.this, DashboardActivity.class);
//                    startActivity(i);
//                    Toast.makeText(UpdatePendingAlarm.this, "Sukses Di hapus", Toast.LENGTH_SHORT).show();
//                    finish();
//                }else{
//                    Toast.makeText(UpdatePendingAlarm.this, "Gagal Hapus", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = alarm.getWritableDatabase();
                db.execSQL("UPDATE alarm SET status = 'success' WHERE id=" + i.getStringExtra("id_pengeluaran"));
                getContentResolver().notifyChange(updateUri, null);

                cursor = db.rawQuery("SELECT * FROM budget WHERE id_budget = 1",null);
                cursor.moveToFirst();

                int budget = Integer.parseInt(cursor.getString(1).toString());
                int biaya_pengeluaran = Integer.parseInt(pengeluaran.getText().toString());

                int subtitute_budget = budget - biaya_pengeluaran;

                db.execSQL("UPDATE budget SET budget = "+ subtitute_budget +" WHERE id_budget = 1");

                getContentResolver().notifyChange(updateUri, null);
                new AlarmScheduler().cancelAlarm(UpdatePendingAlarm.this, updateUri);

                new StyleableToast
                        .Builder(UpdatePendingAlarm.this)
                        .text("Sukses di Selesaikan")
                        .textColor(Color.WHITE)
                        .backgroundColor(Color.parseColor("#01579b"))
                        .show();

                finish();
                Intent backToDashboard = new Intent(UpdatePendingAlarm.this, DashboardActivity.class);
                startActivity(backToDashboard);
            }
        });
    }


    private void updateAlarm() {
        try {
//            values.put("deskripsi", deskripsi.getText().toString());
//            values.put("jumlah_pengeluaran", pengeluaran.getText().toString());
//            values.put(AlarmReminderContract.AlarmReminderEntry.KEY_DATE, tanggal.getText().toString());
//            values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TIME, waktu.getText().toString());
//            values.put("ulang", ulang.getText().toString());
//            values.put("type", mRepeatType);
//            values.put("status", "pending");

            // Set up calender for creating the notification
            mCalendar.set(Calendar.YEAR, mYear);
            mCalendar.set(Calendar.MONTH, --mMonth);
            mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
            mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
            mCalendar.set(Calendar.MINUTE, mMinute);
            mCalendar.set(Calendar.SECOND, 0);

            long selectedTimestamp =  mCalendar.getTimeInMillis();

            // Check repeat type
            if (mRepeatType.equals("Minute")) {
                mRepeatTime = Integer.parseInt(ulang.getText().toString()) * milMinute;
            } else if (mRepeatType.equals("Hour")) {
                mRepeatTime = Integer.parseInt(ulang.getText().toString()) * milHour;
            } else if (mRepeatType.equals("Day")) {
                mRepeatTime = Integer.parseInt(ulang.getText().toString()) * milDay;
            } else if (mRepeatType.equals("Week")) {
                mRepeatTime = Integer.parseInt(ulang.getText().toString()) * milWeek;
            } else if (mRepeatType.equals("Month")) {
                mRepeatTime = Integer.parseInt(ulang.getText().toString()) * milMonth;
            }

            db = alarm.getWritableDatabase();
            db.execSQL("UPDATE alarm SET deskripsi = '"+ deskripsi.getText().toString()
                    +"', jumlah_pengeluaran='" + pengeluaran.getText().toString()
                    +"', tanggal ='" + tanggal.getText().toString()
                    +"', waktu ='" + waktu.getText().toString()
                    +"', ulang ='" + ulang.getText().toString()
                    +"', type ='" + mRepeatType
                    +"' WHERE id="+i.getStringExtra("id_pengeluaran"));

            getContentResolver().notifyChange(updateUri, null);
            new AlarmScheduler().setRepeatAlarm(getApplicationContext(), selectedTimestamp, updateUri, mRepeatTime);

            new StyleableToast
                    .Builder(this)
                    .text("Berhasil di Update")
                    .textColor(Color.WHITE)
                    .backgroundColor(Color.parseColor("#01579b"))
                    .show();

            startActivity(new Intent(UpdatePendingAlarm.this, DashboardActivity.class));
            finish();

        }catch (SQLiteException e){
            e.getMessage();
        }
    }
}
