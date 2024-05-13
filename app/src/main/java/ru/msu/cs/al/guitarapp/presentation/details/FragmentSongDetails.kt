package ru.msu.cs.al.guitarapp.presentation.details

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.chord_recognition_lib.ChordRecognizer
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import ru.msu.cs.al.guitarapp.R
import ru.msu.cs.al.guitarapp.models.SongItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference


class FragmentSongDetails: Fragment(R.layout.fmt_song_details) {

    private val TAG: String = javaClass.simpleName
    private val disposable: CompositeDisposable = CompositeDisposable()
    private var handleChordJob: Job? = null // Job для отслеживания выполнения корутины handleChord()
    private val currentChord: AtomicReference<String> = AtomicReference("") // Переменная для хранения текущего аккорда
    private var scrollView: ScrollView? = null // Переменная для ссылки на ScrollView
    private lateinit var chordList: List<List<String>> // Список массивов с аккордами
    private var pos: Int = 0 // Переменная для отслеживания позиции массива в распаршенном списке аккордов
    private var arrayNum: Int = 0 // Номер массива в списке аккордов


    companion object {
        private const val ARG_SONG_NAME = "ARG_SONG_NAME"
        private const val ARG_ARTIST_NAME = "ARG_ARTIST_NAME"
        private const val ARG_SONG_TEXT = "ARG_SONG_TEXT"
        private const val ARG_IMAGE_URL = "ARG_IMAGE_URL"
        private const val ARG_CHORDS = "ARG_CHORDS"

        fun createFragment(galleryItem: SongItem): FragmentSongDetails  {
            val fragment = FragmentSongDetails()

            fragment.arguments = Bundle().apply {
                putString(ARG_SONG_NAME, galleryItem.songName)
                putString(ARG_ARTIST_NAME, galleryItem.artistName)
                putString(ARG_SONG_TEXT, galleryItem.songText)
                putString(ARG_IMAGE_URL, galleryItem.songPictureUrl)
                putString(ARG_CHORDS, galleryItem.chords)
            }

            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val image = view.findViewById<ImageView>(R.id.img_view)
        val songName = view.findViewById<TextView>(R.id.txtSong)
        val songArtist = view.findViewById<TextView>(R.id.txtArtist)
        val songText = view.findViewById<TextView>(R.id.txtSongText)

        val args = requireArguments()

        Glide.with(this).load(args.getString(ARG_IMAGE_URL)).into(image)
        songName.text = args.getString(ARG_SONG_NAME, "")
        songArtist.text = args.getString(ARG_ARTIST_NAME, "")
        songText.text = args.getString(ARG_SONG_TEXT, "")
        val chords = args.getString(ARG_CHORDS, "")

        val chordLabelText = view.findViewById<TextView>(R.id.chordLabel)
        val chordStream = ChordRecognizer().chordStream()

        chordList = parseChords(chords)
        scrollView = view.findViewById(R.id.scrollView)

        chordRecognizing(chordLabelText, chordStream)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
        val chordLabelText = view?.findViewById<TextView>(R.id.chordLabel)
        chordLabelText?.text = ""
        handleChordJob?.cancel()
    }

    private fun chordRecognizing(chordLabelText: TextView, chordStream: Flowable<String>) {
        disposable.add(chordStream.subscribe({ chordLabel ->
            chordLabelText.text = chordLabel
            currentChord.set(chordLabel)
            handleChordJob?.cancel()
            handleChordJob = lifecycleScope.launch {
                handleChord()
            }
        }, {e -> e.message?.let { Log.e(TAG, it) }}))
    }

    private suspend fun handleChord() {
        val chord = currentChord.get()
        if ((arrayNum < chordList.size) && (pos < chordList[arrayNum].size)) {
            if (chord == chordList[arrayNum][pos]) {
                if (pos == chordList[arrayNum].size - 1) {
                    val scrollAmountInDp = 35
                    val density = resources.displayMetrics.density
                    val scrollAmountInPixels =
                        (scrollAmountInDp * density + 0.5f).toInt()
                    scrollView?.smoothScrollBy(
                        0,
                        scrollAmountInPixels
                    )
                    pos = 0
                    arrayNum++
                } else {
                    pos++
                }
            }
        }
    }

    fun parseChords(chords: String): List<List<String>> {
        val result = mutableListOf<List<String>>()
        val currentChords = mutableListOf<String>()
        var index = 0
        while (index < chords.length) {
            val char = chords[index]
            when {
                char.isLetter() -> {
                    val chord = StringBuilder()
                    chord.append(char)
                    var i = index + 1
                    while (i < chords.length && (chords[i].isLetter() || chords[i] == '#' || chords[i] == 'b' || chords[i] == 'm')) {
                        chord.append(chords[i])
                        i++
                    }
                    currentChords.add(chord.toString())
                    index = i
                }
                char == '.' -> {
                    result.add(currentChords.toList())
                    currentChords.clear()
                    index++
                }
                else -> index++
            }
        }
        return result
    }
}

