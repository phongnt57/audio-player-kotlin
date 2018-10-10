package  com.pntstudio.buzz.tedaudio.retrofit
import com.google.gson.JsonElement
import com.pntstudio.buzz.tedaudio.model.MediaItemData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url
import okhttp3.ResponseBody
import retrofit2.http.Streaming


interface Api {

    @GET("TEDTalks_audio")
    fun listArticle(): Call<String>

    @Streaming
    @GET
    fun downloadFile(@Url url: String): Call<ResponseBody>


    companion object {

        val BASE_URL = "http://feeds.feedburner.com/"
    }
}