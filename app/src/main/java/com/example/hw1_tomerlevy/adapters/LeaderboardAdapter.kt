package com.example.hw1_tomerlevy.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hw1_tomerlevy.R
import com.example.hw1_tomerlevy.utilities.Player
import com.google.android.material.textview.MaterialTextView

class LeaderboardAdapter(private val onPlayerSelected: ((Player) -> Unit)? = null)
    : RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {
    private var players: List<Player> = ArrayList() //players list

    ///////////////////////////////////////////////////////////////////////////////////////
    //view holder that contains all the graphical elements of each row in the table
    class LeaderboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val rankTV: MaterialTextView = view.findViewById(R.id.leaderboard_LBL_rank) //rank
        val nameTV: MaterialTextView = view.findViewById(R.id.leaderboard_LBL_name) //name
        val scoreTV: MaterialTextView = view.findViewById(R.id.leaderboard_LBL_score) //score
        val modeTV: MaterialTextView = view.findViewById(R.id.leaderboard_LBL_mode) //mode
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    //create a new view holder when required
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        //inflate the row layout from an XML file
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.leader_board_item, parent, false)
        return LeaderboardViewHolder(view)
    }

    //link the data to the view for each row
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val player = players[position]
        holder.rankTV.text = (position + 1).toString()
        holder.nameTV.text = player.name
        holder.scoreTV.text = player.score.toString()
        holder.modeTV.text = player.gameMode

        holder.itemView.setOnClickListener { //on click row
            onPlayerSelected?.invoke(player)
        }
    }

    //function to return number of row in the table
    override fun getItemCount() = players.size

    //function for update player's list
    @SuppressLint("NotifyDataSetChanged")
    fun updatePlayers(newPlayers: List<Player>) {
        players = newPlayers
        notifyDataSetChanged()  //notifies RecyclerView that the data has changed and the view should be refreshed.
    }
}