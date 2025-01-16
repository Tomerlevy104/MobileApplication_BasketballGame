package com.example.hw1_tomerlevy.utilities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.hw1_tomerlevy.interfaces.TiltCallback
import kotlin.math.abs

//This class detects device tilt using the accelerometer sensor
class TiltDetector(context: Context, private val tiltCallback: TiltCallback?) {

    //get the system's sensor service and accelerometer sensor
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) as Sensor
    private var timestamp: Long = 0L //timestamp to track when last tilt was detected
    private lateinit var sensorEventListener: SensorEventListener

    //initialize the event listener when class is created
    init {
        initEventListener()
    }

    //creates the sensor event listener
    private fun initEventListener() {
        sensorEventListener = object : SensorEventListener {
            // Called when sensor values change
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]  //get X axis acceleration
                val y = event.values[1]  //get Y axis acceleration
                calculateTilt(x,y)       //process the values
            }

            //called when sensor accuracy changes
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // pass
            }
        }
    }

    //process accelerometer values to detect tilts
    private fun calculateTilt(x: Float, y: Float) {
        if (System.currentTimeMillis() - timestamp >= 50) { //enough time passed since late check
            timestamp = System.currentTimeMillis()
            if (abs(x) >= 3.0) {
                tiltCallback?.tiltX(x)
            }

            if (abs(y) <= 3.0) {
                tiltCallback?.tiltY(y)
            }
        }
    }

    //start listening to sensor events
    fun start(){
        sensorManager.registerListener(
            sensorEventListener,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    //stop listening to sensor events
    fun stop(){
        sensorManager.unregisterListener(
            sensorEventListener,
            sensor
        )
    }
}