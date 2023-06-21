package com.example.mytodoapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class AddTask extends AppCompatActivity {
    EditText editText;
    EditText day;
    EditText month;
    EditText hour;
    EditText minute;
    Button btEnter;
    CheckBox checkBox;
    boolean checked=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        editText=findViewById(R.id.edit_text);
        day=findViewById(R.id.editTextDay);
        month=findViewById(R.id.editTextMonth);
        hour=findViewById(R.id.editTextHour);
        minute=findViewById(R.id.editTextMinute);
        btEnter=findViewById(R.id.btn_enter);
        checkBox=findViewById(R.id.checkBox);

        day.setEnabled(false);
        month.setEnabled(false);
        hour.setEnabled(false);
        minute.setEnabled(false);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    day.setEnabled(true);
                    month.setEnabled(true);
                    hour.setEnabled(true);
                    minute.setEnabled(true);
                    checked=true;
                }
                else {
                    day.setEnabled(false);
                    month.setEnabled(false);
                    hour.setEnabled(false);
                    minute.setEnabled(false);
                    checked=false;
                }
            }
        });
    }
    public void StartNewActivity(View v){
        Intent intent = new Intent(this, MainActivity.class);
        String TaskName = ((EditText)findViewById(R.id.edit_text)).getText().toString();
        if(checked){
            int day = Integer.parseInt(((EditText)findViewById(R.id.editTextDay)).getText().toString());
            int month = Integer.parseInt(((EditText)findViewById(R.id.editTextMonth)).getText().toString());
            int hour = Integer.parseInt(((EditText)findViewById(R.id.editTextHour)).getText().toString());
            int minute = Integer.parseInt(((EditText)findViewById(R.id.editTextMinute)).getText().toString());
            intent.putExtra("day", day);
            intent.putExtra("month", month);
            intent.putExtra("hour", hour);
            intent.putExtra("minute", minute);
        }
        intent.putExtra("task_name", TaskName);
        intent.putExtra("haveDate", checked);
        startActivity(intent);
    }

}
