@echo off
echo Compiling Java files...
javac -cp "lib/*;." -d bin src/java/*.java src/java/pages/*.java

if %errorlevel% neq 0 (
    echo Error compiling Java files!
    pause
    exit /b %errorlevel%
)

echo Running the application...
java -cp "bin;lib/*" Main

pause
