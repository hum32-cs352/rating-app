@echo off
setlocal
rem Script lives in handoff\C04\assessment, so the repository root is three levels up.
pushd "%~dp0..\..\.." || goto folderError

rem Clean only this module's generated output.
if exist "out\C04-assessment" rmdir /s /q "out\C04-assessment"
mkdir "out\C04-assessment" || goto buildError

javac --release 17 -encoding UTF-8 -d "out\C04-assessment" @handoff/C04/assessment/sources.txt || goto buildError

rem Checks first. If they fail, the driver never runs.
java -cp "out\C04-assessment" pilot.c04.assessment.AssessmentChecks || goto buildError
java -cp "out\C04-assessment" pilot.c04.assessment.AssessmentDriver || goto buildError

popd
pause
exit /b 0

:buildError
echo.
echo BUILD, CHECK OR RUN FAILED. Do not submit as PASS.
popd
pause
exit /b 1

:folderError
echo Cannot open the repository folder.
pause
exit /b 1
