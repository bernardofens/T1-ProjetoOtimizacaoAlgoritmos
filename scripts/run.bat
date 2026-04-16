@echo off
setlocal EnableExtensions

rem execucao padrao sem maven
rem compila todas as classes do pacote com javac
rem e executa o main usando classpath na pasta out

set "proj=%~dp0.."
set "out=%proj%\out"
set "src=%proj%\src\main\java\com\poa\lcs"

if not exist "%out%" (
  mkdir "%out%" >nul 2>nul
)

rem compila a partir do source root para o pacote ficar correto
pushd "%proj%\src\main\java"
javac -d "%out%" com\poa\lcs\*.java
popd
if errorlevel 1 exit /b 1

java -cp "%out%" com.poa.lcs.Main
exit /b %errorlevel%

