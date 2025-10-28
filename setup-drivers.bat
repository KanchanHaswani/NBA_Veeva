@echo off
REM NBA Automation Framework - Driver Setup Script for Windows
REM This script helps install WebDriver executables for offline testing

setlocal enabledelayedexpansion

REM Configuration
set DRIVERS_DIR=.\drivers
set CACHE_DIR=%USERPROFILE%\.cache\selenium-drivers
set CHROME_VERSION=119.0.6045.105
set FIREFOX_VERSION=0.33.0
set EDGE_VERSION=119.0.2151.58

echo NBA Automation Framework - Driver Setup
echo =====================================
echo OS: Windows
echo.

REM Function to download driver
:download_driver
set driver_name=%1
set version=%2
set url=%3
set filename=%4

echo Downloading %driver_name%...

REM Create drivers directory
if not exist "%DRIVERS_DIR%" mkdir "%DRIVERS_DIR%"

REM Download driver using PowerShell
powershell -Command "& {Invoke-WebRequest -Uri '%url%' -OutFile '%DRIVERS_DIR%\%filename%'}"

REM Extract if it's a zip file
if "%filename:~-4%"==".zip" (
    echo Extracting %filename%...
    powershell -Command "& {Expand-Archive -Path '%DRIVERS_DIR%\%filename%' -DestinationPath '%DRIVERS_DIR%' -Force}"
    del "%DRIVERS_DIR%\%filename%"
)

echo ✅ %driver_name% downloaded successfully
goto :eof

REM Function to setup ChromeDriver
:setup_chromedriver
echo Setting up ChromeDriver...

set filename=chromedriver_win64.zip
set url=https://chromedriver.storage.googleapis.com/%CHROME_VERSION%/%filename%

call :download_driver "ChromeDriver" "%CHROME_VERSION%" "%url%" "%filename%"

REM Find the actual chromedriver executable
for /r "%DRIVERS_DIR%" %%f in (chromedriver.exe) do (
    set chromedriver_path=%%f
    goto :found_chrome
)
:found_chrome

if defined chromedriver_path (
    echo ChromeDriver installed at: !chromedriver_path!
    echo To use this driver, add to your PATH:
    echo set PATH="%%~dp!chromedriver_path!;%%PATH%%"
)
goto :eof

REM Function to setup GeckoDriver
:setup_geckodriver
echo Setting up GeckoDriver (Firefox)...

set filename=geckodriver-v%FIREFOX_VERSION%-win64.zip
set url=https://github.com/mozilla/geckodriver/releases/download/v%FIREFOX_VERSION%/%filename%

call :download_driver "GeckoDriver" "%FIREFOX_VERSION%" "%url%" "%filename%"

REM Find the actual geckodriver executable
for /r "%DRIVERS_DIR%" %%f in (geckodriver.exe) do (
    set geckodriver_path=%%f
    goto :found_gecko
)
:found_gecko

if defined geckodriver_path (
    echo GeckoDriver installed at: !geckodriver_path!
    echo To use this driver, add to your PATH:
    echo set PATH="%%~dp!geckodriver_path!;%%PATH%%"
)
goto :eof

REM Function to setup EdgeDriver
:setup_edgedriver
echo Setting up EdgeDriver...

set filename=edgedriver_win64.zip
set url=https://msedgedriver.azureedge.net/%EDGE_VERSION%/%filename%

call :download_driver "EdgeDriver" "%EDGE_VERSION%" "%url%" "%filename%"

REM Find the actual msedgedriver executable
for /r "%DRIVERS_DIR%" %%f in (msedgedriver.exe) do (
    set edgedriver_path=%%f
    goto :found_edge
)
:found_edge

if defined edgedriver_path (
    echo EdgeDriver installed at: !edgedriver_path!
    echo To use this driver, add to your PATH:
    echo set PATH="%%~dp!edgedriver_path!;%%PATH%%"
)
goto :eof

