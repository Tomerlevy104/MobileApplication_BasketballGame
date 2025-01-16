package com.example.hw1_tomerlevy.managers

import android.content.Context
import android.content.SharedPreferences
import com.example.hw1_tomerlevy.utilities.Constants
import com.example.hw1_tomerlevy.utilities.Player
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//Singleton class that manages the leaderboard data using SharedPreferences
class LeaderboardManager private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        Constants.SPKeys.LEADERBOARD_KEY, Context.MODE_PRIVATE)

    private val gson = Gson()

    //static object
    companion object {
        @Volatile //volatile ensures the instance is always up to date across multiple threads
        private var instance: LeaderboardManager? = null

        fun init(context: Context): LeaderboardManager {
            return instance ?: synchronized(this) {
                //double-checked locking pattern for thread safety
                instance ?: LeaderboardManager(context).also { instance = it }
            }
        }

        //get the singleton instance
        fun getInstance(): LeaderboardManager {
            return instance ?: throw IllegalStateException(
                "LeaderboardManager must be initialized")
        }
    }

    //function to add a new player score to the leaderboard
    fun addScore(player: Player) {
        //get current list of players
        val players = getTopPlayers().toMutableList()
        //add new player
        players.add(player)
        //sort players by score in descending order
        players.sortByDescending { it.score }

        //keep only top MAX_PLAYERS
        if (players.size > Constants.Player.MAX_TOP_10_PLAYERS) {
            players.removeAt(players.lastIndex)
        }
        //save updated list
        saveTopPlayers(players)
    }

    //function to save the list of players to SharedPreferences as JSON
    private fun saveTopPlayers(players: List<Player>) {
        val jsonString = gson.toJson(players)
        sharedPreferences.edit()
            .putString(Constants.SPKeys.TOP_PLAYERS_KEY, jsonString)
            .apply()
    }

    //function to get the list of top players from SharedPreferences
    fun getTopPlayers(): List<Player> {
        //get JSON string from SharedPreferences, default to empty array if not found
        val jsonString = sharedPreferences.getString(Constants.SPKeys.TOP_PLAYERS_KEY, "[]")
        //create TypeToken for proper deserialization of List<Player>
        val type = object : TypeToken<List<Player>>() {}.type
        //convert JSON string back to List<Player>
        return gson.fromJson(jsonString, type)
    }
}