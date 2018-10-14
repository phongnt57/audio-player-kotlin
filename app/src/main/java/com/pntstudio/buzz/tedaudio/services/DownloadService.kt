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
import android.os.Environment
import android.util.Log
import com.pntstudio.buzz.tedaudio.helps.BusProvider
import okhttp3.ResponseBody
import com.pntstudio.buzz.tedaudio.helps.ProgressResponseBody
import javax.xml.datatype.DatatypeConstants.SECONDS
import okhttp3.OkHttpClient
import com.pntstudio.buzz.tedaudio.helps.OnAttachmentDownloadListener
import com.pntstudio.buzz.tedaudio.model.Events
import com.squareup.otto.Bus
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import java.io.*
import java.util.concurrent.TimeUnit


/**
 * Created by admin on 10/8/18.
 */
class DownloadService : IntentService("Dowload Service"),OnAttachmentDownloadListener{

    override fun onAttachmentDownloadedError() {

    }

    override fun onAttachmentDownloadedFinished() {
    }

    override fun onAttachmentDownloadUpdate(percent: Int) {
        Log.e(TAG,download.progess.toString())
        download.progess = percent
        sendNotification(download)
    }

    override fun onAttachmentDownloadedSuccess() {
    }

    private var notificationManager: NotificationManager? = null
    private var notificationBuilder: NotificationCompat.Builder? = null
    private  lateinit var download: Download
    private val outputFolder = "TED audio"
    private val outputRoot = File(Environment.getExternalStorageDirectory(), outputFolder)
    private val TAG = "Download service"
    private var mBus: Bus? = null




    lateinit var api: Api
    override fun onCreate() {
        super.onCreate()
        if (!outputRoot.exists() && !outputRoot.mkdirs()) {
            return
        }
        if (mBus == null) {
            mBus = BusProvider.instance
        }



    }
    fun isOreoPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    override fun onHandleIntent(intent: Intent?) {
        // Normally we would do some work here, like download a file.
         download = intent!!.getSerializableExtra("download") as Download
        notificationManager =  getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;

        val channelId =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel()
                } else {
                    ""
                }

        notificationBuilder = NotificationCompat.Builder(this,channelId)
                .setSmallIcon(R.drawable.ic_download)
                .setContentTitle("Downloading")
                .setContentText(download.title)
                .setAutoCancel(true)
        notificationManager!!.notify(101, notificationBuilder!!.build())

        initDownload(download)


    }

    private fun sendNotification(download: Download) {
        notificationBuilder!!.setProgress(100, download.progess, false)
        notificationManager!!.notify(101, notificationBuilder!!.build())
    }

    private fun onDownloadComplete() {
        Log.e(TAG,"download complete")
        download.progess = 100
        mBus!!.post(Events.DownloadSucess(download))

//        sendIntent(download)
//        notificationManager!!.cancel(101)
        notificationBuilder!!.setContentTitle("Download success")
        notificationBuilder!!.setProgress(0, 0, false)
        notificationManager!!.notify(101, notificationBuilder!!.build())

    }


    private fun initDownload(download: Download) {
        val retrofit = Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(getOkHttpDownloadClientBuilder(this).build())
                .build()
        api = retrofit.create(Api::class.java)
        val request = api.downloadFile(download.link)
        request.enqueue(object :Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                Log.e(TAG,t.toString())


            }

            override fun onResponse(call: Call<ResponseBody>?, response: retrofit2.Response<ResponseBody>?) {
                writeResponseBodyToDisk(response!!.body()!!)
            }

        })



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

    private fun writeResponseBodyToDisk(body: ResponseBody): Boolean {
        try {
            // todo change the file location/name according to your needs
            val futureStudioIconFile = File(outputRoot,download.title+".mp3")

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(4096)

                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0

                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)

                while (true) {
                    val read = inputStream!!.read(fileReader)

                    if (read == -1) {
                        break
                    }

                    outputStream.write(fileReader, 0, read)

                    fileSizeDownloaded += read.toLong()

                    Log.e(TAG, "file download: $fileSizeDownloaded of $fileSize")
                }

                outputStream.flush()
                onDownloadComplete()
                return true
            } catch (e: IOException) {
                return false
            } finally {
                if (inputStream != null) {
                    inputStream.close()
                }

                if (outputStream != null) {
                    outputStream.close()
                }
            }
        } catch (e: IOException) {
            return false
        }

    }

    override fun onTaskRemoved(rootIntent: Intent) {
        notificationManager!!.cancel(101)
    }

}