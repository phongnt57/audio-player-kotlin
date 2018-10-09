package com.pntstudio.buzz.tedaudio.viewmodel

/**
 * Created by admin on 10/3/18.
 */
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.LiveData
import android.util.Log
import com.pntstudio.buzz.tedaudio.model.MediaItemData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import com.pntstudio.buzz.tedaudio.retrofit.Api
import com.pntstudio.buzz.tedaudio.rssparse.RssReader
import retrofit2.converter.scalars.ScalarsConverterFactory


class MediaListFragmentViewModel : ViewModel() {

    //this is the data that we will fetch asynchronously
    private var heroList: MutableLiveData<ArrayList<MediaItemData>> = MutableLiveData()


    //we will call this method to get the data
    //if the list is null
    //we will load it asynchronously from server in this method
    //finally we will return the list
    val heroes: MutableLiveData<ArrayList<MediaItemData>>
        get() {
                heroList = MutableLiveData<ArrayList<MediaItemData>>()
                loadHeroes()
            return heroList
        }


    fun getMediaList(): LiveData<ArrayList<MediaItemData>> {
        return heroList
    }


    //This method is using Retrofit to get the JSON data from URL
    private fun loadHeroes() {
        val gson = GsonBuilder()
                .setLenient()
                .create()
        val retrofit = Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        val api = retrofit.create(Api::class.java)
        val call = api.listArticle();


        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
               val reader = RssReader(response.body()!!);

                val list = reader.items!!
                Log.e("response body value","".plus(list.size));

                //finally we are setting the list to our MutableLiveData
                heroList.setValue(list)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

                Log.e("response body error",t.message);

            }
        })
    }
}

