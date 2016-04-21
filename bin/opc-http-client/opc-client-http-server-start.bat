set JAR_NAME=opc-client-server-1.0-SNAPSHOT-shaded.jar
set HEAP=2600M
IF EXIST "..\assembly\%JAR_NAME%" del /F %JAR_NAME% & copy "..\assembly\%JAR_NAME%" %JAR_NAME%
CALL java ^
-Xms%HEAP% ^
-Xmx%HEAP% ^
-Dfile.encoding=UTF-8 ^
-cp "%JAR_NAME%;." ^
ru.spbstu.dis.opc.client.api.http.server.OpcWrapperApplication server opc.yml