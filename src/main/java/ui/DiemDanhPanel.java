package ui;

import dao.DiemDanhDAO;
import dao.LopHocPhanDAO;
import dao.SinhVienDAO;
import model.DiemDanh;
import model.LopHocPhan;
import model.SinhVien;
import service.DiemDanhService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiemDanhPanel extends JPanel {

    private JComboBox<LopHocPhan> cbLHP;
    private JSpinner spNgayHoc;
    private JSpinner spBuoiHoc;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblTongSoBuoi, lblGioiHanVang, lblSoSVBiCam;

    private DiemDanhDAO diemDanhDAO;
    private LopHocPhanDAO lhpDAO;
    private SinhVienDAO svDAO;
    private DiemDanhService diemDanhService;

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    public DiemDanhPanel() {
        diemDanhDAO = new DiemDanhDAO();
        lhpDAO = new LopHocPhanDAO();
        svDAO = new SinhVienDAO();
        diemDanhService = new DiemDanhService();
        initUI();
        loadCombos();
    }

    private void initUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Control
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Thông tin điểm danh buổi học"));

        topPanel.add(new JLabel("Lớp HP:"));
        cbLHP = new JComboBox<>();
        cbLHP.setPreferredSize(new Dimension(240, 26));
        cbLHP.addActionListener(e -> loadDanhSach());
        topPanel.add(cbLHP);

        topPanel.add(new JLabel("Ngày học:"));
        spNgayHoc = new JSpinner(new SpinnerDateModel());
        spNgayHoc.setEditor(new JSpinner.DateEditor(spNgayHoc, "dd/MM/yyyy"));
        topPanel.add(spNgayHoc);

        topPanel.add(new JLabel("Buổi số:"));
        spBuoiHoc = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        topPanel.add(spBuoiHoc);

        JButton btnQuet = new JButton("🚨 Quét Cấm Thi");
        btnQuet.setBackground(new Color(198, 40, 40)); btnQuet.setForeground(Color.WHITE);
        btnQuet.addActionListener(e -> quetCamThi());
        topPanel.add(btnQuet);

        add(topPanel, BorderLayout.NORTH);

        // Center Table
        String[] cols = {"STT", "Mã SV", "Họ và tên", "Tổng số vắng", "Trạng thái hôm nay", "Cảnh báo cấm thi"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Cho phép sửa cột trạng thái hôm nay
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);

        // Editor cho cột trạng thái hôm nay
        JComboBox<String> cbTrangThaiEditor = new JComboBox<>(new String[]{"CoMat", "Vang", "VangPhep"});
        table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(cbTrangThaiEditor));

        // Màu sắc cảnh báo cấm thi
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
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

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Danh sách sinh viên và chuyên cần"));
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // South Actions & Summary
        JPanel bottomContainer = new JPanel(new BorderLayout());

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        infoPanel.setBackground(new Color(245, 245, 245));
        lblTongSoBuoi = new JLabel("Tổng số buổi: --");
        lblGioiHanVang = new JLabel("Giới hạn vắng tối đa (20%): --");
        lblSoSVBiCam = new JLabel("Số SV bị cấm thi: 0");
        lblSoSVBiCam.setFont(new Font("Arial", Font.BOLD, 13));
        lblSoSVBiCam.setForeground(Color.RED);
        infoPanel.add(lblTongSoBuoi);
        infoPanel.add(new JSeparator(SwingConstants.VERTICAL));
        infoPanel.add(lblGioiHanVang);
        infoPanel.add(new JSeparator(SwingConstants.VERTICAL));
        infoPanel.add(lblSoSVBiCam);
        bottomContainer.add(infoPanel, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        JButton btnSave = new JButton("💾 Lưu điểm danh hôm nay");
        btnSave.setBackground(new Color(46, 125, 50)); btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> luuDiemDanh());

        JButton btnRefresh = new JButton("🔄 Làm mới");
        btnRefresh.addActionListener(e -> loadDanhSach());

        btnPanel.add(btnSave);
        btnPanel.add(btnRefresh);
        bottomContainer.add(btnPanel, BorderLayout.SOUTH);

        add(bottomContainer, BorderLayout.SOUTH);
    }

    private void loadCombos() {
        cbLHP.removeAllItems();
        for (LopHocPhan l : lhpDAO.getAll()) cbLHP.addItem(l);
        loadDanhSach();
    }

    private void loadDanhSach() {
        LopHocPhan lhp = (LopHocPhan) cbLHP.getSelectedItem();
        tableModel.setRowCount(0);
        if (lhp == null) return;

        int tongBuoi = lhp.getTongSoBuoi();
        int maxVang = tongBuoi > 0 ? tongBuoi / 5 : 3;
        lblTongSoBuoi.setText("Tổng số buổi: " + tongBuoi);
        lblGioiHanVang.setText("Giới hạn vắng tối đa (20%): " + maxVang + " buổi");

        List<SinhVien> svList = svDAO.getSinhVienByLHP(lhp.getMaLHP());
        int countCamThi = 0;
        int stt = 1;

        for (SinhVien sv : svList) {
            int soVang = diemDanhDAO.countVang(sv.getMaSV(), lhp.getMaLHP());
            boolean isCam = soVang > maxVang;
            if (isCam) countCamThi++;

            String canhBao = isCam ? "⛔ CẤM THI (Vắng quá 20%)" : "Bình thường";

            tableModel.addRow(new Object[]{
                stt++, sv.getMaSV(), sv.getHoTen(), soVang, "CoMat", canhBao
            });
        }

        lblSoSVBiCam.setText("Số SV bị cấm thi: " + countCamThi);
    }

    private void luuDiemDanh() {
        LopHocPhan lhp = (LopHocPhan) cbLHP.getSelectedItem();
        if (lhp == null) return;

        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }

        Date ngayHoc = (Date) spNgayHoc.getValue();
        int buoi = (int) spBuoiHoc.getValue();

        Map<String, String> mapTT = new HashMap<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String maSV = (String) tableModel.getValueAt(i, 1);
            String tt = (String) tableModel.getValueAt(i, 4);
            mapTT.put(maSV, tt);
        }

        int count = diemDanhService.ghiDiemDanhBuoi(lhp.getMaLHP(), ngayHoc, buoi, mapTT);
        loadDanhSach();
        JOptionPane.showMessageDialog(this, "Đã lưu điểm danh cho " + count + " sinh viên vào buổi " + buoi + "!");
    }

    private void quetCamThi() {
        LopHocPhan lhp = (LopHocPhan) cbLHP.getSelectedItem();
        if (lhp == null) return;

        List<SinhVien> dsCamThi = diemDanhService.getDanhSachCamThi(lhp.getMaLHP());
        if (dsCamThi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có sinh viên nào vi phạm cấm thi!", "Kết quả quét", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Phát hiện ").append(dsCamThi.size()).append(" sinh viên bị CẤM THI do vắng quá 20%:\n\n");
            for (SinhVien s : dsCamThi) {
                int vang = diemDanhDAO.countVang(s.getMaSV(), lhp.getMaLHP());
                sb.append("• ").append(s.getMaSV()).append(" - ").append(s.getHoTen())
                  .append(" (Nghỉ ").append(vang).append(" buổi)\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "CẢNH BÁO CẤM THI", JOptionPane.WARNING_MESSAGE);
        }
    }
}
