@echo off
goto :start

:expand
echo %~1 >> envsubst_options
goto:eof

:start
for /f "delims=" %%i in (options) do call:expand "%%i"