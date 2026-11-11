package main;

import com.formdev.flatlaf.FlatLightLaf;
import ui.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Entry point - Ứng dụng Quản lý Điểm & Học sinh - Sinh viên
 */
public class MainApp {

    public static void main(String[] args) {
        // Cài đặt Look & Feel
        try {
            FlatLightLaf.setup();
            UIManager.put("TabbedPane.tabWidth", 170);
            UIManager.put("TabbedPane.minimumTabWidth", 150);
            UIManager.put("defaultFont", new Font("Arial", Font.PLAIN, 13));
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Khởi chạy trên Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Splash screen
            JWindow splash = createSplash();
            splash.setVisible(true);

            // Đợi 1.5s rồi mở cửa sổ chính
            Timer timer = new Timer(1500, e -> {
                splash.dispose();
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    private static JWindow createSplash() {
        JWindow splash = new JWindow();
        splash.setSize(500, 300);
        splash.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 80, 162));
        panel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 3));

        // Title
        JLabel titleLabel = new JLabel("QUẢN LÝ ĐIỂM & HỌC SINH", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(60, 10, 10, 10));
        panel.add(titleLabel, BorderLayout.CENTER);

        JLabel subLabel = new JLabel("SINH VIÊN", JLabel.CENTER);
        subLabel.setFont(new Font("Arial", Font.BOLD, 18));
        subLabel.setForeground(new Color(255, 220, 80));
        panel.add(subLabel, BorderLayout.SOUTH);

        JPanel loadingPanel = new JPanel(new BorderLayout());
        loadingPanel.setBackground(new Color(30, 80, 162));
        loadingPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        bar.setForeground(new Color(255, 220, 80));
        loadingPanel.add(new JLabel("Đang khởi động...", JLabel.CENTER) {{
            setForeground(Color.WHITE);
        }}, BorderLayout.NORTH);
        loadingPanel.add(bar, BorderLayout.CENTER);
        panel.add(loadingPanel, BorderLayout.SOUTH);

        splash.add(panel);
        return splash;
    }
}
