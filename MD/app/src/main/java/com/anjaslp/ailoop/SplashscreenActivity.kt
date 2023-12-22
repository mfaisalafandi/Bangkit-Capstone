package com.anjaslp.ailoop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SplashscreenActivity : AppCompatActivity() {

    private val animationDuration = 1500
    private val nilaiAlpha = 1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        supportActionBar?.hide()

        val ivSplash = findViewById<ImageView>(R.id.iv_splash)
        ivSplash.alpha = 0f
        ivSplash.animate()
            .setDuration(animationDuration.toLong())
            .alpha(nilaiAlpha)
            .withEndAction{
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
    }
}