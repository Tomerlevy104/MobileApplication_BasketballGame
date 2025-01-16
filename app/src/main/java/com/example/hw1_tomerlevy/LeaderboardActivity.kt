package com.example.hw1_tomerlevy

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.hw1_tomerlevy.fragments.HighScoreFragment
import com.example.hw1_tomerlevy.fragments.MapFragment
import com.google.android.material.button.MaterialButton

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var lb_FRAME_records: FrameLayout
    private lateinit var lb_FRAME_map: FrameLayout
    private lateinit var mapFragment: MapFragment
    private lateinit var leaderboard_BTN_back: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader_board)
        findViews()
        initFragments()
        initViews()
    }

    private fun findViews() {
        lb_FRAME_records = findViewById(R.id.lb_FRAME_records)
        lb_FRAME_map = findViewById(R.id.lb_FRAME_map)
        leaderboard_BTN_back = findViewById(R.id.leaderboard_BTN_back)
    }

    private fun initFragments() {
        //initial high score table fragment
        if (supportFragmentManager.findFragmentById(R.id.lb_FRAME_records) == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.lb_FRAME_records, HighScoreFragment())
                .commit()
        }

        //initialize map fragment
        if (supportFragmentManager.findFragmentById(R.id.lb_FRAME_map) == null) {
            mapFragment = MapFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.lb_FRAME_map, mapFragment)
                .commit()
        }
    }

    private fun initViews() {
        leaderboard_BTN_back.setOnClickListener {
            val intent = Intent(this, StartPageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
