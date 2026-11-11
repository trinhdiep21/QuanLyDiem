-- ============================================
-- Database: quanlydiem
-- Hệ thống Quản lý Điểm & Học sinh - Sinh viên
-- ============================================

CREATE DATABASE IF NOT EXISTS quanlydiem CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE quanlydiem;

-- Bảng Khóa học
CREATE TABLE IF NOT EXISTS KhoaHoc (
    maKhoa VARCHAR(10) PRIMARY KEY,
    tenKhoa VARCHAR(100) NOT NULL,
    namBatDau INT NOT NULL,
    namKetThuc INT,
    moTa TEXT
);

-- Bảng Lớp học (lớp sinh hoạt)
CREATE TABLE IF NOT EXISTS LopHoc (
    maLop VARCHAR(10) PRIMARY KEY,
    tenLop VARCHAR(100) NOT NULL,
    maKhoa VARCHAR(10),
    siSo INT DEFAULT 0,
    FOREIGN KEY (maKhoa) REFERENCES KhoaHoc(maKhoa)
);

-- Bảng Sinh viên
CREATE TABLE IF NOT EXISTS SinhVien (
    maSV VARCHAR(15) PRIMARY KEY,
    hoTen VARCHAR(100) NOT NULL,
    ngaySinh DATE,
    gioiTinh ENUM('Nam','Nu','Khac') DEFAULT 'Nam',
    diaChi TEXT,
    email VARCHAR(100),
    soDienThoai VARCHAR(15),
    maLop VARCHAR(10),
    trangThai ENUM('DangHoc','TotNghiep','NghiHoc','ThoiHoc') DEFAULT 'DangHoc',
    FOREIGN KEY (maLop) REFERENCES LopHoc(maLop)
);

-- Bảng Môn học
CREATE TABLE IF NOT EXISTS MonHoc (
    maMH VARCHAR(10) PRIMARY KEY,
    tenMH VARCHAR(200) NOT NULL,
    soTinChi INT NOT NULL DEFAULT 3,
    loai ENUM('BatBuoc','TuChon') DEFAULT 'BatBuoc',
    moTa TEXT
);

-- Bảng Khung chương trình đào tạo
CREATE TABLE IF NOT EXISTS KhungCTDT (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maKhoa VARCHAR(10),
    maMH VARCHAR(10),
    loai ENUM('BatBuoc','TuChon') DEFAULT 'BatBuoc',
    FOREIGN KEY (maKhoa) REFERENCES KhoaHoc(maKhoa),
    FOREIGN KEY (maMH) REFERENCES MonHoc(maMH),
    UNIQUE KEY uq_khung (maKhoa, maMH)
);

-- Bảng Học kỳ / Kỳ học
CREATE TABLE IF NOT EXISTS HocKy (
    maHK VARCHAR(10) PRIMARY KEY,
    tenHK VARCHAR(100) NOT NULL,
    namHoc VARCHAR(20) NOT NULL,
    kyHoc INT NOT NULL,
    ngayBatDau DATE,
    ngayKetThuc DATE,
    trangThai ENUM('DangHoc','DaDong') DEFAULT 'DangHoc'
);

-- Bảng Lớp học phần (class per subject per semester)
CREATE TABLE IF NOT EXISTS LopHocPhan (
    maLHP VARCHAR(15) PRIMARY KEY,
    maMH VARCHAR(10),
    maHK VARCHAR(10),
    giaoVien VARCHAR(100),
    soTietLyThuyet INT DEFAULT 0,
    soTietThucHanh INT DEFAULT 0,
    tongSoBuoi INT DEFAULT 0,
    soPhepNghi INT DEFAULT 3,
    FOREIGN KEY (maMH) REFERENCES MonHoc(maMH),
    FOREIGN KEY (maHK) REFERENCES HocKy(maHK)
);

