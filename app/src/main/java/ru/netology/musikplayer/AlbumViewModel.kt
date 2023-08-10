package ru.netology.musikplayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.*
import okio.IOException
import org.json.JSONException
import org.json.JSONObject

class AlbumViewModel : ViewModel() {
    val tracksLD = MutableLiveData<List<Track>>()


    init {
        loadAlbumData()
    }
    private fun loadAlbumData() {
        val url =
            "https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/album.json"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
//                val errorMessage = "Ошибка загрузки данных: ${e.message}"

            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    try {
                        val jsonObject = JSONObject(responseBody)
                        val jsonArray = jsonObject.getJSONArray("tracks")
//                        val album = Album()
                        val tracks = mutableListOf<Track>()

                        for (i in 0 until jsonArray.length()) {
                            val compositionObject = jsonArray.getJSONObject(i)
                            val name = compositionObject.getString("id")
                            val file = compositionObject.getString("file")
                            tracks.add(Track(name, file))
                        }
                        tracksLD.postValue(tracks)
                    } catch (e: JSONException) {

                    }
                } else {

                }
            }
        })
    }
  }