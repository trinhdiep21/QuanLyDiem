package view;

import dao.HocKyDAO;
import model.HocKy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FormHocKy extends JFrame {

    private JTextField txtMaHK;
    private JTextField txtTenHK;
    private JTextField txtNamHoc;
    private JSpinner spKyHoc;
    private JComboBox<String> cboTrangThai;

    private JButton btnThem;
    private JButton btnSua;
    private JButton btnKhoaKy;
    private JButton btnXoa;
    private JButton btnLamMoi;

    private JTable tblHocKy;
    private DefaultTableModel modelTable;

    private HocKyDAO hkDAO;

    public FormHocKy() {
        hkDAO = new HocKyDAO();
        initComponents();
        fillTable(hkDAO.getAll());
    }

    private void initComponents() {
        setTitle("Quản Lý Học Kỳ & Khóa Sổ");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 560);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        JLabel lblTitle = new JLabel("QUẢN LÝ HỌC KỲ & ĐÓNG MỞ SỔ ĐIỂM", JLabel.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setForeground(new Color(25, 118, 210));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new BorderLayout(5, 5));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        JPanel pnlForm = new JPanel(new GridLayout(3, 4, 10, 10));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin học kỳ"));

        pnlForm.add(new JLabel("Mã học kỳ:"));
        txtMaHK = new JTextField();
        pnlForm.add(txtMaHK);

        pnlForm.add(new JLabel("Tên học kỳ:"));
        txtTenHK = new JTextField();
        pnlForm.add(txtTenHK);

        pnlForm.add(new JLabel("Năm học:"));
        txtNamHoc = new JTextField("2024-2025");
        pnlForm.add(txtNamHoc);

        pnlForm.add(new JLabel("Kỳ học (1, 2, 3):"));
        spKyHoc = new JSpinner(new SpinnerNumberModel(1, 1, 3, 1));
        pnlForm.add(spKyHoc);

        pnlForm.add(new JLabel("Trạng thái sổ điểm:"));
        cboTrangThai = new JComboBox<>(new String[]{"DangHoc", "DaDong"});
        pnlForm.add(cboTrangThai);
        pnlForm.add(new JLabel("")); // ô trống

        pnlCenter.add(pnlForm, BorderLayout.NORTH);

        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnThem = new JButton("Thêm");
        btnThem.setBackground(new Color(46, 125, 50));
        btnThem.setForeground(Color.WHITE);
        btnThem.addActionListener(e -> btnThemActionPerformed());

        btnSua = new JButton("Sửa");
        btnSua.setBackground(new Color(255, 152, 0));
        btnSua.setForeground(Color.WHITE);
        btnSua.addActionListener(e -> btnSuaActionPerformed());

        btnKhoaKy = new JButton("🔒 Đóng/Mở sổ kỳ này");
        btnKhoaKy.setBackground(new Color(142, 36, 170));
        btnKhoaKy.setForeground(Color.WHITE);
        btnKhoaKy.addActionListener(e -> btnKhoaKyActionPerformed());

        btnXoa = new JButton("Xóa");
        btnXoa.setBackground(new Color(211, 47, 47));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.addActionListener(e -> btnXoaActionPerformed());

        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.addActionListener(e -> clearForm());

        pnlActions.add(btnThem);
        pnlActions.add(btnSua);
        pnlActions.add(btnKhoaKy);
        pnlActions.add(btnXoa);
        pnlActions.add(btnLamMoi);
        pnlCenter.add(pnlActions, BorderLayout.SOUTH);

        add(pnlCenter, BorderLayout.CENTER);

        String[] columns = {"STT", "Mã HK", "Tên Học Kỳ", "Năm Học", "Kỳ", "Trạng Thái Sổ"};
        modelTable = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblHocKy = new JTable(modelTable);
        tblHocKy.setRowHeight(24);
        tblHocKy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tblHocKyMouseClicked();
            }
        });

        JScrollPane scroll = new JScrollPane(tblHocKy);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách học kỳ"));
        scroll.setPreferredSize(new Dimension(750, 260));
        add(scroll, BorderLayout.SOUTH);
    }

    private void fillTable(List<HocKy> list) {
        modelTable.setRowCount(0);
        int stt = 1;
        for (HocKy hk : list) {
            String tt = "DangHoc".equals(hk.getTrangThai()) ? "🔓 Đang mở (Cho nhập điểm)" : "🔒 ĐÃ ĐÓNG SỔ (Khóa)";
            modelTable.addRow(new Object[]{
                stt++, hk.getMaHK(), hk.getTenHK(), hk.getNamHoc(), hk.getKyHoc(), tt
            });
        }
    }

    private void tblHocKyMouseClicked() {
        int row = tblHocKy.getSelectedRow();
        if (row < 0) return;

        txtMaHK.setText(modelTable.getValueAt(row, 1).toString());
        txtMaHK.setEditable(false);
        txtTenHK.setText(modelTable.getValueAt(row, 2).toString());
        txtNamHoc.setText(modelTable.getValueAt(row, 3).toString());
        spKyHoc.setValue(Integer.parseInt(modelTable.getValueAt(row, 4).toString()));

        String tt = modelTable.getValueAt(row, 5).toString();
        cboTrangThai.setSelectedItem(tt.contains("ĐÃ ĐÓNG") ? "DaDong" : "DangHoc");
    }

    private void clearForm() {
        txtMaHK.setText("");
        txtMaHK.setEditable(true);
        txtTenHK.setText("");
        txtNamHoc.setText("2024-2025");
        spKyHoc.setValue(1);
        cboTrangThai.setSelectedIndex(0);
        fillTable(hkDAO.getAll());
    }

    private void btnThemActionPerformed() {
        String ma = txtMaHK.getText().trim();
        String ten = txtTenHK.getText().trim();
        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã và Tên học kỳ!");
            return;
        }

        HocKy hk = new HocKy(ma, ten, txtNamHoc.getText().trim(), (int) spKyHoc.getValue(), null, null, cboTrangThai.getSelectedItem().toString());
        if (hkDAO.insert(hk)) {
            JOptionPane.showMessageDialog(this, "Thêm học kỳ thành công!");
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!");
        }
    }

    private void btnSuaActionPerformed() {
        String ma = txtMaHK.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn học kỳ cần sửa!");
            return;
        }

        HocKy hk = new HocKy(ma, txtTenHK.getText().trim(), txtNamHoc.getText().trim(), (int) spKyHoc.getValue(), null, null, cboTrangThai.getSelectedItem().toString());
        if (hkDAO.update(hk)) {
            JOptionPane.showMessageDialog(this, "Cập nhật học kỳ thành công!");
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }

    private void btnKhoaKyActionPerformed() {
        String ma = txtMaHK.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn học kỳ trên bảng!");
            return;
        }

        List<HocKy> list = hkDAO.getAll();
        HocKy found = list.stream().filter(h -> h.getMaHK().equals(ma)).findFirst().orElse(null);
        if (found != null) {
            String newStatus = "DangHoc".equals(found.getTrangThai()) ? "DaDong" : "DangHoc";
            found.setTrangThai(newStatus);
            if (hkDAO.update(found)) {
                String msg = "DaDong".equals(newStatus) ? "ĐÃ ĐÓNG SỔ ĐIỂM (Khóa toàn bộ việc sửa điểm)" : "ĐÃ MỞ SỔ ĐIỂM (Cho phép giáo viên sửa điểm)";
                JOptionPane.showMessageDialog(this, msg);
                clearForm();
            }
        }
    }

    private void btnXoaActionPerformed() {
        String ma = txtMaHK.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn học kỳ cần xóa!");
            return;
        }

        int ok = JOptionPane.showConfirmDialog(this, "Xóa học kỳ " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            if (hkDAO.delete(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại! Học kỳ đang có lớp học phần mở.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormHocKy().setVisible(true));
    }
}
