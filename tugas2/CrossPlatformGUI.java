package tugas2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class CrossPlatformGUI extends JFrame {

    public CrossPlatformGUI() {
        // 1. MENDETEKSI SISTEM OPERASI (OS)
        // Kita mengambil properti "os.name" dari sistem untuk mengetahui OS yang sedang digunakan
        String osName = System.getProperty("os.name");
        
        // Cek apakah OS yang digunakan adalah keluarga macOS (Mac OS X)
        boolean isMac = osName.toLowerCase().contains("mac");

        // 2. PENYESUAIAN KHUSUS MACOS (Global Menu Bar)
        // Secara default di Windows/Linux, Menu Bar menempel di jendela aplikasi.
        // Di macOS, standar UX adalah Menu Bar berada di atas layar (Global Screen Menu).
        // Properti apple.laf.useScreenMenuBar harus di-set ke true jika mendeteksi OS Mac.
        if (isMac) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }

        // --- Setup dasar JFrame ---
        setTitle("Demo GUI Lintas Platform");
        setSize(450, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Memposisikan jendela di tengah layar saat dijalankan

        // 3. MEMBUAT KOMPONEN MENU
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenuItem menuItemSimpan = new JMenuItem("Simpan");

        // 4. PENYESUAIAN SHORTCUT KEY LINTAS PLATFORM
        // Jika di macOS, standar copy/paste/save menggunakan tombol Command (Meta).
        // Jika di Windows/Linux, standar menggunakan tombol Control (Ctrl).
        int shortcutKeyMask = isMac ? InputEvent.META_DOWN_MASK : InputEvent.CTRL_DOWN_MASK;
        
        // Menggabungkan huruf 'S' dengan modifier (Cmd/Ctrl)
        // Di Mac menjadi: Cmd+S | Di Windows menjadi: Ctrl+S
        menuItemSimpan.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcutKeyMask));
        
        // Event listener saat menu di-klik atau shortcut ditekan
        menuItemSimpan.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Aksi Simpan Dijalankan!");
        });

        // Menyusun hirarki menu (MenuItem -> Menu -> MenuBar -> Frame)
        menuFile.add(menuItemSimpan);
        menuBar.add(menuFile);
        setJMenuBar(menuBar);

        // 5. MENAMPILKAN NAMA OS DI TENGAH JENDELA
        // Ini bertujuan agar kita bisa langsung tahu bahwa deteksi OS berhasil saat presentasi
        JLabel osLabel = new JLabel("Sistem Operasi Anda saat ini: " + osName, SwingConstants.CENTER);
        osLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(osLabel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        // 6. MENGAKTIFKAN SYSTEM LOOK AND FEEL (TEMA ASLI OS)
        // Tanpa kode ini, Java Swing akan menggunakan tema jadul bernama 'Metal' atau 'Ocean'.
        // Dengan kode ini, GUI akan menggunakan tema Windows 10/11 di Windows, dan tema Aqua di macOS.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Gagal menerapkan System Look and Feel");
            e.printStackTrace();
        }

        // Best practice Swing: Menjalankan UI di Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            CrossPlatformGUI app = new CrossPlatformGUI();
            app.setVisible(true); // Menampilkan frame ke layar
        });
    }
}
