import com.google.gson.JsonElement
import com.pntstudio.buzz.tedaudio.model.MediaItemData
import retrofit2.Call
import retrofit2.http.GET

interface Api {

    @GET("TEDTalks_audio")
    fun listArticle(): Call<String>



    companion object {

        val BASE_URL = "http://feeds.feedburner.com/"
    }
}