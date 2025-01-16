package com.example.hw1_tomerlevy

import android.content.Intent
import android.content.pm.PackageManager
import android.Manifest
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import com.example.hw1_tomerlevy.managers.LeaderboardManager
import com.example.hw1_tomerlevy.managers.SignalManager
import com.google.android.material.button.MaterialButton

class StartPageActivity : AppCompatActivity() {

    private lateinit var start_editText_name: AppCompatEditText
    private lateinit var start_radioGroup_mode: RadioGroup
    private lateinit var start_radioGroup_level: RadioGroup
    private lateinit var start_radioButton_buttons: RadioButton
    private lateinit var start_radioButton_sensors: RadioButton
    private lateinit var start_radioButton_easy: RadioButton
    private lateinit var start_radioButton_hard: RadioButton
    private lateinit var start_BTN_startGame: MaterialButton
    private lateinit var start_BTN_Top10Records: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_page)
        LeaderboardManager.init(this)
        checkLocationPermission() //check location permissions
        findViews()
        initViews()
    }

    private fun findViews() {
        start_editText_name = findViewById(R.id.start_editText_name)
        start_BTN_startGame = findViewById(R.id.start_BTN_startGame)
        start_radioGroup_mode = findViewById(R.id.start_radioGroup_mode)
        start_radioGroup_level = findViewById(R.id.start_radioGroup_level)
        start_radioButton_buttons = findViewById(R.id.start_radioButton_buttons)
        start_radioButton_sensors = findViewById(R.id.start_radioButton_sensors)
        start_radioButton_easy = findViewById(R.id.start_radioButton_easy)
        start_radioButton_hard = findViewById(R.id.start_radioButton_hard)
        start_BTN_Top10Records = findViewById(R.id.start_BTN_Top10Records)
    }

    private fun initViews() {
        start_BTN_startGame.setOnClickListener {
            val name = start_editText_name.text.toString().trim()
            val selectedModeId = start_radioGroup_mode.checkedRadioButtonId
            val selectedLevelId = start_radioGroup_level.checkedRadioButtonId

            if (name.isEmpty() || selectedModeId == -1 || selectedLevelId == -1) {
                SignalManager.getInstance().toast("Please fill all fields")
                return@setOnClickListener
            }

            val mode = when (selectedModeId) {
                R.id.start_radioButton_buttons -> "buttons"
                R.id.start_radioButton_sensors -> "sensors"
                else -> "unknown"
            }

            val level = when (selectedLevelId) {
                R.id.start_radioButton_easy -> "easy"
                R.id.start_radioButton_hard -> "hard"
                else -> "unknown"
            }

            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("PLAYER_NAME", name)
                putExtra("GAME_MODE", mode)
                putExtra("GAME_LEVEL", level)
            }
            startActivity(intent)
            finish()
        }

        start_BTN_Top10Records.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        }
    }

    //location permission
    //ask for location permission
    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            //permission request with an explanation to the user why the permission is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                //showing an explanation to the user why we need the permission
                AlertDialog.Builder(this)
                    .setTitle("Location permission required")
                    .setMessage("Location permission is required to show the location where game records were broken.")
                    .setPositiveButton("Approval") { _, _ ->
                        requestLocationPermission()
                    }
                    .setNegativeButton("Cancellation") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            } else {
                //request permission directly
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    //handling the user's response to authorization
    override fun onRequestPermissionsResult(
        requestCode: Int, //1001
        permissions: Array<out String>, //an array of all the permissions we requested
        grantResults: IntArray //an array containing the results for each permission we requested
    ) {
        //onRequestPermissionsResult is a function that is automatically called by the system when the user responds
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission accepted
                    SignalManager.getInstance().toast("Permission accepted")
                } else {
                    //permission denied
                    SignalManager.getInstance()
                        .toast("Location permission denied - we can't show peak locations")
                }
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}