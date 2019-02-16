@echo off
title Server Console

:start
echo Starting Server.
echo.

java -Djava.util.logging.config.file=Config/Logging.properties -jar WebServer-0.0.1-SNAPSHOT.jar

if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
goto end

:restart
echo.
echo Admin Restarted Server.
echo.
goto start

:error
echo.
echo Server Terminated Abnormally!
echo.

:end
echo.
echo Server Terminated.
echo.
pause