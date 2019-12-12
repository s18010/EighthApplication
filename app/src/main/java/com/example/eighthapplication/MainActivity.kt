package com.example.eighthapplication

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Toast
import androidx.core.content.edit
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), TimeAlertDialog.Listener,
    DatePickerFragment.OnDateSelectedListener,
    TimeBeginPickerFragment.OnTimeBeginSelectedListener,
    TimeFinishPickerFragment.OnTimeFinishSelectedListener {

    override fun onDateSelected(year: Int, month: Int, date: Int) {
        val c = Calendar.getInstance()
        c.set(year, month, date)
        date_begin.text = DateFormat.format("yyyy/MM/dd", c)
        val pref = getSharedPreferences("date_begin", Context.MODE_PRIVATE)
        pref.edit {
            putInt("year", year)
            putInt("month", month)
            putInt("date", date)
        }
    }

    override fun onTimeBeginSelected(hourOfDay: Int, minute: Int) {
        time_begin.text = "%1$02d:%2$02d".format(hourOfDay, minute)
        val pref = getSharedPreferences("time_begin", Context.MODE_PRIVATE)
        pref.edit {
            putInt("hourOfDay", hourOfDay)
            putInt("minute", minute)
        }
    }

    override fun onTimeFinishSelected(hourOfDay: Int, minute: Int) {
        time_finish.text = "%1$02d:%2$02d".format(hourOfDay, minute)
        val pref = getSharedPreferences("time_finish", Context.MODE_PRIVATE)
        pref.edit {
            putInt("hourOfDay", hourOfDay)
            putInt("minute", minute)
        }
    }

    override fun cancelAlarm() {
        cancelAlarmManager()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        date_begin.setOnClickListener {
            val dialog = DatePickerFragment()
            dialog.show(supportFragmentManager, "date_dialog")
        }

        time_begin.setOnClickListener {
            val dialog = TimeBeginPickerFragment()
            dialog.show(supportFragmentManager, "time_begin_dialog")
        }

        time_finish.setOnClickListener {
            val dialog = TimeFinishPickerFragment()
            dialog.show(supportFragmentManager, "time_finish_dialog")
        }

        saveButton.setOnClickListener {
            Toast.makeText(this, "共有プリファレンスにデータを保存しました。", Toast.LENGTH_SHORT).show()
        }

        if (intent?.getBooleanExtra("onReceive", false) == true) {
            val dialog = TimeAlertDialog()
            dialog.show(supportFragmentManager, "alert_dialog")
        }

        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        date_begin.text = DateFormat.format("yyyy/MM/dd", c)
        time_begin.text = "%1$02d:%2$02d".format(9, 30)
        time_finish.text = "%1$02d:%2$02d".format(hour, minute)

        c.apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 18)
            set(Calendar.MINUTE, 30)
        }
        setAlarmManager(c)
    }

    private fun setAlarmManager(scheduledTime: Calendar) {
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, BroadcastReceiver::class.java)
        val pending = PendingIntent.getBroadcast(this, 0, intent, 0)
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
//                am.setInexactRepeating(
                am.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    scheduledTime.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pending
                )
            }
            else -> {
                am.set(AlarmManager.RTC_WAKEUP, scheduledTime.timeInMillis, pending)
            }
        }
    }

    private fun cancelAlarmManager() {
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, BroadcastReceiver::class.java)
        val pending = PendingIntent.getBroadcast(this, 0, intent, 0)
        am.cancel(pending)
    }
}
