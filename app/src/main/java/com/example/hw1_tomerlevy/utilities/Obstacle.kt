package com.example.hw1_tomerlevy.utilities

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.example.hw1_tomerlevy.R

enum class ObstacleType {
    BADOBSTACLE, GOODOBSTACLE, NONE
}

class Obstacle(
    private val imageView: AppCompatImageView,
    var type: ObstacleType = ObstacleType.NONE) {

    //show obstacle function
    fun show() {
        when (type) {
            ObstacleType.BADOBSTACLE -> {
                imageView.setImageResource(R.drawable.basketball_player_defence)
                imageView.visibility = View.VISIBLE
            }

            ObstacleType.GOODOBSTACLE -> {
                imageView.setImageResource(R.drawable.basket)
                imageView.visibility = View.VISIBLE
            }

            ObstacleType.NONE -> {
                imageView.visibility = View.INVISIBLE
            }
        }
    }

    //hide obstacle function
    fun hide() {
        imageView.visibility = View.INVISIBLE
        type = ObstacleType.NONE
    }

    fun isVisible(): Boolean {
        return imageView.visibility == View.VISIBLE
    }
}