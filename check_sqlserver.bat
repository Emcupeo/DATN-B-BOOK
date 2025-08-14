@echo off
echo Checking SQL Server connection...

REM Kiểm tra SQL Server có chạy không
sqlcmd -S localhost -U sa -P kienhuy -Q "SELECT @@VERSION" -t 30

if %ERRORLEVEL% EQU 0 (
    echo SQL Server is running and accessible
    echo.
    echo Checking database BBook...
    sqlcmd -S localhost -U sa -P kienhuy -Q "SELECT name FROM sys.databases WHERE name = 'BBook'" -t 30
) else (
    echo ERROR: Cannot connect to SQL Server
    echo Please check:
    echo 1. SQL Server is running
    echo 2. SQL Server is listening on port 1433
    echo 3. Username and password are correct
    echo 4. SQL Server allows SQL authentication
)

pause 