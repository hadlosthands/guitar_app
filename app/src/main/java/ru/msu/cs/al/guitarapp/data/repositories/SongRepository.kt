package ru.msu.cs.al.guitarapp.data.repositories

import ru.msu.cs.al.guitarapp.models.SongItem

interface SongRepository {
    fun getSong(): List<SongItem>
}