package com.example.testalarmclock;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Random;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    TimePicker timePicker;
    Button btnAddAlarm;
    TextView txtAlarmSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timePicker = findViewById(R.id.timePicker);
        btnAddAlarm = findViewById(R.id.btn_add_alarm);
        txtAlarmSet = findViewById(R.id.txt_alarm_set);

        addOnClickListenerOnBtnSaveAlarm();

    }

    public void addOnClickListenerOnBtnSaveAlarm(){
        btnAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleAlarm();
            }
        });
    }

    private void scheduleAlarm() {
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);

        // todo create an alarm object
//        Alarm alarm = new Alarm(
//                alarmId,
//                TimePickerUtil.getTimePickerHour(timePicker),
//                TimePickerUtil.getTimePickerMinute(timePicker),
//                title.getText().toString(),
//                true,
//        );

        //todo save object to db
//        createAlarmViewModel.insert(alarm);

//        alarm.schedule(getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            schedule(getApplicationContext(),alarmId);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void schedule(Context context, int alarmId) {
        int hour, minute;

        hour = TimePickerUtil.getTimePickerHour(timePicker);
        minute = TimePickerUtil.getTimePickerMinute(timePicker);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

//        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        Intent intent = new Intent(context, BroadcastReceiver.class);
        intent.putExtra("title", "test alarm");

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // if alarm time has already passed, increment day by 1
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    alarmPendingIntent
            );
//        }
    }
}