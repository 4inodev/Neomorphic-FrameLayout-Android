package com.example.androidnemorphframelayout

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidnemorphframelayout.databinding.ActivityMainBinding
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var isShapeRect = true

        binding.apply {

            neomorphFrameLayout.setOnClickListener {
                neomorphFrameLayout.switchShadowType()
            }

            changeShape.setOnClickListener {
                if (isShapeRect){
                    neomorphFrameLayout.setViewCircular()
                }else{
                    neomorphFrameLayout.setViewRectangular()
                }
                isShapeRect = !isShapeRect
            }

            changeBackground.setOnClickListener {
                neomorphFrameLayout.neomorphBackgroundColor(randomColor())
            }

            changeShadowColor.setOnClickListener {
                neomorphFrameLayout.neomorphShadowColor(randomColor())
            }

            changeHighlightColor.setOnClickListener {
                neomorphFrameLayout.neomorphHighlightColor(randomColor())
            }

        }

    }

    private fun randomColor(): Int {
        val rnd = Random.Default
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }
}
