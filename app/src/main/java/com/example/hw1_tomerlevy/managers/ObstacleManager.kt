package com.example.hw1_tomerlevy.managers

import androidx.appcompat.widget.AppCompatImageView
import com.example.hw1_tomerlevy.utilities.Constants
import com.example.hw1_tomerlevy.utilities.Obstacle
import com.example.hw1_tomerlevy.utilities.ObstacleType


//class to manage the game's obstacles including their movement
class ObstacleManager(private val columns: List<Array<AppCompatImageView>>) {

    //creates 5x10 matrix of Obstacle objects, each linked to an ImageView
    private val obstacles: Array<Array<Obstacle>> = Array(5) { col ->
        Array((Constants.Obstacles.NUMBERSOFROWS)-1) { row ->
            Obstacle(columns[col][row])
        }
    }

    fun moveObstaclesDown(ballPosition: Int): ObstacleType? {
        var collisionType: ObstacleType? = null
        for (col in 0..((Constants.Obstacles.NUMBEROFCOLS)-1)) {
            for (row in (Constants.Obstacles.BALL_ROW - 1) downTo 0) {
                val currentObstacle = obstacles[col][row]

                if (!currentObstacle.isVisible()) continue

                //if the obstacle is in the last row
                if (row == (Constants.Obstacles.BALL_ROW - 1)) {
                    if (col == ballPosition) { //check collision
                        collisionType = currentObstacle.type //define collision type
                    }
                    currentObstacle.hide()
                } else {
                    moveObstacleOneRowDown(col, row) //move obstacle one row down
                }
            }
        }
        return collisionType
    }

    //function to move the obstacle one row down
    private fun moveObstacleOneRowDown(col: Int, row: Int) {
        val currentObstacle = obstacles[col][row]
        val nextObstacle = obstacles[col][row + 1]

        nextObstacle.type = currentObstacle.type
        nextObstacle.show()
        currentObstacle.hide()
    }

    //function to add a new obstacle to random column
    fun addObstacleToRandomCol(ratio: Int, obstacleCounter: Int) {
        if (obstacleCounter % ratio != 0) return //checks if the obstacleCounter is divisible by the ratio. If not, exits the function.

        val randomColumn = (0..(Constants.Obstacles.NUMBEROFCOLS)-1).random()
        val obstacleType =
            if (obstacleCounter % Constants.GoodObstacleRatio.GOR == 0) ObstacleType.GOODOBSTACLE else ObstacleType.BADOBSTACLE

        obstacles[randomColumn][0].type = obstacleType
        obstacles[randomColumn][0].show()
    }
}