@echo off
setlocal EnableExtensions

rem execucao padrao sem maven
rem compila todas as classes do pacote com javac
rem e executa o main usando classpath na pasta out

# seta as variaveis de ambiente para o projeto
set "proj=%~dp0.."
# seta a variavel de ambiente out para o diretorio out do projeto
set "out=%proj%\out" 
# seta a variavel de ambiente src para o diretorio src\main\java\com\poa\lcs do projeto
set "src=%proj%\src\main\java\com\poa\lcs"
# verifica se o diretorio out existe, se nao existir, cria o diretorio
if not exist "%out%" (
  mkdir "%out%" >nul 2>nul
)

rem compila a partir do source root para o pacote ficar correto
pushd "%proj%\src\main\java"
# compila a partir do source root para o pacote ficar correto
javac -d "%out%" com\poa\lcs\*.java
# volta para o diretorio original
popd
if errorlevel 1 exit /b 1

# executa o main usando classpath na pasta out
java -cp "%out%" com.poa.lcs.Main
exit /b %errorlevel%

