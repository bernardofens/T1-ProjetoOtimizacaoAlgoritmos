@echo off
setlocal EnableExtensions
rem roda os testes com maven
pushd "%~dp0.."
call ".\mvnw.cmd" -q test
if errorlevel 1 (
  popd
  exit /b 1
)
popd
echo.
echo testes concluidos com sucesso.
exit /b 0
