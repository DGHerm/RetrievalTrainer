@echo off
goto :start

:expand
echo %~1 >> envsubst_options
goto:eof

:start
del /f envsubst_options >nul 2>&1
for /f "delims=" %%i in (options) do call:expand "%%i"