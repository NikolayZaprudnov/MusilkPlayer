package ru.netology.musikplayer

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {
    var tracks = emptyList<Track>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.one_composition, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun updateData(tracks: List<Track>) {
        this.tracks = tracks
        notifyDataSetChanged()
    }

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mediaObserver = MediaLifecycleObserver()
        var uri = "https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/"
        private val compositionNameTextView: TextView =
            itemView.findViewById(R.id.name)
        private val playButton: ImageButton =
            itemView.findViewById(R.id.play)
        private val pauseButton: ImageButton =
            itemView.findViewById(R.id.pause)
        private val nextButton: ImageButton =
            itemView.findViewById(R.id.next)
        private val previousButton: ImageButton =
            itemView.findViewById(R.id.previous)

        fun bind(track: Track) {
            val trackUri = (uri + track.id + ".mp3")
            compositionNameTextView.text = track.id
            playButton.setOnClickListener {
                playButton.visibility = View.GONE
                pauseButton.visibility = View.VISIBLE
                mediaObserver.apply {
                    player?.setDataSource(
                        trackUri
                    )
                }.play()
//                MediaPlayer.create(itemView.context, trackUri).apply {
//                    setOnCompletionListener {
//                        it.release()
//                    }
//                }.start()
            }
            pauseButton.setOnClickListener {
                playButton.visibility = View.VISIBLE
                pauseButton.visibility = View.GONE
//                MediaPlayer.create(itemView.context, trackUri).apply {
//                    setOnCompletionListener {
//                        it.release()
//                    }
//                }.pause()
                mediaObserver.pause()
            }
            nextButton.setOnClickListener {
                val nextTrackUri = (uri + ((track.id.toInt() + 1).toString()) + ".mp3")
                mediaObserver.stop()
                mediaObserver.apply {
                    player?.setDataSource(
                        nextTrackUri
                    )
                }.play()
//                MediaPlayer.create(itemView.context, trackUri).apply {
//                    setOnCompletionListener {
//                        it.release()
//                    }
//                }.stop()
//                val nextTrackUri = (uri + ((track.id.toInt()+1).toString()) + ".mp3").toUri()
//                MediaPlayer.create(itemView.context, nextTrackUri).apply {
//                    setOnCompletionListener {
//                        it.release()
//                    }
//                }.start()
            }
            previousButton.setOnClickListener {
                val previousTrackUri = (uri + ((track.id.toInt() - 1).toString()) + ".mp3")
                mediaObserver.stop()
                mediaObserver.apply {
                    player?.setDataSource(
                        previousTrackUri
                    )
                }.play()
//                MediaPlayer.create(itemView.context, trackUri).apply {
//                    setOnCompletionListener {
//                        it.release()
//                    }
//                }.stop()
//                val previousTrackUri = (uri + ((track.id.toInt()-1).toString()) + ".mp3").toUri()
//                MediaPlayer.create(itemView.context, previousTrackUri).apply {
//                    setOnCompletionListener {
//                        it.release()
//                    }
//                }.start()
            }

        }


    }

}

class MediaLifecycleObserver : LifecycleObserver {
    var player: MediaPlayer? = MediaPlayer()
    fun play() {
        player?.setOnPreparedListener {
            it.start()
        }
        player?.prepareAsync()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pause() {
        player?.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stop() {
        player?.release()
        player = null
    }


}