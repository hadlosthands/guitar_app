package ru.msu.cs.al.guitarapp.presentation.list.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.msu.cs.al.guitarapp.R
import ru.msu.cs.al.guitarapp.models.SongItem
import ru.msu.cs.al.guitarapp.presentation.list.OnSongItemClicked
import com.example.chord_recognition_lib.ChordRecognizer

class SongAdapter(private val dataSet: List<SongItem>,
                  private val onClickAction: OnSongItemClicked
): RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val itemCard: View
        val imagePreview: ImageView
        val tvSong: TextView
        val tvGroup:TextView

        init {
            itemCard = view.findViewById(R.id.itemCard)
            imagePreview = view.findViewById(R.id.img_preview)
            tvSong = view.findViewById(R.id.tvSong)
            tvGroup = view.findViewById(R.id.tvGroup)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            dataSet[position].let {
                Glide.with(imagePreview.context).load(dataSet[position].songPictureUrl).into(imagePreview)
                tvSong.text = it.songName
                tvGroup.text=it.artistName
                val songItem = it
                itemCard.setOnClickListener {
                    onClickAction.onSongItemClicked(songItem)
                }
            }
        }
    }

    override fun getItemCount(): Int = dataSet.size
}