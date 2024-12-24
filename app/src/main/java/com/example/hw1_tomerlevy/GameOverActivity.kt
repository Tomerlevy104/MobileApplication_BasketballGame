package com.example.hw1_tomerlevy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class GameOverActivity : AppCompatActivity() {

    private lateinit var game_over_BTN_start_over: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)
        findViews()
        initViews()
    }

    private fun findViews() {
        game_over_BTN_start_over = findViewById(R.id.game_over_BTN_start_over)
    }

    private fun initViews() {
        game_over_BTN_start_over.setOnClickListener {
            //go back to start a new game
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}