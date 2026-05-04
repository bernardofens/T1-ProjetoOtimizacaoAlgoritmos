@echo off 
setlocal 
# roda os testes com maven
cd /d "%~dp0.." 
call mvnw.cmd -q test
if errorlevel 1 exit /b 1
echo.
echo testes concluidos com sucesso.
exit /b 0
