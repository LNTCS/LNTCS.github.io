package com.example.alarmshake

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.sensorManager
import org.jetbrains.anko.toast
import kotlin.math.pow

class AlarmResultActivity : AppCompatActivity(), SensorEventListener {
    var sensor: Sensor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_result)

        //  TYPE_ACCELEROMETER 가속도 센서를 가져온다.
        var sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        //  센서의 리스너를 등록
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    //  알림이 꺼질 흔듦의 강도
    val SHAKE_THRESHOLD = 2.7f

    override fun onSensorChanged(event: SensorEvent?) {
        //  센서의 종류가 내가 사용하는 가속도 센서인지 확인
        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
            var axisX = event.values[0]
            var axisY = event.values[1]
            var axisZ = event.values[2]

            var gX = axisX / SensorManager.GRAVITY_EARTH
            var gY = axisY / SensorManager.GRAVITY_EARTH
            var gZ = axisZ / SensorManager.GRAVITY_EARTH

            var shake = gX.pow(2) + gY.pow(2) + gZ.pow(2)

            //  흔들린 정도를 가져오는 식이다. 이해하려 하지 말자.
            var force = Math.sqrt(shake.toDouble()).toFloat()

            //  흔들은 강도가 내가 정한 수치 이상일 경우 알람 종료
            if(force > SHAKE_THRESHOLD){
                toast("알람 종료")
                finish()
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}
