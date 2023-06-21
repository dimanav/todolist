package com.example.mytodoapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class MainActivity extends AppCompatActivity {
    Button btAdd;
    RecyclerView recyclerView;

    List<MainData> dataList=new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RoomDB database;

    MainAdapter mainAdapter;
    Calendar calendar = Calendar.getInstance();

    int task_id;
    boolean checked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btAdd=findViewById(R.id.bt_add);
        recyclerView=findViewById(R.id.recycler_view);

        database=RoomDB.getInstance(this);

        dataList=database.mainDao().getAll();

        linearLayoutManager =new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        mainAdapter=new MainAdapter(dataList,MainActivity.this);

        recyclerView.setAdapter(mainAdapter);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Bundle arguments = getIntent().getExtras();
        String TaskName;
        int day, month, hour, minute;
        if (arguments != null) {
            TaskName = arguments.get("task_name").toString();
            checked = arguments.getBoolean("haveDate");
            if (checked){
                day = arguments.getInt("day");
                month = arguments.getInt("month");
                hour = arguments.getInt("hour");
                minute = arguments.getInt("minute");

                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.MONTH, month - 1);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);

                getIntent().removeExtra("day");
                getIntent().removeExtra("month");
                getIntent().removeExtra("hour");
                getIntent().removeExtra("minute");

                AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, AlarmReceiver.class);
                intent.putExtra("task_name", TaskName);
                intent.putExtra("id", task_id);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, task_id, intent, 0);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }
            addTask(TaskName);
            getIntent().removeExtra("task_name");

        }

        mainAdapter.notifyDataSetChanged();

    };

    public void StartNewActivity(View v){
        Intent add_intent = new Intent(this, AddTask.class);
        startActivity(add_intent);
    }
    public void addTask(String TaskName){

        MainData data=new MainData();

        data.setText(TaskName);


        database.mainDao().insert(data);

        dataList.clear();
        Toast.makeText(MainActivity.this,"Задача добавлена!",Toast.LENGTH_LONG).show();
        dataList.addAll(database.mainDao().getAll());
        data = dataList.get(dataList.size()-1);
        task_id = data.getID();

    }



}