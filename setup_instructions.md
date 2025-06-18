# Criminal Face Detection System - Setup Instructions

## Initial Setup

### 1. Install Python Dependencies

```
pip install opencv-python deepface numpy
```

### 2. Setup MySQL Database

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

### 3. Run the Application

Execute the `run.bat` file to compile and start the application.

## Using the Application

1. **Home Page**: Click "Try Demo" to go to the detection page.

2. **Detect Page**: 
   - Click "Capture and Detect" to take a photo with your webcam
   - The system will compare the face against the criminal database
   - If a match is found, it will display the criminal's details

3. **Add Criminal**:
   - Fill in the criminal's details
   - Upload an image 
   - Click "Add Criminal" to save to the database

## Common Issues and Solutions

### Camera Not Working
- Make sure your webcam is connected and functioning
- Check if other applications can access your webcam
- Ensure you have proper permissions to access camera hardware

### Database Connection Issues
- Verify MySQL is running
- Check if the database credentials in `DatabaseConnection.java` are correct
- Ensure the criminal_db database exists

### Python Script Errors
- Verify all Python dependencies are installed
- Check if Python is available in your system PATH
- Look for any error messages in the console output
