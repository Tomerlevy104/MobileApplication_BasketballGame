package com.example.hw1_tomerlevy.managers

import android.content.Context
import android.content.Context.VIBRATOR_MANAGER_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import java.lang.ref.WeakReference

class SignalManager private constructor(context: Context) {

    private val contextRef = WeakReference(context)

    //function to trigger device vibration
    fun vibrate() {
        //check if Context still exists
        contextRef.get()?.let { context ->
            //get vibrator service based on Android version
            val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                //for Android 12 and above, use VibratorManager
                val vibratorManager =
                    context.getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                //for older versions, use Vibrator directly
                context.getSystemService(VIBRATOR_SERVICE) as Vibrator
            }

            //check Android version for VibrationEffect support
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //create a single vibration effect
                val oneShotVibrationEffect = VibrationEffect.createOneShot(
                    500,    //duration
                    VibrationEffect.DEFAULT_AMPLITUDE  //vibration strength
                )

                //trigger the one-shot vibration
                vibrator.vibrate(oneShotVibrationEffect)

            } else {
                //for older Android versions, use the simple vibrate method
                vibrator.vibrate(500)  //vibrate for 500 milliseconds
            }
        }
    }

    //function to display toast messages
    fun toast(text: String) {
        contextRef.get()?.let { context ->
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show() //show toast message
        }
    }

    //companion object for Singleton implementation
    companion object {
        @Volatile  //Ensures the variable is updated across all threads
        private var instance: SignalManager? = null

        //initialize the SignalManager instance
        fun init(context: Context): SignalManager {
            return instance ?: synchronized(this) { //synchronize to prevent race conditions
                //create new instance if none exists
                instance ?: SignalManager(context).also { instance = it }
            }
        }

        //get the singleton instance
        fun getInstance(): SignalManager {
            return instance ?: throw IllegalStateException(
                "SignalManager must be initialized by calling init(context) before use."
            )
        }
    }
}