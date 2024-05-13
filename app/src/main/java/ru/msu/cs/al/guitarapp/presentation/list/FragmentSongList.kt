package ru.msu.cs.al.guitarapp.presentation.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.msu.cs.al.guitarapp.R
import ru.msu.cs.al.guitarapp.data.datastores.SongDatastore
import ru.msu.cs.al.guitarapp.data.repositories.SongRepository
import ru.msu.cs.al.guitarapp.data.repositories.SongRepositoryImpl
import ru.msu.cs.al.guitarapp.models.SongItem
import ru.msu.cs.al.guitarapp.presentation.details.FragmentSongDetails
import ru.msu.cs.al.guitarapp.presentation.list.adapters.SongAdapter

class FragmentSongList : Fragment(R.layout.fmt_song_list), OnSongItemClicked {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerViewSongs)
        val repository: SongRepository = SongRepositoryImpl(SongDatastore())

        recycler.adapter = SongAdapter(repository.getSong(), this)
    }

    override fun onSongItemClicked(songItem: SongItem) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fmt_container, FragmentSongDetails.createFragment(songItem))
            .addToBackStack(null)
            .commit()
    }
}