# Fix for Camera and Image Upload Issues

## Issues Fixed

1. **Camera Capture Issues**:
   - Improved webcam handling to show a live feed before capturing
   - Added better error messages when something goes wrong
   - Fixed issues with Python script execution from Java

2. **Image Upload Issues**:
   - Fixed image display scaling to show uploaded images correctly
   - Added more supported image formats (.jpg, .jpeg, .png, .webp)
   - Fixed file path handling for database storage

## Installation Instructions

### Step 1: Install Python Dependencies

```
pip install opencv-python deepface numpy
```

If you have issues installing DeepFace, try:

```
pip install opencv-python
pip install numpy
pip install tensorflow
pip install keras
pip install deepface
```

### Step 2: Set Up MySQL Database

Run the following SQL commands to set up your database:

```sql
CREATE DATABASE IF NOT EXISTS criminal_db;
USE criminal_db;
CREATE TABLE IF NOT EXISTS criminals (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    crime VARCHAR(255) NOT NULL,
    address TEXT
);
```

### Step 3: Running the Application

1. Run the `run.bat` file to compile and start the application.
2. Use the improved interface:
   - Click "Try Demo" on the home page
   - Click "Capture and Detect" to take a photo (a camera window will appear)
   - Press SPACE to capture a photo or ESC to exit the camera window

### Troubleshooting

1. **Camera not working**:
   - Check if your webcam is properly connected
   - Try using another webcam application to verify it works
   - Look for any error messages in the console

2. **Face detection issues**:
   - Make sure your face is well lit
   - Position yourself centered in the frame
   - If DeepFace isn't working, the system will fall back to basic face detection