-- Bảng Đăng ký học phần (sinh viên - lớp học phần)
CREATE TABLE IF NOT EXISTS DangKyHP (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maSV VARCHAR(15),
    maLHP VARCHAR(15),
    trangThai ENUM('DangHoc','CamThi','RutMon') DEFAULT 'DangHoc',
    FOREIGN KEY (maSV) REFERENCES SinhVien(maSV),
    FOREIGN KEY (maLHP) REFERENCES LopHocPhan(maLHP),
    UNIQUE KEY uq_dkHP (maSV, maLHP)
);

-- Bảng Cột điểm (định nghĩa cấu trúc điểm của lớp học phần)
CREATE TABLE IF NOT EXISTS CotDiem (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maLHP VARCHAR(15),
    tenCot VARCHAR(50) NOT NULL,
    trongSo DECIMAL(5,2) NOT NULL DEFAULT 0,
    loai ENUM('ThanhPhan','CuoiKy') DEFAULT 'ThanhPhan',
    FOREIGN KEY (maLHP) REFERENCES LopHocPhan(maLHP)
);

-- Bảng Điểm thành phần
CREATE TABLE IF NOT EXISTS DiemThanhPhan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maSV VARCHAR(15),
    idCotDiem INT,
    diem DECIMAL(5,2),
    ghiChu TEXT,
    daKhoa TINYINT(1) DEFAULT 0,
    FOREIGN KEY (maSV) REFERENCES SinhVien(maSV),
    FOREIGN KEY (idCotDiem) REFERENCES CotDiem(id),
    UNIQUE KEY uq_diem (maSV, idCotDiem)
);

-- Bảng Điểm môn học (điểm tổng kết)
CREATE TABLE IF NOT EXISTS DiemMonHoc (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maSV VARCHAR(15),
    maLHP VARCHAR(15),
    diemTrungBinh DECIMAL(5,2),
    diemHe4 DECIMAL(4,2),
    diemChu VARCHAR(5),
    ketQua ENUM('Dat','KhongDat','CamThi') DEFAULT 'Dat',
    FOREIGN KEY (maSV) REFERENCES SinhVien(maSV),
    FOREIGN KEY (maLHP) REFERENCES LopHocPhan(maLHP),
    UNIQUE KEY uq_diemMH (maSV, maLHP)
);

-- Bảng Điểm danh
CREATE TABLE IF NOT EXISTS DiemDanh (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maSV VARCHAR(15),
    maLHP VARCHAR(15),
    ngayHoc DATE NOT NULL,
    buoiHoc INT DEFAULT 1,
    trangThai ENUM('CoMat','Vang','VangPhep') DEFAULT 'CoMat',
    FOREIGN KEY (maSV) REFERENCES SinhVien(maSV),
    FOREIGN KEY (maLHP) REFERENCES LopHocPhan(maLHP)
);

-- ============================================
-- DỮ LIỆU MẪU
-- ============================================

INSERT INTO KhoaHoc VALUES
('K2021', 'Khóa 2021', 2021, 2025, 'Khóa đại học 2021'),
('K2022', 'Khóa 2022', 2022, 2026, 'Khóa đại học 2022'),
('K2023', 'Khóa 2023', 2023, 2027, 'Khóa đại học 2023');

INSERT INTO LopHoc VALUES
('CNTT01', 'CNTT K2021 - 01', 'K2021', 40),
('CNTT02', 'CNTT K2021 - 02', 'K2021', 38),
('CNTT03', 'CNTT K2022 - 01', 'K2022', 42);

