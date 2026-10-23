@echo off
setlocal
pushd "%~dp0..\.." || exit /b 1
if not exist "out\A02" mkdir "out\A02"
javac --release 17 -encoding UTF-8 -d "out\A02" ^
"@handoff\A02\sources.txt"
if errorlevel 1 goto fail
java -cp "out\A02" domain.StudentDriver < "handoff\A02\demo-input.txt"
if errorlevel 1 goto fail
popd
pause
exit /b 0
:fail
echo A02 FAILED read the error above.
popd
pause
exit /b 1