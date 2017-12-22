package com.example.alarmshake

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK

/**
 * Created by LNTCS on 2017-12-22.
 */

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        //  해당 브로드캐스트가 울리면 알람결과창 (뭐 벨울리고 그런 곳) 열어준다
        var i = Intent(context, AlarmResultActivity::class.java)
        //  액티비티가 아닌곳에서 액티비티를 열 때는 FLAG_ACTIVITY_NEW_TASK 플래그를 추가해준다.
        i.flags += FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(i)
    }
}
