package com.example.hw1_tomerlevy

import android.content.Intent
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.hw1_tomerlevy.managers.LeaderboardManager
import com.example.hw1_tomerlevy.utilities.Player
import com.google.android.gms.location.LocationServices
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import java.util.Locale

class GameOverActivity : AppCompatActivity() {

    private lateinit var game_over_BTN_start_over: MaterialButton
    private lateinit var game_over_TXT_score: MaterialTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)
        findViews()
        initViews()

        val playerName = intent.getStringExtra("PLAYER_NAME") ?: "Unknown"
        val score = intent.getIntExtra("SCORE", 0)
        val gameMode = intent.getStringExtra("GAME_MODE") ?: "Unknown"

        game_over_TXT_score.text = String.format(Locale.getDefault(), "Score: %d", score) //showing the score
        saveScore(score, playerName, gameMode)
    }

    private fun findViews() {
        game_over_BTN_start_over = findViewById(R.id.game_over_BTN_start_over)
        game_over_TXT_score = findViewById(R.id.game_over_TXT_score)

    }

    private fun initViews() {
        game_over_BTN_start_over.setOnClickListener {
            //go back to start page
            val intent = Intent(this, StartPageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun saveScore(score: Int, playerName: String, gameMode: String) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                val player = Player(
                    name = playerName,
                    score = score,
                    gameMode = gameMode,
                    latitude = location?.latitude,
                    longitude = location?.longitude
                )
                LeaderboardManager.getInstance().addScore(player)
                Log.d("LOG_SAVE_SCORE", "score saved")
            }
        } else {
            //if there is no location permission
            val player = Player(
                name = playerName,
                score = score,
                gameMode = gameMode,
                latitude = null,
                longitude = null
            )
            LeaderboardManager.getInstance().addScore(player)
        }
    }
}