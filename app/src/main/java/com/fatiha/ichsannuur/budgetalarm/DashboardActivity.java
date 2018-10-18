package com.fatiha.ichsannuur.budgetalarm;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fatiha.ichsannuur.budgetalarm.Connection.DB_Helper;
import com.fatiha.ichsannuur.budgetalarm.Templates.AlarmCancelList;
import com.fatiha.ichsannuur.budgetalarm.Templates.AlarmList;
import com.fatiha.ichsannuur.budgetalarm.Templates.AlarmSuccessList;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class DashboardActivity extends AppCompatActivity {

    private TextView budget;
    private ImageView imageBudget;
    private EditText textBudget;
    private Button simpan_budget;
    private DB_Helper database;
    private Cursor cursor;
    public BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_pending:
                    fragment = new AlarmList();
                    break;
                case R.id.navigation_sucess:
                    fragment = new AlarmSuccessList();
                    break;
                case R.id.navigation_cancel:
                    fragment = new AlarmCancelList();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content,fragment).addToBackStack("tag");
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        imageBudget = (ImageView)findViewById(R.id.bar_budget);
        budget = (TextView)findViewById(R.id.budget);

        database = new DB_Helper(this);


        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_budget);
        textBudget = (EditText)dialog.findViewById(R.id.textBudget);
        simpan_budget = (Button) dialog.findViewById(R.id.simpan_budget);

        showBudget();

        imageBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        simpan_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int prev_budget = Integer.parseInt(budget.getText().toString().replace("Rp. ",""));
                    int new_budget = Integer.parseInt(textBudget.getText().toString().trim());
                    int sum_budget = prev_budget + new_budget;
                    SQLiteDatabase query = database.getWritableDatabase();
                    query.execSQL("UPDATE budget SET budget = "+ sum_budget +" WHERE id_budget = 1");
                    dialog.dismiss();
                    showBudget();
                    textBudget.setText("");
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        });

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        //First Tab show in fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content,new AlarmList());
        transaction.commit();
    }

    private void showBudget() {
        SQLiteDatabase query = database.getWritableDatabase();
        cursor = query.rawQuery("SELECT * FROM budget WHERE id_budget = 1",null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0){
            budget.setText("Rp. " + cursor.getString(1).toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            Intent i = new Intent(getApplicationContext(), AddAlarm.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
