package com.fatiha.ichsannuur.budgetalarm.Adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fatiha.ichsannuur.budgetalarm.Connection.AlarmReminderContract;
import com.fatiha.ichsannuur.budgetalarm.Connection.DB_Helper;
import com.fatiha.ichsannuur.budgetalarm.Object.Alarm;
import com.fatiha.ichsannuur.budgetalarm.R;
import com.fatiha.ichsannuur.budgetalarm.Templates.UpdatePendingAlarm;

import java.util.Calendar;
import java.util.List;

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder> {

    Context context;
    Cursor cursor;
    DB_Helper db_helper;
    SQLiteDatabase query;
    List<Alarm> alarmList;

    public AlarmListAdapter(Context context, List<Alarm> alarmList) {
        this.context = context;
        this.alarmList = alarmList;
    }

    @Override
    public AlarmListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_alarm,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AlarmListAdapter.ViewHolder viewHolder, int i) {
        Alarm alarm = alarmList.get(i);
        viewHolder.id_pengeluaran.setText(alarm.getId_pengeluaran() + "");
        viewHolder.item_tanggal.setText(alarm.getTanggal());
        viewHolder.item_waktu.setText(alarm.getWaktu());
        viewHolder.item_pengeluaran.setText("Rp. " + alarm.getJumlah_pengeluaran() + "");
        String get_hour = alarm.getWaktu().substring(0,2);
        if (Integer.parseInt(get_hour) >= 06 && Integer.parseInt(get_hour) <= 18){
            viewHolder.background_image.setImageResource(R.drawable.day);
        }else{
            viewHolder.background_image.setImageResource(R.drawable.night);
        }
        viewHolder.item_deskripsi.setText(alarm.getDeskripsi());
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView background_image;
        TextView item_tanggal;
        TextView item_waktu;
        TextView item_pengeluaran;
        TextView item_deskripsi;
        TextView id_pengeluaran;
        Intent i;
        public ViewHolder(View itemView) {
            super(itemView);
            background_image = (ImageView) itemView.findViewById(R.id.backgroud_image);
            id_pengeluaran = (TextView)itemView.findViewById(R.id.id_pengeluaran);
            item_tanggal = (TextView)itemView.findViewById(R.id.item_tanggal);
            item_waktu = (TextView)itemView.findViewById(R.id.item_waktu);
            item_pengeluaran = (TextView)itemView.findViewById(R.id.item_pengeluaran);
            item_deskripsi = (TextView)itemView.findViewById(R.id.item_deskripsi);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // GET id
//                    db_helper = new DB_Helper(context);
//                    query = db_helper.getWritableDatabase();
//                    cursor = query.rawQuery("SELECT COUNT(*) FROM alarm",null);
//                    cursor.moveToFirst();

                    i = new Intent(context, UpdatePendingAlarm.class);
                    i.putExtra("id_pengeluaran", id_pengeluaran.getText().toString());
                    Uri updateUri = Uri.parse("content://com.fatiha.ichsannuur.budgetalarm/reminder-path/"+ Integer.parseInt(id_pengeluaran.getText().toString()));
                    //                    Uri updateUri = ContentUris.withAppendedId(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, getAdapterPosition() + 1);
                    i.setData(updateUri);
                    context.startActivity(i);
                }
            });
        }
    }
}
