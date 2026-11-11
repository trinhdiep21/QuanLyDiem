package view;

import dao.DiemDanhDAO;
import dao.LopHocPhanDAO;
import dao.SinhVienDAO;
import model.LopHocPhan;
import model.SinhVien;
import service.DiemDanhService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormDiemDanh extends JFrame {

    private JComboBox<LopHocPhan> cboLHP;
    private JSpinner spNgayHoc;
    private JSpinner spBuoiHoc;
    private JLabel lblThongKe;

    private JButton btnLuu;
    private JButton btnQuetCamThi;

    private JTable tblDiemDanh;
    private DefaultTableModel modelTable;

    private DiemDanhDAO diemDanhDAO;
    private LopHocPhanDAO lhpDAO;
    private SinhVienDAO svDAO;
    private DiemDanhService diemDanhService;

    public FormDiemDanh() {
        diemDanhDAO = new DiemDanhDAO();
        lhpDAO = new LopHocPhanDAO();
        svDAO = new SinhVienDAO();
        diemDanhService = new DiemDanhService();
        initComponents();
        loadLHP();
    }

    private void initComponents() {
        setTitle("Quản Lý Điểm Danh & Quét CẤM THI Tự Động");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        JLabel lblTitle = new JLabel("HỆ THỐNG ĐIỂM DANH & QUYẾT ĐỊNH CẤM THI THEO QUY CHẾ", JLabel.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitle.setForeground(new Color(25, 118, 210));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        pnlTop.setBorder(BorderFactory.createTitledBorder("Thông tin buổi học"));

        pnlTop.add(new JLabel("Lớp học phần:"));
        cboLHP = new JComboBox<>();
        cboLHP.setPreferredSize(new Dimension(240, 26));
        cboLHP.addActionListener(e -> loadDanhSach());
        pnlTop.add(cboLHP);

        pnlTop.add(new JLabel("Ngày học:"));
        spNgayHoc = new JSpinner(new SpinnerDateModel());
        spNgayHoc.setEditor(new JSpinner.DateEditor(spNgayHoc, "dd/MM/yyyy"));
        pnlTop.add(spNgayHoc);

        pnlTop.add(new JLabel("Buổi số:"));
        spBuoiHoc = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        pnlTop.add(spBuoiHoc);

        btnQuetCamThi = new JButton("🚨 Quét CẤM THI");
        btnQuetCamThi.setBackground(new Color(198, 40, 40));
        btnQuetCamThi.setForeground(Color.WHITE);
        btnQuetCamThi.addActionListener(e -> btnQuetCamThiActionPerformed());
        pnlTop.add(btnQuetCamThi);

        add(pnlTop, BorderLayout.NORTH);

        String[] columns = {"STT", "Mã SV", "Họ và tên", "Tổng số buổi vắng", "Hôm nay", "Cảnh báo quy chế"};
        modelTable = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 4; // Chỉ cho sửa cột hôm nay
            }
        };
        tblDiemDanh = new JTable(modelTable);
        tblDiemDanh.setRowHeight(26);

        // Editor combobox cho cột trạng thái hôm nay
        JComboBox<String> cboTT = new JComboBox<>(new String[]{"CoMat", "Vang", "VangPhep"});
        tblDiemDanh.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(cboTT));

        // Highlight màu đỏ sinh viên bị cấm thi
        tblDiemDanh.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Object warning = table.getValueAt(row, 5);
                if (!isSelected) {
                    if (warning != null && warning.toString().contains("CẤM THI")) {
                        c.setBackground(new Color(255, 205, 210));
                        c.setForeground(new Color(183, 28, 28));
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(tblDiemDanh);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách sinh viên (Nhấp vào cột 'Hôm nay' để đổi Có mặt / Vắng)"));
        add(scroll, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new BorderLayout());
        lblThongKe = new JLabel("Tổng số buổi: -- | Giới hạn vắng tối đa (20%): --");
        lblThongKe.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        pnlBottom.add(lblThongKe, BorderLayout.NORTH);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        btnLuu = new JButton("💾 Lưu điểm danh buổi này");
        btnLuu.setBackground(new Color(46, 125, 50));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.addActionListener(e -> btnLuuActionPerformed());
        pnlBtn.add(btnLuu);

        JButton btnReload = new JButton("Làm mới");
        btnReload.addActionListener(e -> loadDanhSach());
        pnlBtn.add(btnReload);

        pnlBottom.add(pnlBtn, BorderLayout.SOUTH);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    private void loadLHP() {
        cboLHP.removeAllItems();
        for (LopHocPhan l : lhpDAO.getAll()) cboLHP.addItem(l);
        loadDanhSach();
    }

    private void loadDanhSach() {
        LopHocPhan lhp = (LopHocPhan) cboLHP.getSelectedItem();
        modelTable.setRowCount(0);
        if (lhp == null) return;

        int tongBuoi = lhp.getTongSoBuoi();
        int maxVang = tongBuoi > 0 ? tongBuoi / 5 : 3;
        lblThongKe.setText("Lớp HP: " + lhp.getMaLHP() + " | Tổng số buổi: " + tongBuoi + " | Giới hạn vắng tối đa (20%): " + maxVang + " buổi");

        List<SinhVien> svList = svDAO.getSinhVienByLHP(lhp.getMaLHP());
        int stt = 1;
        for (SinhVien sv : svList) {
            int soVang = diemDanhDAO.countVang(sv.getMaSV(), lhp.getMaLHP());
            boolean isCam = soVang > maxVang;
            String canhBao = isCam ? "⛔ CẤM THI (Nghỉ > 20%)" : "Bình thường";

            modelTable.addRow(new Object[]{
                stt++, sv.getMaSV(), sv.getHoTen(), soVang, "CoMat", canhBao
            });
        }
    }

    private void btnLuuActionPerformed() {
        LopHocPhan lhp = (LopHocPhan) cboLHP.getSelectedItem();
        if (lhp == null) return;

        if (tblDiemDanh.isEditing()) tblDiemDanh.getCellEditor().stopCellEditing();

        Date ngayHoc = (Date) spNgayHoc.getValue();
        int buoi = (int) spBuoiHoc.getValue();

        Map<String, String> mapTT = new HashMap<>();
        for (int i = 0; i < modelTable.getRowCount(); i++) {
            String maSV = modelTable.getValueAt(i, 1).toString();
            String tt = modelTable.getValueAt(i, 4).toString();
            mapTT.put(maSV, tt);
        }

        int count = diemDanhService.ghiDiemDanhBuoi(lhp.getMaLHP(), ngayHoc, buoi, mapTT);
        loadDanhSach();
        JOptionPane.showMessageDialog(this, "Đã lưu điểm danh cho " + count + " sinh viên vào buổi số " + buoi + "!");
    }

    private void btnQuetCamThiActionPerformed() {
        LopHocPhan lhp = (LopHocPhan) cboLHP.getSelectedItem();
        if (lhp == null) return;

        List<SinhVien> listCam = diemDanhService.getDanhSachCamThi(lhp.getMaLHP());
        if (listCam.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tất cả sinh viên đều đủ điều kiện dự thi (không ai nghỉ quá 20%)!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("QUYẾT ĐỊNH CẤM THI THEO QUY CHẾ\n");
            sb.append("Phát hiện ").append(listCam.size()).append(" sinh viên vi phạm số buổi nghỉ:\n\n");
            for (SinhVien sv : listCam) {
                int soVang = diemDanhDAO.countVang(sv.getMaSV(), lhp.getMaLHP());
                sb.append("• ").append(sv.getMaSV()).append(" - ").append(sv.getHoTen())
                  .append(" (Nghỉ ").append(soVang).append(" buổi)\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "CẢNH BÁO CẤM THI", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormDiemDanh().setVisible(true));
    }
}
