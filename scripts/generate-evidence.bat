@echo off
setlocal

rem gera evidencias basicas para o relatorio
rem isso roda o benchmark e salva a saida do terminal em docs\evidencias

cd /d "%~dp0.."

if not exist "docs\evidencias" (
  mkdir "docs\evidencias"
)

call mvnw.cmd -q -DskipTests compile exec:java "-Dexec.args=--csv" > "docs\evidencias\execucao_terminal.txt"
if errorlevel 1 exit /b 1

echo.
echo evidencias geradas em docs\evidencias
echo - execucao_terminal.txt
echo - results\benchmark.csv
exit /b 0

