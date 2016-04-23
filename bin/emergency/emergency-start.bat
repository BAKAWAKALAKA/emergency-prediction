set JAR_NAME=emergency-prediction-1.0-SNAPSHOT-shaded.jar
set HEAP=500M
IF EXIST "..\..\assembly\emergency\%JAR_NAME%" del /F %JAR_NAME% & copy "..\..\assembly\emergency\%JAR_NAME%" %JAR_NAME%
CALL java ^
-Xms%HEAP% ^
-Xmx%HEAP% ^
-Dfile.encoding=UTF-8 ^
-cp "%JAR_NAME%;." ^
ru.spbstu.dis.ui.emergency.EmergencyPredictionWindow