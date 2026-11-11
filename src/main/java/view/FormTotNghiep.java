package view;

import dao.KhoaHocDAO;
import dao.SinhVienDAO;
import model.KhoaHoc;
import model.SinhVien;
import service.TotNghiepService;
import service.TotNghiepService.KetQuaTotNghiep;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FormTotNghiep extends JFrame {

    private JComboBox<KhoaHoc> cboKhoa;
    private JTextField txtMaSV;
    private JButton btnXet;

    private JTable tblKetQua;
    private DefaultTableModel modelTable;
    private JTextArea txtChiTiet;

    private KhoaHocDAO khoaDAO;
    private SinhVienDAO svDAO;
    private TotNghiepService totNghiepService;

    public FormTotNghiep() {
        khoaDAO = new KhoaHocDAO();
        svDAO = new SinhVienDAO();
        totNghiepService = new TotNghiepService();
        initComponents();
        loadKhoa();
    }

    private void initComponents() {
        setTitle("Tra Cứu GPA & Xét Tốt Nghiệp Tự Động");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        JLabel lblTitle = new JLabel("TRA CỨU GPA HỆ 4 & XÉT ĐIỀU KIỆN TỐT NGHIỆP TỰ ĐỘNG", JLabel.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitle.setForeground(new Color(25, 118, 210));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        pnlTop.setBorder(BorderFactory.createTitledBorder("Bộ lọc xét tốt nghiệp"));

        pnlTop.add(new JLabel("Khóa đào tạo:"));
        cboKhoa = new JComboBox<>();
        cboKhoa.setPreferredSize(new Dimension(180, 26));
        pnlTop.add(cboKhoa);

        pnlTop.add(new JLabel("Mã SV (để trống = xét cả khóa):"));
        txtMaSV = new JTextField(12);
        pnlTop.add(txtMaSV);

        btnXet = new JButton("🎓 Xét tốt nghiệp");
        btnXet.setBackground(new Color(46, 125, 50));
        btnXet.setForeground(Color.WHITE);
        btnXet.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnXet.addActionListener(e -> btnXetActionPerformed());
        pnlTop.add(btnXet);

        add(pnlTop, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(580);

        String[] cols = {"STT", "Mã SV", "Họ và tên", "GPA", "TC Đạt", "Kết luận", "Xếp loại"};
        modelTable = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblKetQua = new JTable(modelTable);
        tblKetQua.setRowHeight(26);

        tblKetQua.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Object kl = table.getValueAt(row, 5);
                if (!isSelected) {
                    if (kl != null && kl.toString().contains("ĐỦ")) {
                        c.setBackground(new Color(232, 245, 233));
                        c.setForeground(new Color(27, 94, 32));
                    } else {
                        c.setBackground(new Color(255, 235, 238));
                        c.setForeground(new Color(183, 28, 28));
                    }
                }
                return c;
            }
        });

        tblKetQua.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tblKetQuaMouseClicked();
            }
        });

        JScrollPane scrollTable = new JScrollPane(tblKetQua);
        scrollTable.setBorder(BorderFactory.createTitledBorder("Danh sách kết quả xét duyệt"));
        split.setLeftComponent(scrollTable);

        txtChiTiet = new JTextArea();
        txtChiTiet.setEditable(false);
        txtChiTiet.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtChiTiet.setBackground(new Color(250, 250, 250));

        JScrollPane scrollText = new JScrollPane(txtChiTiet);
        scrollText.setBorder(BorderFactory.createTitledBorder("Chi tiết đối chiếu khung tín chỉ của SV"));
        split.setRightComponent(scrollText);

        add(split, BorderLayout.CENTER);
    }

    private void loadKhoa() {
        cboKhoa.removeAllItems();
        for (KhoaHoc k : khoaDAO.getAll()) cboKhoa.addItem(k);
    }

    private void btnXetActionPerformed() {
        KhoaHoc k = (KhoaHoc) cboKhoa.getSelectedItem();
        if (k == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khóa học!");
            return;
        }

        modelTable.setRowCount(0);
        txtChiTiet.setText("");
        String ma = txtMaSV.getText().trim();

        if (!ma.isEmpty()) {
            xetMotSV(ma, k.getMaKhoa());
        } else {
            List<SinhVien> list = svDAO.searchSinhVien(null, null, k.getMaKhoa());
            for (SinhVien sv : list) {
                xetMotSV(sv.getMaSV(), k.getMaKhoa());
            }
        }
    }

    private void xetMotSV(String maSV, String maKhoa) {
        KetQuaTotNghiep kq = totNghiepService.xetTotNghiep(maSV, maKhoa);
        SinhVien sv = svDAO.getSinhVienById(maSV);
        String ten = sv != null ? sv.getHoTen() : "N/A";
        String kl = kq.isDuDieuKien() ? "✅ ĐỦ ĐIỀU KIỆN" : "❌ CHƯA ĐỦ";

        modelTable.addRow(new Object[]{
            modelTable.getRowCount() + 1,
            maSV, ten,
            String.format("%.2f", kq.getGpa()),
            kq.getTongTinChiTichLuy(),
            kl,
            kq.isDuDieuKien() ? kq.getXepLoai() : "--"
        });
    }

    private void tblKetQuaMouseClicked() {
        int row = tblKetQua.getSelectedRow();
        if (row < 0) return;

        String maSV = modelTable.getValueAt(row, 1).toString();
        KhoaHoc k = (KhoaHoc) cboKhoa.getSelectedItem();
        if (k == null) return;

        KetQuaTotNghiep kq = totNghiepService.xetTotNghiep(maSV, k.getMaKhoa());
        txtChiTiet.setText(kq.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormTotNghiep().setVisible(true));
    }
}