REM Function to create cache directory
:setup_cache
echo Setting up driver cache directory...
if not exist "%CACHE_DIR%\chrome" mkdir "%CACHE_DIR%\chrome"
if not exist "%CACHE_DIR%\firefox" mkdir "%CACHE_DIR%\firefox"
if not exist "%CACHE_DIR%\edge" mkdir "%CACHE_DIR%\edge"
echo ✅ Cache directory created at: %CACHE_DIR%
goto :eof

REM Function to update configuration
:update_config
echo Updating configuration...

set config_file=.\automation-framework\src\main\resources\config.properties

if exist "%config_file%" (
    REM Backup original config
    copy "%config_file%" "%config_file%.backup"
    
    REM Update driver paths
    if defined chromedriver_path (
        powershell -Command "& {(Get-Content '%config_file%') -replace 'webdriver.chrome.path=', 'webdriver.chrome.path=%chromedriver_path%' | Set-Content '%config_file%'}"
    )
    
    if defined geckodriver_path (
        powershell -Command "& {(Get-Content '%config_file%') -replace 'webdriver.firefox.path=', 'webdriver.firefox.path=%geckodriver_path%' | Set-Content '%config_file%'}"
    )
    
    if defined edgedriver_path (
        powershell -Command "& {(Get-Content '%config_file%') -replace 'webdriver.edge.path=', 'webdriver.edge.path=%edgedriver_path%' | Set-Content '%config_file%'}"
    )
    
    echo ✅ Configuration updated
) else (
    echo ⚠️  Configuration file not found: %config_file%
)
goto :eof

REM Function to test drivers
:test_drivers
echo Testing drivers...

REM Test ChromeDriver
if defined chromedriver_path (
    echo Testing ChromeDriver...
    "%chromedriver_path%" --version
    if !errorlevel! equ 0 (
        echo ✅ ChromeDriver working
    ) else (
        echo ❌ ChromeDriver test failed
    )
)

REM Test GeckoDriver
if defined geckodriver_path (
    echo Testing GeckoDriver...
    "%geckodriver_path%" --version
    if !errorlevel! equ 0 (
        echo ✅ GeckoDriver working
    ) else (
        echo ❌ GeckoDriver test failed
    )
)

REM Test EdgeDriver
if defined edgedriver_path (
    echo Testing EdgeDriver...
    "%edgedriver_path%" --version
    if !errorlevel! equ 0 (
        echo ✅ EdgeDriver working
    ) else (
        echo ❌ EdgeDriver test failed
    )
)
goto :eof

REM Main execution
:main
echo Starting driver setup...

REM Create directories
call :setup_cache

REM Setup drivers based on user choice
echo Which drivers would you like to install?
echo 1) ChromeDriver only
echo 2) GeckoDriver only
echo 3) EdgeDriver only
echo 4) All drivers
echo 5) Skip driver installation

set /p choice="Enter your choice (1-5): "

if "%choice%"=="1" (
    call :setup_chromedriver
) else if "%choice%"=="2" (
    call :setup_geckodriver
) else if "%choice%"=="3" (
    call :setup_edgedriver
) else if "%choice%"=="4" (
    call :setup_chromedriver
    call :setup_geckodriver
    call :setup_edgedriver
) else if "%choice%"=="5" (
    echo Skipping driver installation
) else (
    echo Invalid choice. Exiting.
    exit /b 1
)

REM Update configuration
call :update_config

REM Test drivers
call :test_drivers

echo.
echo 🎉 Driver setup completed!
echo.
echo Next steps:
echo 1. Run the tests: mvn test
echo 2. Check the logs for driver initialization
echo 3. If issues persist, check the driver paths in config.properties
echo.
echo Driver locations:
if exist "%DRIVERS_DIR%" (
    dir /s /b "%DRIVERS_DIR%\*.exe"
)

pause
goto :eof

REM Run main function
call :main
