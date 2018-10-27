package com.pntstudio.buzz.tedaudio

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.android.gms.ads.MobileAds

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        MobileAds.initialize(this, getString(R.string.app_id))

        val handler = Handler()
        handler.postDelayed({
            //Do something after 1000ms
            val intent = Intent(this@SplashActivity,MainActivity::class.java)
            startActivity(intent)
            finish()

        }, 800)



    }
}
