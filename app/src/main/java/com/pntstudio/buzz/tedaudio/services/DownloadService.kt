package com.pntstudio.buzz.tedaudio.services

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.content.IntentSender
import android.os.IBinder
import com.pntstudio.buzz.tedaudio.retrofit.Api
import com.tonyodev.fetch2.Download
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Created by admin on 10/8/18.
 */
class DownloadService : IntentService("Dowload Service"){
    override fun onHandleIntent(intent: Intent?) {
    }

    lateinit var api: Api
    override fun onCreate() {
        super.onCreate()
        val retrofit = Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .build()
        api = retrofit.create(Api::class.java)


    }
    override fun onBind(intent: Intent?): IBinder {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}