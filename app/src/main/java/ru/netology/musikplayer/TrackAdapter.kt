package ru.netology.musikplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter (private var tracks: List<Track>) :
    RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

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
        private val compositionNameTextView: TextView =
            itemView.findViewById(R.id.name)

        fun bind(track: Track) {
            compositionNameTextView.text = track.id
        }
    }
}