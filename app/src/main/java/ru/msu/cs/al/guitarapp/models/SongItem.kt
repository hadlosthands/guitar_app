package ru.msu.cs.al.guitarapp.models

data class SongItem(
    val songName: String = "",
    val artistName: String = "",
    val songText: String = "",
    val songPictureUrl: String = "",
    val chords: String = ""
)
