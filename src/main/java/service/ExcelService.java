package service;

import dao.DiemDAO;
import dao.LopHocPhanDAO;
import dao.SinhVienDAO;
import model.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.*;

/**
 * Service Import/Export bảng điểm qua file Excel (Apache POI)
 */
public class ExcelService {

    private DiemDAO diemDAO;
    private SinhVienDAO svDAO;
    private LopHocPhanDAO lhpDAO;
    private DiemService diemService;

    public ExcelService() {
        diemDAO = new DiemDAO();
        svDAO = new SinhVienDAO();
        lhpDAO = new LopHocPhanDAO();
        diemService = new DiemService();
    }

    // =================== EXPORT ===================

    /**
     * Xuất bảng điểm lớp học phần ra Excel
     */
    public void exportBangDiem(String maLHP, String filePath) throws IOException {
        LopHocPhan lhp = lhpDAO.getById(maLHP);
        if (lhp == null) throw new IOException("Không tìm thấy lớp học phần: " + maLHP);

        List<SinhVien> svList = svDAO.getSinhVienByLHP(maLHP);
        List<CotDiem> cotDiems = diemDAO.getCotDiemByLHP(maLHP);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Bang_Diem");

        // Style
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle titleStyle = createTitleStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        CellStyle numberStyle = createNumberStyle(workbook);

        int rowNum = 0;

        // Tiêu đề
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("BẢNG ĐIỂM MÔN HỌC");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, cotDiems.size() + 4));

        Row infoRow1 = sheet.createRow(rowNum++);
        infoRow1.createCell(0).setCellValue("Môn học: " + lhp.getTenMH());
        infoRow1.createCell(3).setCellValue("Lớp HP: " + maLHP);

        Row infoRow2 = sheet.createRow(rowNum++);
        infoRow2.createCell(0).setCellValue("Học kỳ: " + lhp.getTenHK());
        infoRow2.createCell(3).setCellValue("Giáo viên: " + lhp.getGiaoVien());

        rowNum++; // Dòng trống

        // Header cột
        Row headerRow = sheet.createRow(rowNum++);
        int colIdx = 0;
        String[] fixedHeaders = {"STT", "Mã SV", "Họ và tên"};
        for (String h : fixedHeaders) {
            Cell cell = headerRow.createCell(colIdx++);
            cell.setCellValue(h);
            cell.setCellStyle(headerStyle);
        }
        for (CotDiem cd : cotDiems) {
            Cell cell = headerRow.createCell(colIdx++);
            cell.setCellValue(cd.getTenCot() + "\n(" + (int) cd.getTrongSo() + "%)");
            cell.setCellStyle(headerStyle);
        }
        Cell diemTBCell = headerRow.createCell(colIdx++);
        diemTBCell.setCellValue("Điểm TB");
        diemTBCell.setCellStyle(headerStyle);
        Cell diemChuCell = headerRow.createCell(colIdx++);
        diemChuCell.setCellValue("Điểm chữ");
        diemChuCell.setCellStyle(headerStyle);
        Cell gpa4Cell = headerRow.createCell(colIdx++);
        gpa4Cell.setCellValue("Hệ 4");
        gpa4Cell.setCellStyle(headerStyle);

        // Dữ liệu sinh viên
        int stt = 1;
        for (SinhVien sv : svList) {
            Row dataRow = sheet.createRow(rowNum++);
            colIdx = 0;

            Cell sttCell = dataRow.createCell(colIdx++);
            sttCell.setCellValue(stt++);
            sttCell.setCellStyle(dataStyle);

            Cell maSVCell = dataRow.createCell(colIdx++);
            maSVCell.setCellValue(sv.getMaSV());
            maSVCell.setCellStyle(dataStyle);

            Cell tenCell = dataRow.createCell(colIdx++);
            tenCell.setCellValue(sv.getHoTen());
            tenCell.setCellStyle(dataStyle);

            for (CotDiem cd : cotDiems) {
                DiemThanhPhan dtp = diemDAO.getDiemThanhPhan(sv.getMaSV(), cd.getId());
                Cell diemCell = dataRow.createCell(colIdx++);
                if (dtp != null) {
                    diemCell.setCellValue(dtp.getDiem());
                } else {
                    diemCell.setCellValue("");
                }
                diemCell.setCellStyle(numberStyle);
            }

            double diemTB = diemService.tinhDiemTrungBinh(sv.getMaSV(), maLHP);
            Cell tbCell = dataRow.createCell(colIdx++);
            tbCell.setCellValue(diemTB);
            tbCell.setCellStyle(numberStyle);

            Cell dcCell = dataRow.createCell(colIdx++);
            dcCell.setCellValue(diemService.quyDoiDiemChu(diemTB));
            dcCell.setCellStyle(dataStyle);

            Cell h4Cell = dataRow.createCell(colIdx++);
            h4Cell.setCellValue(diemService.quyDoiHe4(diemTB));
            h4Cell.setCellStyle(numberStyle);
        }

        // Auto width
        for (int i = 0; i < colIdx; i++) {
            sheet.autoSizeColumn(i);
        }

        // Ghi file
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    // =================== IMPORT ===================

    /**
     * Import điểm thành phần từ Excel
     * Format: Cột A = Mã SV, Cột B+ = Điểm theo thứ tự cột điểm
     */
    public int importBangDiem(String maLHP, String filePath) throws IOException {
        List<CotDiem> cotDiems = diemDAO.getCotDiemByLHP(maLHP);
        int count = 0;

        try (FileInputStream fis = new FileInputStream(filePath);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            boolean dataStarted = false;
            int dataRowStart = -1;

            // Tìm dòng bắt đầu dữ liệu (dòng có "Mã SV" hoặc "maSV")
            for (Row row : sheet) {
                Cell firstCell = row.getCell(0);
                if (firstCell != null) {
                    String val = getCellStringValue(firstCell).toLowerCase();
                    if (val.contains("stt") || val.contains("mã sv") || val.contains("masv")) {
                        dataRowStart = row.getRowNum() + 1;
                        dataStarted = true;
                        break;
                    }
                }
            }

            if (!dataStarted) dataRowStart = 5; // Mặc định bỏ qua 5 dòng đầu (header)

            for (int i = dataRowStart; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell maSVCell = row.getCell(1); // Cột B = Mã SV
                if (maSVCell == null) continue;
                String maSV = getCellStringValue(maSVCell).trim();
                if (maSV.isEmpty()) continue;

                for (int j = 0; j < cotDiems.size(); j++) {
                    Cell diemCell = row.getCell(3 + j); // Cột D trở đi = điểm
                    if (diemCell != null && diemCell.getCellType() == CellType.NUMERIC) {
                        double diem = diemCell.getNumericCellValue();
                        if (diem >= 0 && diem <= 10) {
                            diemDAO.upsertDiemThanhPhan(maSV, cotDiems.get(j).getId(), diem);
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    /**
     * Template Excel trống để giáo viên nhập điểm
     */
    public void exportTemplate(String maLHP, String filePath) throws IOException {
        LopHocPhan lhp = lhpDAO.getById(maLHP);
        if (lhp == null) throw new IOException("Không tìm thấy lớp học phần: " + maLHP);

        List<SinhVien> svList = svDAO.getSinhVienByLHP(maLHP);
        List<CotDiem> cotDiems = diemDAO.getCotDiemByLHP(maLHP);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Nhap_Diem");
        CellStyle headerStyle = createHeaderStyle(workbook);

        int rowNum = 0;

        // Header thông tin
        Row r0 = sheet.createRow(rowNum++);
        r0.createCell(0).setCellValue("Lớp HP: " + maLHP + " | Môn: " + lhp.getTenMH() + " | HK: " + lhp.getTenHK());

        rowNum++;

        // Header cột
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("STT");
        headerRow.createCell(1).setCellValue("Mã SV");
        headerRow.createCell(2).setCellValue("Họ và tên");
        for (int i = 0; i < cotDiems.size(); i++) {
            CotDiem cd = cotDiems.get(i);
            Cell c = headerRow.createCell(3 + i);
            c.setCellValue(cd.getTenCot() + " (" + (int) cd.getTrongSo() + "%)");
            c.setCellStyle(headerStyle);
        }

        // Dòng sinh viên (ô điểm để trống)
        int stt = 1;
        for (SinhVien sv : svList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(stt++);
            row.createCell(1).setCellValue(sv.getMaSV());
            row.createCell(2).setCellValue(sv.getHoTen());
        }

        for (int i = 0; i < 3 + cotDiems.size(); i++) sheet.autoSizeColumn(i);

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    // =================== STYLE HELPERS ===================

    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf((long) cell.getNumericCellValue());
            default: return "";
        }
    }

    private CellStyle createHeaderStyle(XSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        return style;
    }

    private CellStyle createTitleStyle(XSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createDataStyle(XSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createNumberStyle(XSSFWorkbook wb) {
        CellStyle style = createDataStyle(wb);
        style.setAlignment(HorizontalAlignment.CENTER);
        DataFormat format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("0.00"));
        return style;
    }
}
