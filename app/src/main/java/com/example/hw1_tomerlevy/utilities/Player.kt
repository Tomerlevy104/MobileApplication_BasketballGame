package com.example.hw1_tomerlevy.utilities

data class Player(
    val name: String,
    val score: Int,
    val gameMode: String,
    val latitude: Double?,
    val longitude: Double?,
    val timestamp: Long = System.currentTimeMillis()
) {

}