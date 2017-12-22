package com.example.alarmshake

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.alarmManager
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  Calendar.getInstance()  = 현재 시간
        var time = Calendar.getInstance()
        time.apply {
            //  현재시간 + 5초
            set(Calendar.SECOND, time[Calendar.SECOND] + 5)
        }

        //  Manifest에 등록한 브로드 캐스트 (com.example.alarmshake.ALARM_START) 를 열 이벤트
        var intent = Intent("com.example.alarmshake.ALARM_START")
        var pIntent = PendingIntent.getBroadcast(this, 123,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // 버전별로 알림을 설정하는 방법은 차이가 있어 버전 대응을 해준다
        // Build.VERSION.SDK_INT => 현재 폰의 버전 받아오기
        when {
            Build.VERSION.SDK_INT >= 23 -> alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.timeInMillis, pIntent)
            Build.VERSION.SDK_INT >= 19 -> alarmManager.setExact(AlarmManager.RTC_WAKEUP, time.timeInMillis, pIntent)
            else -> alarmManager.set(AlarmManager.RTC_WAKEUP, time.timeInMillis, pIntent)
        }
    }
}
