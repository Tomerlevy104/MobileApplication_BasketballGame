package com.example.hw1_tomerlevy.managers

import com.example.hw1_tomerlevy.utilities.Constants

class GameManager(private val lifeCount: Int = Constants.Game.LIFE) {

    var playerScore: Int = 0
    var currentIndexOfBall: Int = 2
        private set //there is no Setter for "currentIndex". you can change currentIndex only from this class

    var badCollision: Int = 0
        private set //there is no Setter for "bad_collision". you can change bad_collision only from this class

    val isGameOver: Boolean
        get() = badCollision == lifeCount

    fun addBadCollision() {
        badCollision++
    }

    fun moveLeft() {
        currentIndexOfBall--
    }

    fun moveRight() {
        currentIndexOfBall++
    }

    fun addPoints(){
        playerScore+= Constants.Score.POINT
    }
}