INSERT INTO SinhVien VALUES
('SV2021001', 'Nguyễn Văn An', '2003-05-10', 'Nam', 'Hà Nội', 'an@email.com', '0901234567', 'CNTT01', 'DangHoc'),
('SV2021002', 'Trần Thị Bình', '2003-08-15', 'Nu', 'HCM', 'binh@email.com', '0902345678', 'CNTT01', 'DangHoc'),
('SV2021003', 'Lê Văn Cường', '2003-02-20', 'Nam', 'Đà Nẵng', 'cuong@email.com', '0903456789', 'CNTT01', 'DangHoc'),
('SV2021004', 'Phạm Thị Dung', '2003-11-25', 'Nu', 'Hải Phòng', 'dung@email.com', '0904567890', 'CNTT02', 'DangHoc'),
('SV2022001', 'Hoàng Văn Em', '2004-03-14', 'Nam', 'Huế', 'em@email.com', '0905678901', 'CNTT03', 'DangHoc');

INSERT INTO MonHoc VALUES
('LTCT', 'Lập trình căn bản', 3, 'BatBuoc', 'Môn lập trình C/C++'),
('CTDL', 'Cấu trúc dữ liệu', 3, 'BatBuoc', 'CTDL và giải thuật'),
('CSDL', 'Cơ sở dữ liệu', 3, 'BatBuoc', 'Hệ quản trị CSDL'),
('MANG', 'Mạng máy tính', 3, 'BatBuoc', 'Kiến thức mạng'),
('AI01', 'Trí tuệ nhân tạo', 3, 'TuChon', 'Nhập môn AI');

INSERT INTO KhungCTDT (maKhoa, maMH, loai) VALUES
('K2021', 'LTCT', 'BatBuoc'),
('K2021', 'CTDL', 'BatBuoc'),
('K2021', 'CSDL', 'BatBuoc'),
('K2021', 'MANG', 'BatBuoc'),
('K2021', 'AI01', 'TuChon');

INSERT INTO HocKy VALUES
('HK1-2324', 'Học kỳ 1 2023-2024', '2023-2024', 1, '2023-09-01', '2024-01-15', 'DaDong'),
('HK2-2324', 'Học kỳ 2 2023-2024', '2023-2024', 2, '2024-02-01', '2024-06-30', 'DaDong'),
('HK1-2425', 'Học kỳ 1 2024-2025', '2024-2025', 1, '2024-09-01', '2025-01-15', 'DangHoc');

INSERT INTO LopHocPhan VALUES
('LTCT-01-HK1', 'LTCT', 'HK1-2324', 'GV. Nguyễn Minh Tuấn', 30, 15, 15, 3),
('CTDL-01-HK2', 'CTDL', 'HK2-2324', 'GV. Trần Thị Lan', 30, 15, 15, 3),
('CSDL-01-HK1', 'CSDL', 'HK1-2425', 'GV. Lê Văn Khoa', 30, 15, 15, 3);

INSERT INTO DangKyHP (maSV, maLHP) VALUES
('SV2021001', 'LTCT-01-HK1'),
('SV2021002', 'LTCT-01-HK1'),
('SV2021003', 'LTCT-01-HK1'),
('SV2021001', 'CTDL-01-HK2'),
('SV2021002', 'CTDL-01-HK2'),
('SV2021001', 'CSDL-01-HK1'),
('SV2021002', 'CSDL-01-HK1'),
('SV2021003', 'CSDL-01-HK1');

INSERT INTO CotDiem (maLHP, tenCot, trongSo, loai) VALUES
('LTCT-01-HK1', 'Chuyên cần', 10, 'ThanhPhan'),
('LTCT-01-HK1', 'Giữa kỳ', 30, 'ThanhPhan'),
('LTCT-01-HK1', 'Cuối kỳ', 60, 'CuoiKy'),
('CTDL-01-HK2', 'Chuyên cần', 10, 'ThanhPhan'),
('CTDL-01-HK2', 'Thực hành', 30, 'ThanhPhan'),
('CTDL-01-HK2', 'Cuối kỳ', 60, 'CuoiKy'),
('CSDL-01-HK1', 'Chuyên cần', 10, 'ThanhPhan'),
('CSDL-01-HK1', 'Giữa kỳ', 30, 'ThanhPhan'),
('CSDL-01-HK1', 'Cuối kỳ', 60, 'CuoiKy');
