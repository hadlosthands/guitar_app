package ru.msu.cs.al.guitarapp.data.repositories

import ru.msu.cs.al.guitarapp.data.datastores.SongDatastore
import ru.msu.cs.al.guitarapp.models.SongItem

class SongRepositoryImpl(
    private val songDatastore: SongDatastore
): SongRepository {

    override fun getSong(): List<SongItem> = songDatastore.getSong()
}