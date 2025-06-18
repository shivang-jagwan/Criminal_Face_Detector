# Criminal Face Detection System

A Java and Python-based application for detecting criminal faces using facial recognition.

## Project Structure

```
criminal-detector/
├── bin/ - Java compiled classes
├── capture/ - Temporary storage for captured images
├── database/ - Criminal database images
├── lib/ - External libraries
└── src/
    ├── java/ - Java source files for UI
    └── python/ - Python scripts for face detection
```

## Prerequisites

- Java JDK 8 or higher
- Python 3.6 or higher
- MySQL Database
- Required Python libraries:
  - OpenCV
  - DeepFace
  - Numpy

## Setup Instructions

### 1. Install Python Requirements

```
pip install opencv-python deepface numpy
```

### 2. Setup MySQL Database

1. Create a database named `criminal_db`
2. Create a table using the following SQL:

```sql
CREATE TABLE criminals (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100),
    crime VARCHAR(255),
    address TEXT
);
```

3. Update database connection parameters in `DatabaseConnection.java` if needed.

### 3. Build and Run the Java Application

```
javac -cp "lib/*" -d bin src/java/*.java src/java/pages/*.java
java -cp "bin;lib/*" Main
```

## Using the Application

1. **Home Page**: Click "Try Demo" to go to the detection page
2. **Detect Page**: 
   - Click "Capture and Detect" to start the webcam, capture an image, and process it
   - The system will compare the face against the criminal database
   - If a match is found, it will display the criminal's details
3. **Add Criminal**:
   - Fill in the criminal's details
   - Upload an image
   - Click "Add Criminal" to save to the database

## Troubleshooting

- Make sure all Python libraries are correctly installed
- Check database connection parameters in `DatabaseConnection.java`
- Ensure proper file permissions for image directories
