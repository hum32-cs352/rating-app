@echo off
setlocal
pushd "%~dp0" || goto folderError
if exist "out" rmdir /s /q "out"
mkdir "out" || goto buildError
javac --release 17 -encoding UTF-8 -d "out" @src/sources.txt || goto buildError
java -cp "out" app.Main || goto buildError
popd
pause
exit /b 0
:buildError
echo BUILD OR RUN FAILED. Do not submit as PASS.
popd
pause
exit /b 1
:folderError
echo Cannot open the repository folder.
pause
exit /b 1