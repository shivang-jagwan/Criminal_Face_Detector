import cv2
import os
import time

def capture_image():
    # Create capture directory if not exists
    os.makedirs("capture", exist_ok=True)
    
    # Initialize webcam
    cam = cv2.VideoCapture(0, cv2.CAP_DSHOW)  # Use DirectShow API on Windows
    
    if not cam.isOpened():
        print("Error: Could not open webcam")
        return None
        
    # Set properties for better quality (if supported)
    cam.set(cv2.CAP_PROP_FRAME_WIDTH, 640)
    cam.set(cv2.CAP_PROP_FRAME_HEIGHT, 480)
    
    print("Camera opened successfully. Press SPACE to capture or ESC to exit.")
    
    # Create window
    cv2.namedWindow("Camera Feed")
    
    capturing = True
    captured_frame = None
    
    # Show video feed
    while capturing:
        ret, frame = cam.read()
        
        if not ret:
            print("Failed to grab frame")
            break
            
        # Display the frame
        cv2.imshow("Camera Feed", frame)
        
        # Wait for key press
        key = cv2.waitKey(1) & 0xFF
        
        # Space key to capture
        if key == 32:  # SPACE key
            captured_frame = frame.copy()
            print("Image captured!")
            capturing = False
            
        # ESC key to exit
        elif key == 27:  # ESC key
            print("Camera feed closed by user")
            capturing = False
    
    # Clean up
    cam.release()
    cv2.destroyAllWindows()
    
    # Save the captured image if available
    if captured_frame is not None:
        output_path = "capture/temp.jpg"
        cv2.imwrite(output_path, captured_frame)
        print(f"Image saved to {output_path}")
        
        # Add a preview window of the captured image
        cv2.imshow("Captured Image", captured_frame)
        cv2.waitKey(1500)  # Show for 1.5 seconds
        cv2.destroyAllWindows()
        return output_path
    
    return None

if __name__ == "__main__":
    try:
        result_path = capture_image()
        if result_path:
            print(f"Successfully captured and saved image to {result_path}")
            exit(0)
        else:
            print("Failed to capture image")
            exit(1)
    except Exception as e:
        print(f"Error: {str(e)}")
        exit(1)