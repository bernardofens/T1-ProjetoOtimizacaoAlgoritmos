@echo off
setlocal EnableExtensions

rem execucao padrao sem maven
rem compila todas as classes do pacote com javac
rem e executa o main usando classpath na pasta out

rem seta as variaveis de ambiente para o projeto
set "proj=%~dp0.."
rem seta a variavel de ambiente out para o diretorio out do projeto
set "out=%proj%\out"
rem verifica se o diretorio out existe, se nao existir, cria o diretorio
if not exist "%out%" (
  mkdir "%out%" >nul 2>nul
)

rem compila a partir do source root para o pacote ficar correto
pushd "%proj%\src\main\java"
rem compila a partir do source root para o pacote ficar correto
javac -d "%out%" com\poa\lcs\*.java
rem volta para o diretorio original
popd
if errorlevel 1 exit /b 1

rem executa o main usando classpath na pasta out
java -cp "%out%" com.poa.lcs.Main %*
exit /b %errorlevel%

