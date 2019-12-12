package com.example.eighthapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


//class MainActivity : AppCompatActivity() {
class MainActivity : AppCompatActivity(), TimeAlertDialog.Listener {

    override fun cancelAlarm() {
        cancelAlarmManager()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent?.getBooleanExtra("onReceive", false) == true) {
            val dialog = TimeAlertDialog()
            dialog.show(supportFragmentManager, "alert_dialog")
        }

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val date = c.get(Calendar.DATE)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        date_begin.text = DateFormat.format("yyyy/MM/dd", c)
        time_begin.text = "%1$02d:%2$02d".format(9, 30)
        time_finish.text = "%1$02d:%2$02d".format(hour, minute)

        c.timeInMillis = System.currentTimeMillis()
        // 1830
        c.set(year, month, date, hour, minute, 0)
        setAlarmManager(c)
    }

    private fun setAlarmManager(scheduledTime: Calendar) {
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, BroadcastReceiver::class.java)
        val pending = PendingIntent.getBroadcast(this, 0, intent, 0)
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                val info = AlarmManager.AlarmClockInfo(scheduledTime.timeInMillis, null)
                am.setAlarmClock(info, pending)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                am.setExact(AlarmManager.RTC_WAKEUP, scheduledTime.timeInMillis, pending)
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
