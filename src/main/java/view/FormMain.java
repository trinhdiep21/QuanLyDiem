package view;

import javax.swing.*;
import java.awt.*;

public class FormMain extends javax.swing.JFrame {

    public FormMain() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        pnlButtons = new javax.swing.JPanel();
        btnQuanLySinhVien = new javax.swing.JButton();
        btnQuanLyMonHoc = new javax.swing.JButton();
        btnQuanLyLop = new javax.swing.JButton();
        btnQuanLyHocKy = new javax.swing.JButton();
        btnQuanLyLopHP = new javax.swing.JButton();
        btnNhapDiem = new javax.swing.JButton();
        btnDiemDanh = new javax.swing.JButton();
        btnTinhGPA = new javax.swing.JButton();
        pnlFooter = new javax.swing.JPanel();
        lblFooter = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HỆ THỐNG QUẢN LÝ ĐIỂM & SINH VIÊN");
        setPreferredSize(new java.awt.Dimension(800, 550));

        // Header
        pnlHeader.setBackground(new java.awt.Color(25, 118, 210));
        pnlHeader.setPreferredSize(new java.awt.Dimension(800, 70));
        pnlHeader.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 18));

        lblTitle.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 22));
        lblTitle.setForeground(java.awt.Color.WHITE);
        lblTitle.setText("HỆ THỐNG QUẢN LÝ ĐIỂM & SINH VIÊN");
        pnlHeader.add(lblTitle);

        getContentPane().add(pnlHeader, java.awt.BorderLayout.NORTH);

        // Buttons Grid
        pnlButtons.setLayout(new java.awt.GridLayout(4, 2, 15, 15));
        pnlButtons.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 30));
        pnlButtons.setBackground(java.awt.Color.WHITE);

        btnQuanLySinhVien.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14));
        btnQuanLySinhVien.setText("Quản lý Sinh Viên");
        btnQuanLySinhVien.setBackground(new java.awt.Color(245, 247, 250));
        btnQuanLySinhVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuanLySinhVienActionPerformed(evt);
            }
        });
        pnlButtons.add(btnQuanLySinhVien);

        btnQuanLyMonHoc.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14));
        btnQuanLyMonHoc.setText("Quản lý Môn Học");
        btnQuanLyMonHoc.setBackground(new java.awt.Color(245, 247, 250));
        btnQuanLyMonHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuanLyMonHocActionPerformed(evt);
            }
        });
        pnlButtons.add(btnQuanLyMonHoc);

        btnQuanLyLop.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14));
        btnQuanLyLop.setText("Quản lý Lớp Sinh Hoạt");
        btnQuanLyLop.setBackground(new java.awt.Color(245, 247, 250));
        btnQuanLyLop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuanLyLopActionPerformed(evt);
            }
        });
        pnlButtons.add(btnQuanLyLop);

        btnQuanLyHocKy.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14));
        btnQuanLyHocKy.setText("Quản lý Học Kỳ & Đóng Sổ");
        btnQuanLyHocKy.setBackground(new java.awt.Color(245, 247, 250));
        btnQuanLyHocKy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuanLyHocKyActionPerformed(evt);
            }
        });
        pnlButtons.add(btnQuanLyHocKy);

        btnQuanLyLopHP.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14));
        btnQuanLyLopHP.setText("Quản lý Lớp Học Phần");
        btnQuanLyLopHP.setBackground(new java.awt.Color(245, 247, 250));
        btnQuanLyLopHP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuanLyLopHPActionPerformed(evt);
            }
        });
        pnlButtons.add(btnQuanLyLopHP);

        btnNhapDiem.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14));
        btnNhapDiem.setText("Nhập Điểm & Import/Export Excel");
        btnNhapDiem.setBackground(new java.awt.Color(245, 247, 250));
        btnNhapDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNhapDiemActionPerformed(evt);
            }
        });
        pnlButtons.add(btnNhapDiem);

        btnDiemDanh.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14));
        btnDiemDanh.setText("Điểm Danh & Quét CẤM THI");
        btnDiemDanh.setBackground(new java.awt.Color(245, 247, 250));
        btnDiemDanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDiemDanhActionPerformed(evt);
            }
        });
        pnlButtons.add(btnDiemDanh);

        btnTinhGPA.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14));
        btnTinhGPA.setText("Tính GPA & Xét Tốt Nghiệp");
        btnTinhGPA.setBackground(new java.awt.Color(245, 247, 250));
        btnTinhGPA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTinhGPAActionPerformed(evt);
            }
        });
        pnlButtons.add(btnTinhGPA);

        getContentPane().add(pnlButtons, java.awt.BorderLayout.CENTER);

        // Footer
        pnlFooter.setBackground(new java.awt.Color(240, 240, 240));
        pnlFooter.setPreferredSize(new java.awt.Dimension(800, 35));
        pnlFooter.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 8));

        lblFooter.setFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
        lblFooter.setText("Bài tập lớn Java Swing & MySQL - Phiên bản NetBeans JFrame");
        pnlFooter.add(lblFooter);

        getContentPane().add(pnlFooter, java.awt.BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>

    private void btnQuanLySinhVienActionPerformed(java.awt.event.ActionEvent evt) {
        new FormSinhVien().setVisible(true);
    }

    private void btnQuanLyMonHocActionPerformed(java.awt.event.ActionEvent evt) {
        new FormMonHoc().setVisible(true);
    }

    private void btnQuanLyLopActionPerformed(java.awt.event.ActionEvent evt) {
        new FormLopHoc().setVisible(true);
    }

    private void btnQuanLyHocKyActionPerformed(java.awt.event.ActionEvent evt) {
        new FormHocKy().setVisible(true);
    }

    private void btnQuanLyLopHPActionPerformed(java.awt.event.ActionEvent evt) {
        new FormLopHocPhan().setVisible(true);
    }

    private void btnNhapDiemActionPerformed(java.awt.event.ActionEvent evt) {
        new FormNhapDiem().setVisible(true);
    }

    private void btnDiemDanhActionPerformed(java.awt.event.ActionEvent evt) {
        new FormDiemDanh().setVisible(true);
    }

    private void btnTinhGPAActionPerformed(java.awt.event.ActionEvent evt) {
        new FormTotNghiep().setVisible(true);
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
        }
        javax.swing.SwingUtilities.invokeLater(() -> new FormMain().setVisible(true));
    }

    // Variables declaration - do not modify
    private javax.swing.JButton btnDiemDanh;
    private javax.swing.JButton btnNhapDiem;
    private javax.swing.JButton btnQuanLyHocKy;
    private javax.swing.JButton btnQuanLyLop;
    private javax.swing.JButton btnQuanLyLopHP;
    private javax.swing.JButton btnQuanLyMonHoc;
    private javax.swing.JButton btnQuanLySinhVien;
    private javax.swing.JButton btnTinhGPA;
    private javax.swing.JLabel lblFooter;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration
}
