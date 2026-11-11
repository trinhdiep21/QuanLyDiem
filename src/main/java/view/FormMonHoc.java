package view;

import dao.MonHocDAO;
import model.MonHoc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FormMonHoc extends JFrame {

    private JTextField txtMaMH;
    private JTextField txtTenMH;
    private JSpinner spTinChi;
    private JComboBox<String> cboLoai;
    private JTextField txtMoTa;

    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;

    private JTable tblMonHoc;
    private DefaultTableModel modelTable;

    private MonHocDAO mhDAO;

    public FormMonHoc() {
        mhDAO = new MonHocDAO();
        initComponents();
        fillTable(mhDAO.getAll());
    }

    private void initComponents() {
        setTitle("Quản Lý Môn Học");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        JLabel lblTitle = new JLabel("QUẢN LÝ MÔN HỌC & TÍN CHỈ", JLabel.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setForeground(new Color(25, 118, 210));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new BorderLayout(5, 5));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        JPanel pnlForm = new JPanel(new GridLayout(3, 4, 10, 10));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin môn học"));

        pnlForm.add(new JLabel("Mã môn học:"));
        txtMaMH = new JTextField();
        pnlForm.add(txtMaMH);

        pnlForm.add(new JLabel("Tên môn học:"));
        txtTenMH = new JTextField();
        pnlForm.add(txtTenMH);

        pnlForm.add(new JLabel("Số tín chỉ:"));
        spTinChi = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        pnlForm.add(spTinChi);

        pnlForm.add(new JLabel("Phân loại:"));
        cboLoai = new JComboBox<>(new String[]{"BatBuoc", "TuChon"});
        pnlForm.add(cboLoai);

        pnlForm.add(new JLabel("Mô tả:"));
        txtMoTa = new JTextField();
        pnlForm.add(txtMoTa);
        pnlForm.add(new JLabel("")); // ô trống
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

        btnXoa = new JButton("Xóa");
        btnXoa.setBackground(new Color(211, 47, 47));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.addActionListener(e -> btnXoaActionPerformed());

        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.addActionListener(e -> clearForm());

        pnlActions.add(btnThem);
        pnlActions.add(btnSua);
        pnlActions.add(btnXoa);
        pnlActions.add(btnLamMoi);
        pnlCenter.add(pnlActions, BorderLayout.SOUTH);

        add(pnlCenter, BorderLayout.CENTER);

        String[] columns = {"STT", "Mã MH", "Tên Môn Học", "Số Tín Chỉ", "Loại", "Mô Tả"};
        modelTable = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblMonHoc = new JTable(modelTable);
        tblMonHoc.setRowHeight(24);
        tblMonHoc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tblMonHocMouseClicked();
            }
        });

        JScrollPane scroll = new JScrollPane(tblMonHoc);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách môn học"));
        scroll.setPreferredSize(new Dimension(750, 260));
        add(scroll, BorderLayout.SOUTH);
    }

    private void fillTable(List<MonHoc> list) {
        modelTable.setRowCount(0);
        int stt = 1;
        for (MonHoc mh : list) {
            String loai = "BatBuoc".equals(mh.getLoai()) ? "Bắt buộc" : "Tự chọn";
            modelTable.addRow(new Object[]{
                stt++, mh.getMaMH(), mh.getTenMH(), mh.getSoTinChi(), loai, mh.getMoTa()
            });
        }
    }

    private void tblMonHocMouseClicked() {
        int row = tblMonHoc.getSelectedRow();
        if (row < 0) return;

        txtMaMH.setText(modelTable.getValueAt(row, 1).toString());
        txtMaMH.setEditable(false);
        txtTenMH.setText(modelTable.getValueAt(row, 2).toString());
        spTinChi.setValue(Integer.parseInt(modelTable.getValueAt(row, 3).toString()));
        String loai = modelTable.getValueAt(row, 4).toString();
        cboLoai.setSelectedItem("Bắt buộc".equals(loai) ? "BatBuoc" : "TuChon");
        txtMoTa.setText(modelTable.getValueAt(row, 5) != null ? modelTable.getValueAt(row, 5).toString() : "");
    }

    private void clearForm() {
        txtMaMH.setText("");
        txtMaMH.setEditable(true);
        txtTenMH.setText("");
        spTinChi.setValue(3);
        cboLoai.setSelectedIndex(0);
        txtMoTa.setText("");
        fillTable(mhDAO.getAll());
    }

    private void btnThemActionPerformed() {
        String ma = txtMaMH.getText().trim();
        String ten = txtTenMH.getText().trim();
        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã và Tên môn học!");
            return;
        }
        if (mhDAO.getById(ma) != null) {
            JOptionPane.showMessageDialog(this, "Mã môn học đã tồn tại!");
            return;
        }

        MonHoc mh = new MonHoc(ma, ten, (int) spTinChi.getValue(), cboLoai.getSelectedItem().toString(), txtMoTa.getText().trim());
        if (mhDAO.insert(mh)) {
            JOptionPane.showMessageDialog(this, "Thêm môn học thành công!");
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!");
        }
    }

    private void btnSuaActionPerformed() {
        String ma = txtMaMH.getText().trim();
        String ten = txtTenMH.getText().trim();
        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn môn học cần sửa!");
            return;
        }

        MonHoc mh = new MonHoc(ma, ten, (int) spTinChi.getValue(), cboLoai.getSelectedItem().toString(), txtMoTa.getText().trim());
        if (mhDAO.update(mh)) {
            JOptionPane.showMessageDialog(this, "Cập nhật môn học thành công!");
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }

    private void btnXoaActionPerformed() {
        String ma = txtMaMH.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn môn học cần xóa!");
            return;
        }

        int ok = JOptionPane.showConfirmDialog(this, "Xóa môn học " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            if (mhDAO.delete(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại! Môn học đang có lớp học phần mở.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormMonHoc().setVisible(true));
    }
}
