package com.pntstudio.buzz.tedaudio.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import com.pntstudio.buzz.tedaudio.R
import com.pntstudio.buzz.tedaudio.model.Download
import com.pntstudio.buzz.tedaudio.model.MediaItemData
import com.pntstudio.buzz.tedaudio.retrofit.Api
import retrofit2.Retrofit
import android.app.NotificationManager
import okhttp3.ResponseBody
import com.pntstudio.buzz.tedaudio.helps.ProgressResponseBody
import javax.xml.datatype.DatatypeConstants.SECONDS
import okhttp3.OkHttpClient
import com.pntstudio.buzz.tedaudio.helps.OnAttachmentDownloadListener
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Created by admin on 10/8/18.
 */
class DownloadService : IntentService("Dowload Service"),OnAttachmentDownloadListener{
    override fun onAttachmentDownloadedError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAttachmentDownloadedFinished() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAttachmentDownloadUpdate(percent: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAttachmentDownloadedSuccess() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var notificationManager: NotificationManager? = null
     var notificationBuilder: NotificationCompat.Builder? = null


    lateinit var api: Api
    override fun onCreate() {
        super.onCreate()



    }
    fun isOreoPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    override fun onHandleIntent(intent: Intent?) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        val download = intent!!.getSerializableExtra("download") as Download
        notificationManager =  getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;

        val channelId =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel()
                } else {
                    ""
                }

        notificationBuilder = NotificationCompat.Builder(this,channelId)
                .setSmallIcon(R.drawable.ic_download)
                .setContentTitle("Download")
                .setContentText(download.link)
                .setAutoCancel(true)
        notificationManager!!.notify(101, notificationBuilder!!.build())

        initDownload(download)


    }

    private fun sendNotification(download: Download) {
        notificationBuilder!!.setProgress(100, download.progess, false)
        notificationManager!!.notify(101, notificationBuilder!!.build())
    }

    private fun initDownload(download: Download) {
        val retrofit = Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(getOkHttpDownloadClientBuilder(this).build())

                .build()
        api = retrofit.create(Api::class.java)
        val request = api.downloadFile(download.link)



    }

    fun getOkHttpDownloadClientBuilder(progressListener: OnAttachmentDownloadListener?): OkHttpClient.Builder {
        val httpClientBuilder = OkHttpClient.Builder()

        // You might want to increase the timeout
        httpClientBuilder.connectTimeout(20, TimeUnit.SECONDS)
        httpClientBuilder.writeTimeout(0, TimeUnit.SECONDS)
        httpClientBuilder.readTimeout(5, TimeUnit.MINUTES)

        httpClientBuilder.addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                if (progressListener == null) return chain.proceed(chain.request())

                val originalResponse = chain.proceed(chain.request())
                return originalResponse.newBuilder()
                        .body(ProgressResponseBody(originalResponse.body()!!, progressListener))
                        .build()
            }
        })

        return httpClientBuilder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {
        val channelId = "download_service"
        val channelName = "My Background Service"
        val chan = NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        notificationManager!!.createNotificationChannel(chan)
        return channelId
    }

}