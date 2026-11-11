@echo off
chcp 65001 >nul
echo =======================================================
echo   IMPORT DATABASE MYSQL CHO PHẦN MỀM QUẢN LÝ ĐIỂM
echo =======================================================
echo.
echo Đang kiểm tra XAMPP MySQL...

set MYSQL_BIN=C:\xampp\mysql\bin\mysql.exe

if not exist "%MYSQL_BIN%" (
    echo [LỖI] Không tìm thấy MySQL tại C:\xampp\mysql\bin\mysql.exe!
    pause
    exit /b 1
)

echo Đang import file database.sql vào MySQL...
"%MYSQL_BIN%" -u root --default-character-set=utf8mb4 < database.sql

if %errorlevel% equ 0 (
    echo.
    echo =======================================================
    echo   [THÀNH CÔNG] Đã tạo database `quanlydiem` và nạp dữ liệu mẫu!
    echo =======================================================
) else (
    echo.
    echo [LỖI] Import thất bại! Hãy chắc chắn bạn đã:
    echo 1. Mở XAMPP Control Panel và ấn "Start" ở mục MySQL.
    echo 2. Nếu tài khoản root có mật khẩu, hãy nhập bằng lệnh:
    echo    C:\xampp\mysql\bin\mysql.exe -u root -p ^< database.sql
)

echo.
pause
