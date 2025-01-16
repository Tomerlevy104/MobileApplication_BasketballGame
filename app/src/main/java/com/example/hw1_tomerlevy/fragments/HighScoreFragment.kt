package com.example.hw1_tomerlevy.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hw1_tomerlevy.R
import com.example.hw1_tomerlevy.adapters.LeaderboardAdapter
import com.example.hw1_tomerlevy.managers.LeaderboardManager

class HighScoreFragment : Fragment() {

    private lateinit var lbf_RV_records: RecyclerView
    private lateinit var leaderboardAdapter: LeaderboardAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_high_score, container, false)

        lbf_RV_records = view.findViewById(R.id.hsf_RV_records)
        lbf_RV_records.layoutManager = LinearLayoutManager(requireContext())


        leaderboardAdapter = LeaderboardAdapter {
            player ->
            Log.d("DEBUG_HIGH_SCORE_FRAGMENT","DEBUG: Player clicked: $player") //LOG
            val mapFragment = parentFragmentManager.findFragmentById(R.id.lb_FRAME_map) as? MapFragment
            mapFragment?.showPlayerLocation(player)
        }

        lbf_RV_records.adapter = leaderboardAdapter
        updateLeaderboard()
        return view
    }

    private fun updateLeaderboard() {
        try {
            val players = LeaderboardManager.getInstance().getTopPlayers()
            leaderboardAdapter.updatePlayers(players)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}