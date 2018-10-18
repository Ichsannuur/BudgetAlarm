package com.fatiha.ichsannuur.budgetalarm;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.fatiha.ichsannuur.budgetalarm.Reminder.AlarmScheduler;
import com.fatiha.ichsannuur.budgetalarm.Templates.UpdatePendingAlarm;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddAlarm extends AppCompatActivity{

    DB_Helper alarm;
    EditText deskripsi, pengeluaran, tanggal, waktu, ulang, type_perulangan;
    Button simpan, kembali;
    TextView font;
    SQLiteDatabase db;
    ContentValues values;
    Calendar mCalendar;
    long mRepeatTime;
    Cursor cursor;
    int mYear, mMonth, mHour, mMinute, mDay;
    String mDate, mTime, mRepeatType;

    // Constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_reminder);

        font = (TextView)findViewById(R.id.font);
        //Font
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "font.ttf");
        font.setTypeface(typeFace);

        deskripsi = (EditText)findViewById(R.id.deskripsi);
        pengeluaran = (EditText)findViewById(R.id.pengeluaran);
        tanggal = (EditText)findViewById(R.id.tanggal);
        waktu = (EditText)findViewById(R.id.waktu);
        ulang = (EditText)findViewById(R.id.ulang);
        type_perulangan = (EditText)findViewById(R.id.type_perulangan);
        simpan = (Button)findViewById(R.id.simpan);
        kembali = (Button)findViewById(R.id.back);

        alarm = new DB_Helper(this);
        db = alarm.getWritableDatabase();
        values = new ContentValues();


        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);

        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTime = String.format("%02d:%02d", mHour, mMinute);

        tanggal.setText(mDate);
        waktu.setText(mTime);
        ulang.setText("1");


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
                new DatePickerDialog(AddAlarm.this, date,
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
                mTimePicker = new TimePickerDialog(AddAlarm.this, new TimePickerDialog.OnTimeSetListener() {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AddAlarm.this);
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


        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deskripsi.getText().toString().equals("")){
                    new StyleableToast
                            .Builder(AddAlarm.this)
                            .text("Lengkapi Data Terlebih Dahulu")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#c62828"))
                            .show();
                }else if(pengeluaran.getText().toString().length() == 0){
                    new StyleableToast
                            .Builder(AddAlarm.this)
                            .text("Lengkapi Data Terlebih Dahulu")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#c62828"))
                            .show();
                }else if(type_perulangan.getText().toString().equals("")){
                    new StyleableToast
                            .Builder(AddAlarm.this)
                            .text("Lengkapi Data Terlebih Dahulu")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#c62828"))
                            .show();
                }else{
                    db = alarm.getWritableDatabase();
                    cursor = db.rawQuery("SELECT * FROM budget WHERE id_budget = 1",null);
                    cursor.moveToFirst();
                    if (cursor.getCount() > 0){
                        if (Integer.parseInt(pengeluaran.getText().toString()) > Integer.parseInt(cursor.getString(1).toString())){
                            new StyleableToast
                                    .Builder(AddAlarm.this)
                                    .text("Budget Anda Kurang")
                                    .textColor(Color.WHITE)
                                    .backgroundColor(Color.parseColor("#c62828"))
                                    .show();
                        }else{
                            saveAlarm();
                        }
                    }
                }
            }
        });

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddAlarm.this, DashboardActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddAlarm.this, DashboardActivity.class));
        finish();
    }

    private void saveAlarm() {
        try {
            values.put("deskripsi", deskripsi.getText().toString());
            values.put("jumlah_pengeluaran", pengeluaran.getText().toString());
            values.put(AlarmReminderContract.AlarmReminderEntry.KEY_DATE, tanggal.getText().toString());
            values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TIME, waktu.getText().toString());
            values.put("ulang", ulang.getText().toString());
            values.put("type", mRepeatType);
            values.put("status", "pending");

            // Set up calender for creating the notification
            mCalendar.set(Calendar.YEAR, mYear);
            mCalendar.set(Calendar.MONTH, --mMonth);
            mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
            mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
            mCalendar.set(Calendar.MINUTE, mMinute);
            mCalendar.set(Calendar.SECOND, 0);


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

            long selectedTimestamp =  mCalendar.getTimeInMillis();
            Uri newUri = getContentResolver().insert(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, values);
            new StyleableToast
                    .Builder(AddAlarm.this)
                    .text("Sukses di Selesaikan")
                    .textColor(Color.WHITE)
                    .backgroundColor(Color.parseColor("#01579b"))
                    .show();

            new AlarmScheduler().setRepeatAlarm(getApplicationContext(), selectedTimestamp, newUri, mRepeatTime);
            startActivity(new Intent(AddAlarm.this, DashboardActivity.class));
            finish();

        }catch (SQLiteException e){
            e.getMessage();
        }
    }

}
