package ru.msu.cs.al.guitarapp.presentation.list

import ru.msu.cs.al.guitarapp.models.SongItem

interface OnSongItemClicked {

    fun onSongItemClicked(songItem: SongItem)

}