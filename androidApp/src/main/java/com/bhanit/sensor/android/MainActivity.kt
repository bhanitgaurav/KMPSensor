package com.bhanit.sensor.android

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import com.bhanit.sensor.Greeting


class MainActivity : ComponentActivity() {
    private lateinit var gyroscope: Gyroscope
    private lateinit var accelerometer: Accelerometer
    private val proximitySensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(sensorEvent: SensorEvent) {
            Log.d(TAG, "onSensorChanged: Sensor Name: + ${sensorEvent.sensor.name}")
            Log.d(TAG, "onSensorChanged: Accuracy: + ${sensorEvent.accuracy.toString()}")
            Log.d(TAG, "onSensorChanged: Timestamp: + ${sensorEvent.timestamp.toString()}")
            Log.d(TAG, "onSensorChanged: Distance: + ${sensorEvent.values[0].toString()}")
            sensorName.value = ("Sensor Name: " + sensorEvent.sensor.name)
            sensorAccuracy.value = ("Accuracy: " + sensorEvent.accuracy.toString())
            sensorTimestamp.value = ("Timestamp: " + sensorEvent.timestamp.toString())
            sensorDistance.value = ("Distance: " + sensorEvent.values[0].toString())
        }

        override fun onAccuracyChanged(sensor: Sensor, i: Int) {}

    }

    private lateinit var proximitySensor: Sensor
    private lateinit var sensorManager: SensorManager
    val sensorName: MutableLiveData<String> = MutableLiveData()
    private val sensorAccuracy: MutableLiveData<String> = MutableLiveData()
    private val sensorTimestamp: MutableLiveData<String> = MutableLiveData()
    private val sensorDistance: MutableLiveData<String> = MutableLiveData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GreetingView(Greeting().greet())
                }
            }
        }
        initSensor()
    }

    private fun initSensor() {
        accelerometer = Accelerometer(this)
        gyroscope = Gyroscope(this)
        setAccelerometerSensorListener()
        setGyroscopeSensorListener()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)!!
        setProximitySensorListener()
    }

    private fun setGyroscopeSensorListener() {
        // create a listener for gyroscope
        gyroscope.setListener(object : Gyroscope.Listener {
            // on rotation method of gyroscope
            override fun onRotation(rx: Float, ry: Float, rz: Float) {
                // set the color green if the device rotates on positive z axis
                Log.d(TAG, "onRotation: gyroscope rx $rx ry $ry rz $rz")
                if (rz > 1.0f) {
                    Log.d(TAG, "onRotation: gyroscope red")
                    window.decorView.setBackgroundColor(resources.getColor(R.color.red_300))
                } else if (rz < -1.0f) {
                    Log.d(TAG, "onRotation: gyroscope yellow")
                    window.decorView.setBackgroundColor(resources.getColor(R.color.yellow_300))
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        sensorManager.registerListener(proximitySensorListener, proximitySensor, 2 * 1000 * 1000);
        accelerometer.register()
        gyroscope.register()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(proximitySensorListener);
        accelerometer.unregister()
        gyroscope.unregister()
    }

    private fun setAccelerometerSensorListener() {
        accelerometer.setListener(object : Accelerometer.Listener {
            //on translation method of accelerometer
            override fun onTranslation(tx: Float, ty: Float, ts: Float) {
                // set the color red if the device moves in positive x axis
                Log.d(TAG, "accelerometer onTranslation: tx $tx ty $ty ts $ts")
                if (tx > 1.0f) {
                    Log.d(TAG, "accelerometer onTranslation: red")
                    window.decorView.setBackgroundColor(resources.getColor(R.color.red_300))
                } else if (tx < -1.0f) {
                    Log.d(TAG, "accelerometer onTranslation: blue")
                    window.decorView.setBackgroundColor(resources.getColor(R.color.blue_300))
                }
            }
        })
    }

    private fun setProximitySensorListener() {
        object : SensorEventListener {
            override fun onSensorChanged(sensorEvent: SensorEvent) {
                Log.d(TAG, "onSensorChanged: Sensor Name: + ${sensorEvent.sensor.name}")
                Log.d(TAG, "onSensorChanged: Accuracy: + ${sensorEvent.accuracy.toString()}")
                Log.d(TAG, "onSensorChanged: Timestamp: + ${sensorEvent.timestamp.toString()}")
                Log.d(TAG, "onSensorChanged: Distance: + ${sensorEvent.values[0].toString()}")
                sensorName.value = ("Sensor Name: " + sensorEvent.sensor.name)
                sensorAccuracy.value = ("Accuracy: " + sensorEvent.accuracy.toString())
                sensorTimestamp.value = ("Timestamp: " + sensorEvent.timestamp.toString())
                sensorDistance.value = ("Distance: " + sensorEvent.values[0].toString())
                changeScreenColor(sensorEvent)
            }

            override fun onAccuracyChanged(sensor: Sensor, i: Int) {
                Log.d(TAG, "onAccuracyChanged: ")
            }
        }
    }

    private fun changeScreenColor(sensorEvent: SensorEvent) {
        if (sensorEvent.values[0] < proximitySensor.maximumRange) {
            // Detected something nearby
            window.decorView.setBackgroundColor(resources.getColor(R.color.red_300));
        } else {
            // Nothing is nearby
            window.decorView
                .setBackgroundColor(resources.getColor(R.color.green_300));
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}


@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}


