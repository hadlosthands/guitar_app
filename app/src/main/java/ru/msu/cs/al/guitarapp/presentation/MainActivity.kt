package ru.msu.cs.al.guitarapp.presentation

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chord_recognition_lib.ChordRecognizer
import io.reactivex.disposables.CompositeDisposable
import ru.msu.cs.al.guitarapp.R
import ru.msu.cs.al.guitarapp.presentation.list.FragmentSongList
import android.Manifest.permission.RECORD_AUDIO
import android.content.pm.PackageManager.PERMISSION_GRANTED


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fmt_container, FragmentSongList())
                .commit()
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (supportFragmentManager.fragments.size > 1) {
                        supportFragmentManager.popBackStack()
                    } else {
                        finish()
                    }
                }
            }
        )
    }

    private fun requestAudio(): Boolean {
        if (checkSelfPermission(RECORD_AUDIO) != PERMISSION_GRANTED) {
            requestPermissions(arrayOf(RECORD_AUDIO), 1337)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}