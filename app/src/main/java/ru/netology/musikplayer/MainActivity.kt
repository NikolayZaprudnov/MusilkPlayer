package ru.netology.musikplayer
//import android.media.MediaPlayer
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import okhttp3.*
//import ru.netology.musikplayer.databinding.ActivityMainBinding
//import java.io.IOException
//
//class MainActivity : AppCompatActivity() {
//    private lateinit var compositions: List<Track>
//    private lateinit var compositionAdapter: CompositionAdapter
//    private lateinit var mediaPlayer: MediaPlayer
//    private var currentCompositionIndex = 0
//    private var isPlaying = false
//
//    private lateinit var binding: ActivityMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)
//
//        compositionAdapter = CompositionAdapter()
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//        binding.recyclerView.adapter = compositionAdapter
//
//        mediaPlayer = MediaPlayer()
//
//        loadData()
//
//        binding.playButton.setOnClickListener {
//            togglePlayback()
//        }
//
//        binding.nextButton.setOnClickListener {
//            playNextComposition()
//        }
//
//        binding.prevButton.setOnClickListener {
//            playPreviousComposition()
//        }
//
//        binding.pauseButton.setOnClickListener {
//            pausePlayback()
//        }
//
//        binding.stopButton.setOnClickListener {
//            stopPlayback()
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mediaPlayer.release()
//    }
//
//    private fun loadData() {
//        val url = "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/album.json"
//
//        val client = OkHttpClient()
//        val request = Request.Builder()
//            .url(url)
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                runOnUiThread {
//                    showErrorToast("Ошибка загрузки данных: ${e.message}")
//                }
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                val responseBody = response.body?.string()
//
//                if (response.isSuccessful && responseBody != null) {
//                    parseAlbumData(responseBody)
//                    runOnUiThread {
//                        compositionAdapter.notifyDataSetChanged()
//                    }
//                } else {
//                    runOnUiThread {
//                        showErrorToast("Ошибка загрузки данных: ${response.code}")
//                    }
//                }
//            }
//        })
//    }
//
//    private fun parseAlbumData(jsonData: String) {
//        try {
//            val gson = Gson()
//            val albumType = object : TypeToken<Album>() {}.type
//            val album = gson.fromJson<Album>(jsonData, albumType)
//
//            compositions = album.compositions
//            compositionAdapter.setCompositions(compositions)
//
//            runOnUiThread {
//                binding.titleTextView.text = album.album
//                binding.artistTextView.text = album.artist
//            }
//        } catch (e: Exception) {
//            runOnUiThread {
//                showErrorToast("Ошибка разбора данных: ${e.message}")
//            }
//        }
//    }
//
//    private fun togglePlayback() {
//        if (isPlaying) {
//            pausePlayback()
//        } else {
//            playNextComposition()
//        }
//    }
//
//    private fun playNextComposition() {
//        if (compositions.isNotEmpty()) {
//            val composition = compositions[currentCompositionIndex]
//            playComposition(composition)
//
//            currentCompositionIndex = (currentCompositionIndex + 1) % compositions.size
//            isPlaying = true
//            binding.playButton.setImageResource(R.drawable.ic_pause)
//        }
//    }
//
//    private fun playPreviousComposition() {
//        if (compositions.isNotEmpty()) {
//            currentCompositionIndex = (currentCompositionIndex - 1 + compositions.size) % compositions.size
//            val composition = compositions[currentCompositionIndex]
//            playComposition(composition)
//            isPlaying = true
//            binding.playButton.setImageResource(R.drawable.ic_pause)
//        }
//    }
//
//    private fun pausePlayback() {
//        mediaPlayer.pause()
//        isPlaying = false
//        binding.playButton.setImageResource(R.drawable.ic_play)
//    }
//
//    private fun stopPlayback() {
//        mediaPlayer.stop()
//        mediaPlayer.prepare()
//        isPlaying = false
//        binding.playButton.setImageResource(R.drawable.ic_play)
//    }
//
//    private fun playComposition(composition: Composition) {
//        mediaPlayer.reset()
//        try {
//            val assetFileDescriptor = assets.openFd(composition.file)
//            mediaPlayer.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)
//            mediaPlayer.prepare()
//            mediaPlayer.start()
//            mediaPlayer.setOnCompletionListener {
//                playNextComposition()
//            }
//        } catch (e: IOException) {
//            showErrorToast("Ошибка воспроизведения композиции: ${e.message}")
//        }
//    }
//
//    private fun showErrorToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
//
//    private inner class CompositionAdapter : RecyclerView.Adapter<CompositionViewHolder>() {
//        private var compositionsList = mutableListOf<Track>()
//
//        fun setCompositions(compositions: List<Track>) {
//            compositionsList.clear()
//            compositionsList.addAll(compositions)
//            notifyDataSetChanged()
//        }
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompositionViewHolder {
//            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.one_composition, parent, false)
//            return CompositionViewHolder(itemView)
//        }
//
//        override fun onBindViewHolder(holder: CompositionViewHolder, position: Int) {
//            val composition = compositionsList[position]
//            holder.bind(composition)
//        }
//
//        override fun getItemCount(): Int {
//            return compositionsList.size
//        }
//    }
//
//    private inner class CompositionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(composition: Track) {
//            itemView.compositionNameTextView.text = composition.name
//
//            itemView.setOnClickListener {
//                if (!isPlaying) {
//                    playComposition(composition)
//                    currentCompositionIndex = adapterPosition
//                    isPlaying = true
//                    binding.playButton.setImageResource(R.drawable.ic_pause)
//                }
//            }
//        }
//    }
//}

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.netology.musikplayer.databinding.ActivityMainBinding
import ru.netology.musikplayer.databinding.OneCompositionBinding

class MainActivity : AppCompatActivity() {
    private lateinit var albumViewModel: AlbumViewModel
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        albumViewModel = ViewModelProvider(this).get(AlbumViewModel::class.java)

        val recyclerView: RecyclerView = findViewById(R.id.list)

        trackAdapter = TrackAdapter()
        recyclerView.adapter = trackAdapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        albumViewModel.tracksLD.observe(this) { tracks ->
            trackAdapter.updateData(tracks)
        }
        var uri = "https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/1.mp3".toUri()
        binding.album
            .setOnClickListener{
            MediaPlayer.create(this, uri  ).apply {
                setOnCompletionListener {
                    it.release()
                }
            }.start()
        }

    }
}
