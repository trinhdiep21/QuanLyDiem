package ui;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTabbedPane tabbedPane;

    public MainFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("QUẢN LÝ ĐIỂM & HỌC SINH - SINH VIÊN");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1024, 600));

        setJMenuBar(createMenuBar());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 80, 162));
        headerPanel.setPreferredSize(new Dimension(0, 55));
        JLabel titleLabel = new JLabel("   PHẦN MỀM QUẢN LÝ ĐIỂM & HỌC SINH - SINH VIÊN", JLabel.LEFT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 17));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Tabbed pane
        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 13));

        tabbedPane.addTab("👥 Sinh Viên", new SinhVienPanel());
        tabbedPane.addTab("📚 Môn Học", new MonHocPanel());
        tabbedPane.addTab("🏫 Lớp Học", new LopHocPanel());
        tabbedPane.addTab("📅 Học Kỳ", new HocKyPanel());
        tabbedPane.addTab("📝 Lớp Học Phần", new LopHocPhanPanel());
        tabbedPane.addTab("📊 Nhập Điểm", new DiemPanel());
        tabbedPane.addTab("📋 Điểm Danh", new DiemDanhPanel());
        tabbedPane.addTab("🏆 Xem GPA", new GPAPanel());
        tabbedPane.addTab("🎓 Tốt Nghiệp", new TotNghiepPanel());
        tabbedPane.addTab("🗂 Khung CTĐT", new KhungCTDTPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Status bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 3));
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.add(new JLabel("✅ Kết nối Database thành công"));
        add(statusBar, BorderLayout.SOUTH);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Hệ thống");
        JMenuItem exitItem = new JMenuItem("Thoát");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Trợ giúp");
        JMenuItem aboutItem = new JMenuItem("Thông tin");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "Phần mềm Quản lý Điểm & Học sinh - Sinh viên\nPhiên bản 1.0\n© 2024",
            "Thông tin", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        return menuBar;
    }
}
