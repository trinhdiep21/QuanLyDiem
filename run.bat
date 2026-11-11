@echo off
chcp 65001 >nul
echo =======================================================
echo   PHẦN MỀM QUẢN LÝ ĐIỂM & HỌC SINH - SINH VIÊN
echo =======================================================
echo.

set JAVA_HOME=C:\Program Files\Java\jdk-22
set PATH=%JAVA_HOME%\bin;%PATH%

REM Kiem tra file JAR
if not exist "target\QuanLyDiemSinhVien-jar-with-dependencies.jar" (
    echo [THÔNG BÁO] Chưa có file JAR, đang biên dịch dự án bằng Maven...
    call "C:\Program Files\NetBeans-22\netbeans\java\maven\bin\mvn.cmd" package -DskipTests
)

echo.
echo [1] Đang khởi động ứng dụng Java Swing...
echo (Lưu ý: Hãy chắc chắn XAMPP MySQL đã được BẬT - Start MySQL trong XAMPP Control Panel)
echo.

"%JAVA_HOME%\bin\java.exe" -jar target\QuanLyDiemSinhVien-jar-with-dependencies.jar

pause
