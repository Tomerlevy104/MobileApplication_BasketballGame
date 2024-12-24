package com.example.hw1_tomerlevy.logic

class GameManager(private val lifeCount: Int = 3) {

    var currentIndexOfBall: Int = 1
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
}