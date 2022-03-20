package com.example.shipandcat

import android.content.Context
import android.content.res.Resources
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.shipandcat.databinding.ActivityMainBinding


var myWidthImage: Int = 800
var myHeightImage: Int = 1400
var dpScale: Float = 1f

class MainActivity : AppCompatActivity() {
    private val sea by lazy {
        ImageView(this)
    }
    private val ship by lazy {
        ImageView(this)
    }

    lateinit var binding: ActivityMainBinding
    lateinit var sManager: SensorManager
    val fps: Long = 60
    var yAngle: Float = 0f

    var widthShip: Int = 0
    var widthSea: Int = 0
    var heightSea: Int = 1
    var heightShip: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar.hide()

        initSensor()
        getSize()
        initViewObjects()

        Thread (Runnable {
            while (true) {
                Thread.sleep(fps)
                runOnUiThread {
                    binding.idContainer.removeAllViews()
                    binding.idContainer.addView(sea)
                    binding.idContainer.add
                }
            }
        }).start()
    }

    private fun getSize() {
        myWidthImage = Resources.getSystem().displayMetrics.widthPixels
        myHeightImage = Resources.getSystem().displayMetrics.heightPixels
        dpScale = Resources.getSystem().displayMetrics.density

        widthSea = (1.5* myWidthImage).toInt()
        widthShip = (0.5* myWidthImage).toInt()

        val seaImageWidth = 974
        val seaImageHeight = 517
        val shipImageWidth = 749
        val shipImageHeight = 422

        heightSea = seaImageWidth/widthSea*seaImageHeight
        heightShip = shipImageWidth/widthShip*shipImageHeight
    }

    private fun initViewObjects() {
        sea.setImageResource(R.drawable.sea)
        //sea.animate().rotation(90F)
        sea.layoutParams = FrameLayout.LayoutParams(widthSea, heightSea)
        (sea.layoutParams as FrameLayout.LayoutParams).topMargin = (myHeightImage *0.5).toInt()
        (sea.layoutParams as FrameLayout.LayoutParams).leftMargin = ((widthSea - myWidthImage)*0.5).toInt()

        ship.setImageResource(R.drawable.ship)
        //ship.animate().rotation(90F)
        ship.layoutParams = FrameLayout.LayoutParams(widthShip, heightShip)
        (ship.layoutParams as FrameLayout.LayoutParams).topMargin = 0 //(myHeightImage *0.5).toInt()
        (ship.layoutParams as FrameLayout.LayoutParams).leftMargin = 0 // ((widthShip - myWidthImage)*0.5).toInt()

    }


    private fun initSensor() {
        val tvSensor = binding.tvSensor
        sManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val sListener = object: SensorEventListener {
            override fun onSensorChanged(p0: SensorEvent?) {
                val value = p0?.values
                val sData = "X: ${value?.get(0)}\nY: ${value?.get(1)}\nZ: ${value?.get(2)}"
                yAngle = value?.get(1).toString().toFloat()
                tvSensor.text = sData
            }
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            }
        }
        sManager.registerListener(sListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)

    }
}