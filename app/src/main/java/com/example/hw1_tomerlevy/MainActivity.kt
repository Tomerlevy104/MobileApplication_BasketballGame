package com.example.hw1_tomerlevy

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.example.hw1_tomerlevy.interfaces.TiltCallback
import com.example.hw1_tomerlevy.managers.GameManager
import com.example.hw1_tomerlevy.managers.LeaderboardManager
import com.example.hw1_tomerlevy.managers.ObstacleManager
import com.example.hw1_tomerlevy.utilities.ObstacleType
import com.example.hw1_tomerlevy.utilities.Constants
import com.example.hw1_tomerlevy.managers.SignalManager
import com.example.hw1_tomerlevy.utilities.SingleSoundPlayer
import com.example.hw1_tomerlevy.utilities.TiltDetector
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity(), TiltCallback {

    private lateinit var main_BTN_Left: MaterialButton
    private lateinit var main_BTN_Right: MaterialButton
    private lateinit var main_IMG_hearts: Array<AppCompatImageView>
    private lateinit var main_LBL_score: MaterialTextView
    private lateinit var main_IMG_balls: Array<AppCompatImageView>
    private lateinit var gameManager: GameManager
    private lateinit var obstacleManager: ObstacleManager
    private lateinit var timer: Timer
    private lateinit var soundPlayer: SingleSoundPlayer
    private lateinit var tiltDetector: TiltDetector
    private var ratio: Int = Constants.Level.RATIOEASY
    private var obstacleCounter = 0
    private var isSensorMode: Boolean = false
    private var baseSpeed: Long = Constants.Level.EXRUNTIMEEASY //default speed
    private var currentSpeed: Long = baseSpeed //current speed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LeaderboardManager.init(this)
        findViews()
        initializeGame()
        initControls()
        startGame()
    }

    private fun findViews() {
        main_BTN_Left = findViewById(R.id.main_BTN_Left)
        main_BTN_Right = findViewById(R.id.main_BTN_Right)
        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2),
            findViewById(R.id.main_IMG_heart3))

        main_LBL_score = findViewById(R.id.main_LBL_score)

        main_IMG_balls = arrayOf(
            findViewById(R.id.main_IMG_ball0),
            findViewById(R.id.main_IMG_ball1),
            findViewById(R.id.main_IMG_ball2),
            findViewById(R.id.main_IMG_ball3),
            findViewById(R.id.main_IMG_ball4))

        val columns = arrayOf(
            getColumnViews(0),
            getColumnViews(1),
            getColumnViews(2),
            getColumnViews(3),
            getColumnViews(4))

        obstacleManager = ObstacleManager(columns.toList())
    }

    //function that returns an array of ImageViews for a specific column
    private fun getColumnViews(colIndex: Int): Array<AppCompatImageView> {
        return Array(Constants.Obstacles.NUMBERSOFROWS-1) { row ->  //creates array of 10 ImageViews for a column
            findViewById<AppCompatImageView>( //finds view in layout by ID
                resources.getIdentifier(  //gets resource ID dynamically
                    "obstacle_defender_${row}_${colIndex}",  //ID pattern: obstacle_defender_[row]_[column]
                    "id",  //looking for ID resource type
                    packageName  //within this app's package
                )
            )
        }
    }

    private fun initializeGame() {
        gameManager = GameManager(main_IMG_hearts.size)
        soundPlayer = SingleSoundPlayer(this)

        intent.getStringExtra("GAME_LEVEL")?.let { gameLevel ->
            ratio = when (gameLevel) {
                "easy" -> Constants.Level.RATIOEASY
                "hard" -> Constants.Level.RATIOHARD
                else -> Constants.Level.RATIOEASY
            }
            baseSpeed = when (gameLevel) {
                "easy" -> Constants.Level.EXRUNTIMEEASY
                "hard" -> Constants.Level.EXRUNTIMEHARD
                else -> Constants.Level.EXRUNTIMEEASY
            }
            this.currentSpeed = baseSpeed //initialize the speed according to player's selection
        }

        isSensorMode = intent.getStringExtra("GAME_MODE") == "sensors"

        if (isSensorMode) {
            tiltDetector = TiltDetector(this, this)
            main_BTN_Left.visibility = View.GONE
            main_BTN_Right.visibility = View.GONE
        }
    }

    //init left and right buttons
    private fun initControls() {
        main_BTN_Left.setOnClickListener {
            if (gameManager.currentIndexOfBall > 0) {
                gameManager.moveLeft()
                updateBallPosition()
            }
        }
        main_BTN_Right.setOnClickListener {
            if (gameManager.currentIndexOfBall < 4) {
                gameManager.moveRight()
                updateBallPosition()
            }
        }
        updateBallPosition()
    }

    private fun startGame() {
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    obstacleCounter++
                    obstacleManager.addObstacleToRandomCol(ratio, obstacleCounter)
                    val collisionType = obstacleManager.moveObstaclesDown(gameManager.currentIndexOfBall)

                    if (collisionType != null) {
                        handleCollision(collisionType) //check collision
                    }
                }
            }
        }, Constants.Timer.DELAY, this.currentSpeed)
    }

    private fun updateBallPosition() {
        for (i in main_IMG_balls.indices) {
            main_IMG_balls[i].visibility = if (i == gameManager.currentIndexOfBall) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun handleCollision(obstacleType: ObstacleType) {
        when (obstacleType) {
            ObstacleType.BADOBSTACLE -> {
                soundPlayer.playSound(R.raw.bad_collision_referee_whistle_sound)
                gameManager.addBadCollision()
                if (gameManager.badCollision > 0) {
                    main_IMG_hearts[main_IMG_hearts.size - gameManager.badCollision].visibility = View.INVISIBLE
                    SignalManager.getInstance().toast("Oh No!")
                    SignalManager.getInstance().vibrate()
                }
                if (gameManager.isGameOver){
                    gameOver()
                }
            }
            ObstacleType.GOODOBSTACLE -> {
                gameManager.addPoints()
                main_LBL_score.text = gameManager.playerScore.toString()
            }
            ObstacleType.NONE -> {
            //do nothing
            }
        }
    }

    //tilt option
    override fun tiltX(value: Float) {
        if (value > 3.0f && gameManager.currentIndexOfBall > 0) {
            gameManager.moveLeft()
            updateBallPosition()
        } else if (value < -3.0f && gameManager.currentIndexOfBall < 4) {
            gameManager.moveRight()
            updateBallPosition()
        }
    }

    override fun tiltY(value: Float) {
        if (!isSensorMode) return
        //calculate new speed due to tilt Y
        val newSpeed = when {
            value < -2.0f -> { //back tilt
                //coerceAtLeast(50) - A function that ensures that the value is not less than 50
                (baseSpeed * 0.3).toLong().coerceAtLeast(50)  //faster speed
            }
            value > 2.0f -> { //forth tilt
                (baseSpeed * 2.0).toLong().coerceAtMost(1000) //slower speed
            }
            else -> baseSpeed   //no tilt
        }

        this.currentSpeed = newSpeed //update the speed
        restartTimer()

    }

    //reinitialize the timer
    private fun restartTimer() {
        timer.cancel()
        startGame()
    }

    private fun gameOver() {
        val intent = Intent(this, GameOverActivity::class.java).apply {
            putExtra("PLAYER_NAME", intent.getStringExtra("PLAYER_NAME"))
            putExtra("SCORE", gameManager.playerScore)
            putExtra("GAME_MODE", intent.getStringExtra("GAME_MODE"))
        }
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        if (isSensorMode) tiltDetector.start()
        if (::timer.isInitialized) {
            timer.cancel()
            startGame()
        }
    }

    override fun onPause() {
        super.onPause()
        if (isSensorMode) tiltDetector.stop()
        if (::timer.isInitialized) timer.cancel()
    }
}