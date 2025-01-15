# Basketball Game - Mobile Applications Course Project

<a href="https://ibb.co/3B6QBhq"><img src="https://i.ibb.co/3B6QBhq/Image-For-Read-Me.jpg" alt="Image-For-Read-Me" border="0"></a>

Welcome to my first game! In this exciting challenge, you'll need to dodge the defender and make sure they don't steal your ball. This project is part of the "Mobile Applications" course at Afeka College and was developed as an Android-based mobile game using Android Studio.

## Project Overview

The game involves a **player** (represented by a basketball) that needs to avoid obstacles (represented by defense players) trying to steal the ball. Players can choose between button controls or sensor-based controls, where tilting the device moves the ball. The game features a global leaderboard that tracks high scores along with the locations where they were achieved.

### Technologies Used:
- **Programming Language:** Kotlin
- **IDE:** Android Studio
- **XML:** For designing UI layouts
- **Google Maps API:** For displaying score locations
- **SharedPreferences:** For persistent leaderboard data

## How to Run the Project

1. **Clone or Download the Repository**:
   ```bash
   git clone https://github.com/yourusername/basketball-game.git
   ```
   Or simply download the ZIP file of this repository from GitHub.

2. **Set Up Google Maps API Key**:
   - Go to the [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new project or select an existing one
   - Enable the Maps SDK for Android
   - Create credentials (API key)
   - Open `AndroidManifest.xml` and verify the metadata tag exists:
     ```xml
     <meta-data
         android:name="com.google.android.geo.API_KEY"
         android:value="${MAPS_API_KEY}"/>
     ```

3. **Open in Android Studio**:
   - Open Android Studio
   - Click on **File > Open** and select the project folder
   - Wait for the project to sync

4. **Build the Project**:
   - Click on **Build > Make Project** to compile
   - Ensure all dependencies are properly synced

5. **Run the Application**:
   - Connect your Android device (enable USB debugging) or use the emulator
   - Ensure your device/emulator has:
     - Location services enabled
     - Accelerometer (for sensor controls)
   - Click **Run > Run 'app'** (or press Shift + F10)

6. **Start Playing**:
   - Enter your name
   - Choose your control mode (Buttons/Sensors)
   - Select difficulty level (Easy/Hard)
   - Use buttons or tilt your device to control the ball
   - Avoid the defenders and try to achieve a high score!

## Game Instructions

- **Objective**: Control the basketball to avoid collisions with the defense players.
- **Controls**:
  - **Button Mode**:
    - Use the **Left** button to move left
    - Use the **Right** button to move right
  - **Sensor Mode**:
    - Tilt device left/right to move the ball
    - Tilt forward/backward to adjust game speed
- **Game Over**: The game ends when all lives are lost.

### Notes:
- The game has a timer that controls the movement of obstacles
- Each defender collision costs one life
- Your high scores are saved with your current location
- View the leaderboard to see where other players achieved their high scores
- Location permission is required to save your score location

---

Enjoy the game and try to survive as long as possible!
