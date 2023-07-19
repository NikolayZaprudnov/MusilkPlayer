package ru.netology.musikplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var albumViewModel: AlbumViewModel
    private lateinit var trackAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.komposition_view)

        albumViewModel = ViewModelProvider(this).get(AlbumViewModel::class.java)

        val recyclerView: RecyclerView = findViewById(R.id.list)

        trackAdapter = TrackAdapter(emptyList())
        recyclerView.adapter = trackAdapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        albumViewModel.tracksLD.observe(this) { tracks ->
            trackAdapter.updateData(tracks)
        }
    }
}
