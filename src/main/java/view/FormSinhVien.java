package view;

import dao.LopHocDAO;
import dao.SinhVienDAO;
import model.LopHoc;
import model.SinhVien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Form Sinh Viên - Thiết kế chuẩn form cơ bản trong NetBeans
 */
public class FormSinhVien extends JFrame {

    private JTextField txtMaSV;
    private JTextField txtHoTen;
    private JTextField txtNgaySinh;
    private JTextField txtDiaChi;
    private JTextField txtEmail;
    private JTextField txtSDT;
    private JRadioButton rdoNam;
    private JRadioButton rdoNu;
    private ButtonGroup btgGioiTinh;
    private JComboBox<LopHoc> cboLop;
    private JComboBox<String> cboTrangThai;

    private JTextField txtTimKiem;
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JButton btnTimKiem;

    private JTable tblSinhVien;
    private DefaultTableModel modelTable;

    private SinhVienDAO svDAO;
    private LopHocDAO lopDAO;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    public FormSinhVien() {
        svDAO = new SinhVienDAO();
        lopDAO = new LopHocDAO();
        initComponents();
        loadCboLop();
        fillTable(svDAO.getAllSinhVien());
    }

    private void initComponents() {
        setTitle("Quản Lý Sinh Viên");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        // Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ THÔNG TIN SINH VIÊN", JLabel.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setForeground(new Color(25, 118, 210));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Panel Giữa: Gồm Form nhập liệu + Nút bấm
        JPanel pnlCenter = new JPanel(new BorderLayout(5, 5));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        // Panel Nhập liệu
        JPanel pnlForm = new JPanel(new GridLayout(5, 4, 10, 10));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết"));

        pnlForm.add(new JLabel("Mã SV:"));
        txtMaSV = new JTextField();
        pnlForm.add(txtMaSV);

        pnlForm.add(new JLabel("Họ và tên:"));
        txtHoTen = new JTextField();
        pnlForm.add(txtHoTen);

        pnlForm.add(new JLabel("Ngày sinh (dd/MM/yyyy):"));
        txtNgaySinh = new JTextField();
        pnlForm.add(txtNgaySinh);

        pnlForm.add(new JLabel("Giới tính:"));
        JPanel pnlGT = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        rdoNam = new JRadioButton("Nam", true);
        rdoNu = new JRadioButton("Nữ");
        btgGioiTinh = new ButtonGroup();
        btgGioiTinh.add(rdoNam);
        btgGioiTinh.add(rdoNu);
        pnlGT.add(rdoNam);
        pnlGT.add(rdoNu);
        pnlForm.add(pnlGT);

        pnlForm.add(new JLabel("Lớp sinh hoạt:"));
        cboLop = new JComboBox<>();
        pnlForm.add(cboLop);

        pnlForm.add(new JLabel("Số điện thoại:"));
        txtSDT = new JTextField();
        pnlForm.add(txtSDT);

        pnlForm.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        pnlForm.add(txtEmail);

        pnlForm.add(new JLabel("Trạng thái:"));
        cboTrangThai = new JComboBox<>(new String[]{"DangHoc", "TotNghiep", "NghiHoc", "ThoiHoc"});
        pnlForm.add(cboTrangThai);

        pnlForm.add(new JLabel("Địa chỉ:"));
        txtDiaChi = new JTextField();
        pnlForm.add(txtDiaChi);
        pnlForm.add(new JLabel("")); // ô trống
        pnlForm.add(new JLabel("")); // ô trống

        pnlCenter.add(pnlForm, BorderLayout.NORTH);

        // Panel Nút bấm & Tìm kiếm
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnThem = new JButton("Thêm");
        btnThem.setBackground(new Color(46, 125, 50));
        btnThem.setForeground(Color.WHITE);
        btnThem.addActionListener(e -> btnThemActionPerformed());

        btnSua = new JButton("Sửa");
        btnSua.setBackground(new Color(255, 152, 0));
        btnSua.setForeground(Color.WHITE);
        btnSua.addActionListener(e -> btnSuaActionPerformed());

        btnXoa = new JButton("Xóa");
        btnXoa.setBackground(new Color(211, 47, 47));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.addActionListener(e -> btnXoaActionPerformed());

        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.addActionListener(e -> clearForm());

        txtTimKiem = new JTextField(15);
        btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setBackground(new Color(30, 136, 229));
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.addActionListener(e -> btnTimKiemActionPerformed());

        pnlActions.add(btnThem);
        pnlActions.add(btnSua);
        pnlActions.add(btnXoa);
        pnlActions.add(btnLamMoi);
        pnlActions.add(Box.createHorizontalStrut(20));
        pnlActions.add(new JLabel("Tìm mã/tên:"));
        pnlActions.add(txtTimKiem);
        pnlActions.add(btnTimKiem);

        pnlCenter.add(pnlActions, BorderLayout.SOUTH);
        add(pnlCenter, BorderLayout.CENTER);

        // Panel Dưới: Bảng dữ liệu JTable
        String[] columns = {"STT", "Mã SV", "Họ và tên", "Ngày sinh", "Giới tính", "Lớp", "SĐT", "Email", "Trạng thái"};
        modelTable = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblSinhVien = new JTable(modelTable);
        tblSinhVien.setRowHeight(24);
        tblSinhVien.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tblSinhVienMouseClicked();
            }
        });

        JScrollPane scroll = new JScrollPane(tblSinhVien);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách sinh viên (Nhấp vào dòng để xem thông tin)"));
        scroll.setPreferredSize(new Dimension(900, 260));
        add(scroll, BorderLayout.SOUTH);
    }

    private void loadCboLop() {
        cboLop.removeAllItems();
        cboLop.addItem(null);
        for (LopHoc lop : lopDAO.getAll()) {
            cboLop.addItem(lop);
        }
    }

    private void fillTable(List<SinhVien> list) {
        modelTable.setRowCount(0);
        int stt = 1;
        for (SinhVien sv : list) {
            String ns = sv.getNgaySinh() != null ? SDF.format(sv.getNgaySinh()) : "";
            modelTable.addRow(new Object[]{
                stt++, sv.getMaSV(), sv.getHoTen(), ns,
                sv.getGioiTinh(), sv.getTenLop(), sv.getSoDienThoai(),
                sv.getEmail(), sv.getTrangThai()
            });
        }
    }

    private void tblSinhVienMouseClicked() {
        int row = tblSinhVien.getSelectedRow();
        if (row < 0) return;

        txtMaSV.setText(modelTable.getValueAt(row, 1).toString());
        txtMaSV.setEditable(false); // Không cho sửa khóa chính
        txtHoTen.setText(modelTable.getValueAt(row, 2).toString());
        txtNgaySinh.setText(modelTable.getValueAt(row, 3) != null ? modelTable.getValueAt(row, 3).toString() : "");

        String gt = modelTable.getValueAt(row, 4).toString();
        if ("Nu".equalsIgnoreCase(gt) || "Nữ".equalsIgnoreCase(gt)) {
            rdoNu.setSelected(true);
        } else {
            rdoNam.setSelected(true);
        }

        String tenLop = modelTable.getValueAt(row, 5) != null ? modelTable.getValueAt(row, 5).toString() : "";
        for (int i = 0; i < cboLop.getItemCount(); i++) {
            LopHoc lop = cboLop.getItemAt(i);
            if (lop != null && lop.getTenLop().equalsIgnoreCase(tenLop)) {
                cboLop.setSelectedIndex(i);
                break;
            }
        }

        txtSDT.setText(modelTable.getValueAt(row, 6) != null ? modelTable.getValueAt(row, 6).toString() : "");
        txtEmail.setText(modelTable.getValueAt(row, 7) != null ? modelTable.getValueAt(row, 7).toString() : "");
        cboTrangThai.setSelectedItem(modelTable.getValueAt(row, 8));

        // Lấy địa chỉ từ DB
        SinhVien sv = svDAO.getSinhVienById(txtMaSV.getText());
        if (sv != null) {
            txtDiaChi.setText(sv.getDiaChi());
        }
    }

    private void clearForm() {
        txtMaSV.setText("");
        txtMaSV.setEditable(true);
        txtHoTen.setText("");
        txtNgaySinh.setText("");
        txtDiaChi.setText("");
        txtEmail.setText("");
        txtSDT.setText("");
        rdoNam.setSelected(true);
        cboLop.setSelectedIndex(0);
        cboTrangThai.setSelectedIndex(0);
        txtTimKiem.setText("");
        fillTable(svDAO.getAllSinhVien());
    }

    private SinhVien getModelFromForm() {
        String ma = txtMaSV.getText().trim();
        String ten = txtHoTen.getText().trim();
        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã SV và Họ tên!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        SinhVien sv = new SinhVien();
        sv.setMaSV(ma);
        sv.setHoTen(ten);
        try {
            String nsStr = txtNgaySinh.getText().trim();
            if (!nsStr.isEmpty()) sv.setNgaySinh(SDF.parse(nsStr));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ngày sinh sai định dạng (dd/MM/yyyy)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        sv.setGioiTinh(rdoNam.isSelected() ? "Nam" : "Nu");
        LopHoc lop = (LopHoc) cboLop.getSelectedItem();
        sv.setMaLop(lop != null ? lop.getMaLop() : null);
        sv.setSoDienThoai(txtSDT.getText().trim());
        sv.setEmail(txtEmail.getText().trim());
        sv.setDiaChi(txtDiaChi.getText().trim());
        sv.setTrangThai(cboTrangThai.getSelectedItem().toString());
        return sv;
    }

    private void btnThemActionPerformed() {
        SinhVien sv = getModelFromForm();
        if (sv == null) return;

        if (svDAO.getSinhVienById(sv.getMaSV()) != null) {
            JOptionPane.showMessageDialog(this, "Mã sinh viên này đã tồn tại!", "Trùng mã", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (svDAO.insert(sv)) {
            JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!");
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnSuaActionPerformed() {
        SinhVien sv = getModelFromForm();
        if (sv == null) return;

        if (svDAO.update(sv)) {
            JOptionPane.showMessageDialog(this, "Cập nhật sinh viên thành công!");
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnXoaActionPerformed() {
        String ma = txtMaSV.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sinh viên " + ma + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (svDAO.delete(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa sinh viên thành công!");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại! Sinh viên đang có dữ liệu điểm học phần.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void btnTimKiemActionPerformed() {
        String kw = txtTimKiem.getText().trim();
        List<SinhVien> list = svDAO.searchSinhVien(kw, null, null);
        fillTable(list);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormSinhVien().setVisible(true));
    }
}
