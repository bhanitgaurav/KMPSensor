package com.bhanit.sensor.android

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager


class Gyroscope internal constructor(context: Context) {
    // create an interface with one method
    interface Listener {
        // create method with all 3
        // axis translation as argument
        fun onRotation(tx: Float, ty: Float, ts: Float)
    }

    // create an instance
    private var listener: Listener? = null

    // method to set the instance
    fun setListener(l: Listener?) {
        listener = l
    }

    private val sensorManager: SensorManager
    private val sensor: Sensor
    private val sensorEventListener: SensorEventListener

    // create constructor with context as argument
    init {

        // create instance of sensor manager
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // create instance of sensor with type gyroscope
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!!

        // create the sensor listener
        sensorEventListener = object : SensorEventListener {
            // this method is called when
            // the device's position changes
            override fun onSensorChanged(sensorEvent: SensorEvent) {
                // check if listener is different from null
                if (listener != null) {
                    // pass the three floats in listener on rotation of axis
                    listener!!.onRotation(
                        sensorEvent.values[0], sensorEvent.values[1],
                        sensorEvent.values[2]
                    )
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, i: Int) {}
        }
    }

    // create register method
    // for sensor notifications
    fun register() {
        // call sensor manager's register listener and pass the required arguments
        sensorManager.registerListener(
            sensorEventListener,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    // create method to unregister
    // from sensor notifications
    fun unregister() {
        // call sensor manager's unregister listener
        // and pass the required arguments
        sensorManager.unregisterListener(sensorEventListener)
    }
}