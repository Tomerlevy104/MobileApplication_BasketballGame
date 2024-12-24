package com.example.hw1_tomerlevy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.example.hw1_tomerlevy.logic.GameManager
import com.example.hw1_tomerlevy.utilities.Constants
import com.example.hw1_tomerlevy.utilities.SignalManager
import com.google.android.material.button.MaterialButton
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {

    private lateinit var main_BTN_Left: MaterialButton

    private lateinit var main_BTN_Right: MaterialButton

    private lateinit var main_IMG_hearts: Array<AppCompatImageView>

    private lateinit var main_IMG_c0: Array<AppCompatImageView>

    private lateinit var main_IMG_c1: Array<AppCompatImageView>

    private lateinit var main_IMG_c2: Array<AppCompatImageView>

    private lateinit var gameManager: GameManager

    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //connect the activity to the XML file ("activity_main.xml")
        findViews() //locates the graphic elements defined in XML and connect them to variables in the class
        main_IMG_c1[Constants.IMG.BALL_ROW].setImageResource(R.drawable.ball) //initial the ball position
        gameManager = GameManager(main_IMG_hearts.size)
        initViews() //initiating various actions, such as setting actions for buttons
    }

    private fun findViews() {
        main_BTN_Left = findViewById(R.id.main_BTN_Left)
        main_BTN_Right = findViewById(R.id.main_BTN_Right)
        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2),
            findViewById(R.id.main_IMG_heart3)
        )

        //column 0
        main_IMG_c0 = arrayOf(
            findViewById(R.id.obstacle_defender_0_0),
            findViewById(R.id.obstacle_defender_1_0),
            findViewById(R.id.obstacle_defender_2_0),
            findViewById(R.id.obstacle_defender_3_0),
            findViewById(R.id.obstacle_defender_4_0),
            findViewById(R.id.obstacle_defender_5_0),
            findViewById(R.id.obstacle_defender_6_0),
            findViewById(R.id.obstacle_defender_7_0),
            findViewById(R.id.obstacle_defender_8_0),
            findViewById(R.id.obstacle_defender_9_0),
            findViewById(R.id.main_IMG_ball0)
        )

        //column 1
        main_IMG_c1 = arrayOf(
            findViewById(R.id.obstacle_defender_0_1),
            findViewById(R.id.obstacle_defender_1_1),
            findViewById(R.id.obstacle_defender_2_1),
            findViewById(R.id.obstacle_defender_3_1),
            findViewById(R.id.obstacle_defender_4_1),
            findViewById(R.id.obstacle_defender_5_1),
            findViewById(R.id.obstacle_defender_6_1),
            findViewById(R.id.obstacle_defender_7_1),
            findViewById(R.id.obstacle_defender_8_1),
            findViewById(R.id.obstacle_defender_9_1),
            findViewById(R.id.main_IMG_ball1)
        )

        //column 2
        main_IMG_c2 = arrayOf(
            findViewById(R.id.obstacle_defender_0_2),
            findViewById(R.id.obstacle_defender_1_2),
            findViewById(R.id.obstacle_defender_2_2),
            findViewById(R.id.obstacle_defender_3_2),
            findViewById(R.id.obstacle_defender_4_2),
            findViewById(R.id.obstacle_defender_5_2),
            findViewById(R.id.obstacle_defender_6_2),
            findViewById(R.id.obstacle_defender_7_2),
            findViewById(R.id.obstacle_defender_8_2),
            findViewById(R.id.obstacle_defender_9_2),
            findViewById(R.id.main_IMG_ball2)
        )
    }

    private fun initViews() {
        //click on the left button
        main_BTN_Left.setOnClickListener {
            if (gameManager.currentIndexOfBall > 0) {
                gameManager.moveLeft()
                updateBallPosition(gameManager.currentIndexOfBall)
            }
        }
        //click on the right button
        main_BTN_Right.setOnClickListener {
            if (gameManager.currentIndexOfBall < 2) {
                gameManager.moveRight()
                updateBallPosition(gameManager.currentIndexOfBall)
            }
        }
        startGame()
    }

    private fun startGame() {
        addNewObstacle()
    }

    //function for moving the ball
    private fun updateBallPosition(currentIndex: Int) {
        val columns = listOf(main_IMG_c0, main_IMG_c1, main_IMG_c2)
        for (c in columns.indices) {
            val ballImageView = columns[c][Constants.IMG.BALL_ROW]
            if (c == currentIndex) {
                ballImageView.visibility = View.VISIBLE
            } else {
                ballImageView.visibility = View.INVISIBLE
            }
        }
    }

    private fun addNewObstacle() {
        var obstacleCounter = 0  //obstacle counter for knowing that each 2 sec adding new obstacle
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    obstacleCounter++
                    //each 2 sec add new obstacle
                    if (obstacleCounter % 4 == 0) {
                        addObstaclesToFirstRow() //This function is executed every 4 clock cycles.
                    }
                    moveObstacleDown() //This function is executed every 1 clock cycles.
                }
            }
        }, Constants.Timer.DELAY, 500) //The timer executes "run" function every 0.5 sec
    }

    private fun addObstaclesToFirstRow() {
        //random a column
        val randomColumn = (0..2).random()
        val theColumn = when (randomColumn) {
            0 -> main_IMG_c0
            1 -> main_IMG_c1
            else -> main_IMG_c2
        }
        //showing obstacle on the first row
        theColumn[0].visibility = View.VISIBLE
    }

    //function for moving obstacles down
    private fun moveObstacleDown() {
        for (columnIndex in 0..2) {
            val column = when (columnIndex) {
                0 -> main_IMG_c0
                1 -> main_IMG_c1
                else -> main_IMG_c2
            }

            //for each row in the column (down of matrix to up)
            for (i in (column.size - 1) downTo 1) {
                if (column[i - 1].visibility == View.VISIBLE) {
                    //check if it's the last row (ball row)
                    if (i == column.size - 1) {
                        //checking collision
                        checkCollision(columnIndex)
                        //previous obstacle - invisible
                        column[i - 1].visibility = View.INVISIBLE
                    } else {
                        //moving the obstacle one row down
                        column[i].visibility = View.VISIBLE
                        column[i - 1].visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    //function for checking a collision - This function is only reached when the obstacle is in the last row.
    private fun checkCollision(columnIndex: Int) {
        //checking if the column is the same like ball column position
        if (columnIndex == gameManager.currentIndexOfBall) {
            //collision
            gameManager.addBadCollision()
            //update hearts view according to lifeCount
            main_IMG_hearts[main_IMG_hearts.size - gameManager.badCollision].visibility =
                View.INVISIBLE
            //toast message
            if (gameManager.badCollision > 0) {
                toastAndVibrate()
            }
            //check if game over
            if (gameManager.isGameOver) {
                gameOver()
            }
        }
    }

    //toast and vibrate function
    private fun toastAndVibrate() {
        SignalManager.getInstance().toast("Oh No!")
        SignalManager.getInstance().vibrate()
    }

    private fun gameOver() {
        timer.cancel()
        //move to Game Over screen
        val intent = Intent(this, GameOverActivity::class.java)
        startActivity(intent)
        finish()
    }

    //This method is called when the activity becomes active and ready for user interaction
    override fun onResume() {
        super.onResume()
        //Check if the timer has been initialized
        if (this::timer.isInitialized) {
            //Resume the timer if it has already been initialized
            timer.cancel() //cancel the previous timer
            addNewObstacle() //start new timer
        } else {
            //Start the game if the timer is not initialized
            startGame()
        }
    }

    //This method is called when the activity is no longer in the foreground
    override fun onPause() {
        super.onPause()
        //If the timer is initialized, cancel it to pause the game
        if (this::timer.isInitialized) {
            timer.cancel()
        }
    }
}