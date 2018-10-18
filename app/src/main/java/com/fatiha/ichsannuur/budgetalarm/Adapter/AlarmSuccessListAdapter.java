package com.fatiha.ichsannuur.budgetalarm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fatiha.ichsannuur.budgetalarm.DashboardActivity;
import com.fatiha.ichsannuur.budgetalarm.Object.Alarm;
import com.fatiha.ichsannuur.budgetalarm.R;
import com.fatiha.ichsannuur.budgetalarm.Templates.DetailAlarm;

import java.util.List;

public class AlarmSuccessListAdapter extends RecyclerView.Adapter<AlarmSuccessListAdapter.ViewHolder> {

    Context context;
    List<Alarm> alarmList;
    Intent i;

    public AlarmSuccessListAdapter(Context context, List<Alarm> alarmList) {
        this.context = context;
        this.alarmList = alarmList;
    }

    @Override
    public AlarmSuccessListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_success_list_alarm,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AlarmSuccessListAdapter.ViewHolder viewHolder, int i) {
        Alarm alarm = alarmList.get(i);
        viewHolder.id_pengeluaran.setText(alarm.getId_pengeluaran() + "");
        viewHolder.item_tanggal.setText(alarm.getTanggal());
        viewHolder.item_waktu.setText(alarm.getWaktu());
        viewHolder.item_pengeluaran.setText("Rp. " + alarm.getJumlah_pengeluaran() + "");
        viewHolder.item_deskripsi.setText(alarm.getDeskripsi());
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id_pengeluaran;
        TextView item_tanggal;
        TextView item_pengeluaran;
        TextView item_waktu;
        TextView item_deskripsi;
        public ViewHolder(final View itemView) {
            super(itemView);
            id_pengeluaran = (TextView)itemView.findViewById(R.id.id_pengeluaran);
            item_tanggal = (TextView)itemView.findViewById(R.id.item_tanggal);
            item_waktu = (TextView)itemView.findViewById(R.id.item_waktu);
            item_pengeluaran = (TextView)itemView.findViewById(R.id.item_pengeluaran);
            item_deskripsi = (TextView)itemView.findViewById(R.id.item_deskripsi);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i = new Intent(context, DetailAlarm.class);
                    i.putExtra("id_pengeluaran", id_pengeluaran.getText().toString());
                    context.startActivity(i);
                    ((DashboardActivity)context).finish();
                }
            });
        }

    }
}
