package tugas2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Program GUI Lintas-Platform Komprehensif (Pertemuan 6)
 * Mendukung adaptasi otomatis untuk:
 * 1. Windows (Windows 10/11 Look and Feel)
 * 2. macOS (Aqua Look and Feel & Global Screen Menu Bar)
 * 3. Linux Ubuntu (GTK+ Look and Feel / Yaru Theme)
 */
public class CrossPlatformGUI extends JFrame {

    public CrossPlatformGUI() {
        // ========================================================================
        // 1. MENDETEKSI SISTEM OPERASI (Windows, macOS, atau Linux/Ubuntu)
        // ========================================================================
        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String osArch = System.getProperty("os.arch");
        String osLower = osName.toLowerCase();

        boolean isMac = osLower.contains("mac");
        boolean isWindows = osLower.contains("win");
        boolean isLinux = osLower.contains("nix") || osLower.contains("nux") || osLower.contains("aix");

        // Identifikasi label ramah untuk tampilan antarmuka
        String osDisplayName;
        String osBadgeIcon;
        if (isMac) {
            osDisplayName = "macOS (Apple)";
            osBadgeIcon = "🍎";
        } else if (isWindows) {
            osDisplayName = "Microsoft Windows";
            osBadgeIcon = "🪟";
        } else if (isLinux) {
            osDisplayName = "Linux (Ubuntu / GNOME GTK)";
            osBadgeIcon = "🐧";
        } else {
            osDisplayName = "Sistem Operasi Lain (" + osName + ")";
            osBadgeIcon = "💻";
        }

        // ========================================================================
        // 2. PENYESUAIAN KHUSUS MACOS (Global Screen Menu Bar & Anti-Aliasing)
        // ========================================================================
        // Di macOS, standar Apple Human Interface Guidelines mewajibkan menu berada
        // di puncak layar monitor (Global Menu Bar), bukan menempel di frame jendela.
        if (isMac) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.application.name", "Demo GUI Multi-OS");
        }

        // Setup dasar jendela JFrame
        setTitle("Demonstrasi GUI Lintas Platform (" + osDisplayName + ")");
        setSize(700, 520);
        setMinimumSize(new Dimension(600, 450));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Memposisikan jendela di tengah layar

        // ========================================================================
        // 3. PENYESUAIAN SHORTCUT KEY LINTAS PLATFORM (Command vs Control)
        // ========================================================================
        // Pengguna Mac menggunakan tombol Command (Meta Key: ⌘)
        // Pengguna Windows dan Linux Ubuntu menggunakan tombol Control (Ctrl Key)
        int shortcutKeyMask = isMac ? InputEvent.META_DOWN_MASK : InputEvent.CTRL_DOWN_MASK;
        String shortcutName = isMac ? "Cmd" : "Ctrl";

        // ========================================================================
        // 4. MEMBUAT KOMPONEN MENU BAR LINTAS-PLATFORM DENGAN ICON SISTEM
        // ========================================================================
        JMenuBar menuBar = new JMenuBar();

        // Mengambil icon native bawaan sistem operasi melalui UIManager
        Icon fileIcon = UIManager.getIcon("FileView.fileIcon");
        Icon saveIcon = UIManager.getIcon("FileView.floppyDriveIcon");
        Icon infoIcon = UIManager.getIcon("OptionPane.informationIcon");

        // --- Menu File ---
        JMenu menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_F);

        JMenuItem itemBuka = new JMenuItem("Buka...", fileIcon);
        itemBuka.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, shortcutKeyMask));
        itemBuka.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Aksi 'Buka' dipicu dengan shortcut " + shortcutName + "+O"));

        JMenuItem itemSimpan = new JMenuItem("Simpan", saveIcon);
        itemSimpan.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcutKeyMask));
        itemSimpan.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Aksi 'Simpan' dipicu dengan shortcut " + shortcutName + "+S"));

        JCheckBoxMenuItem itemAutoSave = new JCheckBoxMenuItem("Auto-Save Aktif", true);

        JMenuItem itemKeluar = new JMenuItem("Keluar");
        // Di Windows/Linux keluar biasa dengan Alt+F4 / Ctrl+Q, di Mac dengan Cmd+Q
        itemKeluar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, shortcutKeyMask));
        itemKeluar.addActionListener(e -> System.exit(0));

        menuFile.add(itemBuka);
        menuFile.add(itemSimpan);
        menuFile.add(itemAutoSave);
        menuFile.addSeparator(); // Garis pemisah native
        menuFile.add(itemKeluar);

        // --- Menu Bantuan ---
        JMenu menuBantuan = new JMenu("Bantuan");
        JMenuItem itemTentang = new JMenuItem("Tentang Aplikasi", infoIcon);
        itemTentang.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Aplikasi Demonstrasi UI Lintas Platform\n" +
                "OS: " + osName + " (" + osArch + ")\n" +
                "Versi: " + osVersion + "\n" +
                "Look & Feel: " + UIManager.getLookAndFeel().getName(),
                "Informasi Sistem", JOptionPane.INFORMATION_MESSAGE));
        menuBantuan.add(itemTentang);

        menuBar.add(menuFile);
        menuBar.add(menuBantuan);
        setJMenuBar(menuBar);

        // ========================================================================
        // 5. MEMBUAT LAYOUT DAN BERBAGAI WIDGET DENGAN KARAKTERISTIK UNIK TIAP OS
        // ========================================================================
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(12, 14, 14, 14));

        // --- PANEL HEADER: Identitas OS Terdeteksi ---
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 2, 2));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                new EmptyBorder(8, 12, 8, 12)
        ));

        JLabel titleLabel = new JLabel(osBadgeIcon + " Terdeteksi: " + osDisplayName + " (" + osName + ")", SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        String lafName = UIManager.getLookAndFeel().getName();
        JLabel subTitleLabel = new JLabel("Theme Aktif: " + lafName + " | Shortcut Utama: " + shortcutName + "+[Key]", SwingConstants.LEFT);
        subTitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subTitleLabel.setForeground(new Color(90, 90, 90));

        headerPanel.add(titleLabel);
        headerPanel.add(subTitleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- TABBED PANE (Bentuk tab sangat berbeda di Mac, Windows, dan Ubuntu) ---
        JTabbedPane tabbedPane = new JTabbedPane();

        // -------------------------------------------------------------
        // TAB 1: SHOWCASE WIDGET FORM & KONTROL INTERAKTIF
        // -------------------------------------------------------------
        JPanel tabWidgets = new JPanel();
        tabWidgets.setLayout(new BoxLayout(tabWidgets, BoxLayout.Y_AXIS));
        tabWidgets.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Group 1: Text Field & Password (Menampilkan efek glow focus khas Mac/Ubuntu vs kotak tegas Windows)
        JPanel textPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        textPanel.setBorder(new TitledBorder("1. Text Input (Perhatikan Border & Focus Ring)"));
        textPanel.add(new JLabel("Nama Pengguna:"));
        textPanel.add(new JTextField("Mahasiswa Java"));
        textPanel.add(new JLabel("Kata Sandi:"));
        textPanel.add(new JPasswordField("rahasia123"));
        tabWidgets.add(textPanel);
        tabWidgets.add(Box.createVerticalStrut(8));

        // Group 2: Checkbox & Radio Button (Perhatikan icon centang dan lingkaran radio tiap OS)
        JPanel selectionPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        selectionPanel.setBorder(new TitledBorder("2. Seleksi (Check Box & Radio Button Native)"));
        
        JCheckBox cb1 = new JCheckBox("Terima Notifikasi", true);
        JCheckBox cb2 = new JCheckBox("Mode Hemat Daya", false);
        JCheckBox cb3 = new JCheckBox("Kirim Laporan Bug", true);

        JRadioButton rb1 = new JRadioButton("Tema Terang", true);
        JRadioButton rb2 = new JRadioButton("Tema Gelap");
        JRadioButton rb3 = new JRadioButton("Ikuti Sistem");
        ButtonGroup bgTheme = new ButtonGroup();
        bgTheme.add(rb1);
        bgTheme.add(rb2);
        bgTheme.add(rb3);

        selectionPanel.add(cb1);
        selectionPanel.add(cb2);
        selectionPanel.add(cb3);
        selectionPanel.add(rb1);
        selectionPanel.add(rb2);
        selectionPanel.add(rb3);
        tabWidgets.add(selectionPanel);
        tabWidgets.add(Box.createVerticalStrut(8));

        // Group 3: Slider & Progress Bar (Perhatikan aksen Aqua Biru di Mac, Hijau/Biru di Windows, Oranye Yaru di Ubuntu)
        JPanel progressPanel = new JPanel(new GridLayout(2, 2, 10, 8));
        progressPanel.setBorder(new TitledBorder("3. Nilai & Indikator (Slider & Progress Bar)"));
        
        JSlider slider = new JSlider(0, 100, 65);
        slider.setMajorTickSpacing(25);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(65);
        progressBar.setStringPainted(true);

        // Sambungkan slider ke progress bar agar interaktif saat presentasi
        slider.addChangeListener(e -> progressBar.setValue(slider.getValue()));

        progressPanel.add(new JLabel("Volume / Tingkat Slider:"));
        progressPanel.add(slider);
        progressPanel.add(new JLabel("Progres Pengunduhan:"));
        progressPanel.add(progressBar);
        tabWidgets.add(progressPanel);
        tabWidgets.add(Box.createVerticalStrut(8));

        // Group 4: Buttons (Perhatikan bentuk bulat Aqua di Mac, kotak modern di Windows, border GTK di Ubuntu)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBorder(new TitledBorder("4. Tombol Aksi (Button Styling)"));
        
        JButton btnDialog = new JButton("Uji Dialog Native");
        btnDialog.addActionListener(e -> {
            // Urutan tombol dialog (OK/Cancel vs Cancel/OK) berbeda di Mac dan Windows/Linux
            int respon = JOptionPane.showConfirmDialog(this,
                    "Perhatikan urutan tombol konfirmasi dan icon dialog pada " + osDisplayName + "!",
                    "Dialog Konfirmasi Native",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (respon == JOptionPane.OK_OPTION) {
                JOptionPane.showMessageDialog(this, "Anda memilih OK!");
            }
        });

        JToggleButton btnToggle = new JToggleButton("Toggle Status");
        JButton btnSimpan = new JButton("Simpan Data (" + shortcutName + "+S)");

        buttonPanel.add(btnToggle);
        buttonPanel.add(btnDialog);
        buttonPanel.add(btnSimpan);
        tabWidgets.add(buttonPanel);

        tabbedPane.addTab("Koleksi Widget OS", tabWidgets);

        // -------------------------------------------------------------
        // TAB 2: DETAIL PERBEDAAN SPESIFIKASI KETIGA SISTEM OPERASI
        // -------------------------------------------------------------
        JPanel tabInfo = new JPanel(new BorderLayout(8, 8));
        tabInfo.setBorder(new EmptyBorder(12, 12, 12, 12));

        String infoHtml = "<html><body style='font-family: sans-serif; font-size: 11px;'>"
                + "<h3>Tabel Karakteristik Tampilan GUI Berdasarkan OS:</h3>"
                + "<table border='1' cellpadding='6' cellspacing='0' style='border-collapse: collapse; width: 100%;'>"
                + "<tr style='background-color: #e0e0e0;'>"
                + "  <th>Karakteristik</th>"
                + "  <th>🪟 Microsoft Windows</th>"
                + "  <th>🍎 Apple macOS</th>"
                + "  <th>🐧 Linux Ubuntu (GTK)</th>"
                + "</tr>"
                + "<tr>"
                + "  <td><b>Look and Feel Class</b></td>"
                + "  <td><code>WindowsLookAndFeel</code></td>"
                + "  <td><code>AquaLookAndFeel</code></td>"
                + "  <td><code>GTKLookAndFeel</code></td>"
                + "</tr>"
                + "<tr>"
                + "  <td><b>Letak Menu Bar</b></td>"
                + "  <td>Di dalam jendela (bawah titlebar)</td>"
                + "  <td>Di atas layar (Global Screen Menu)</td>"
                + "  <td>Di dalam jendela aplikasi</td>"
                + "</tr>"
                + "<tr>"
                + "  <td><b>Tombol Pintasan</b></td>"
                + "  <td><code>Ctrl + Key</code> (Contoh: Ctrl+S)</td>"
                + "  <td><code>Command (⌘) + Key</code></td>"
                + "  <td><code>Ctrl + Key</code> (Contoh: Ctrl+S)</td>"
                + "</tr>"
                + "<tr>"
                + "  <td><b>Ciri Khas Widget</b></td>"
                + "  <td>Desain rata (flat), sudut tegas, aksen biru terang</td>"
                + "  <td>Tombol kapsul membundar, efek glassy/transparan Aqua</td>"
                + "  <td>Tema Yaru/Ambiance, aksen oranye aubergine khas Ubuntu</td>"
                + "</tr>"
                + "<tr>"
                + "  <td><b>Urutan Tombol Dialog</b></td>"
                + "  <td>[OK] [Batal]</td>"
                + "  <td>[Batal] [OK] (Aksi utama di kanan)</td>"
                + "  <td>[Batal] [OK]</td>"
                + "</tr>"
                + "</table>"
                + "<p><i>* Program ini mendeteksi OS saat startup dan otomatis menyesuaikan seluruh parameter di atas.</i></p>"
                + "</body></html>";

        JLabel lblInfoHtml = new JLabel(infoHtml);
        lblInfoHtml.setVerticalAlignment(SwingConstants.TOP);
        tabInfo.add(new JScrollPane(lblInfoHtml), BorderLayout.CENTER);

        tabbedPane.addTab("Tabel Komparasi 3 OS", tabInfo);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    public static void main(String[] args) {
        // ========================================================================
        // 6. MENGAKTIFKAN SYSTEM LOOK AND FEEL SEBELUM FRAME DIBUAT
        // ========================================================================
        // Di Windows: memuat WindowsLookAndFeel
        // Di macOS: memuat AquaLookAndFeel
        // Di Ubuntu Linux: memuat GTKLookAndFeel (sesuai desktop environment GNOME)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Peringatan: Gagal menerapkan System Look and Feel, menggunakan bawaan Swing.");
            e.printStackTrace();
        }

        // Menjalankan UI di Event Dispatch Thread (EDT) untuk stabilitas thread GUI
        SwingUtilities.invokeLater(() -> {
            CrossPlatformGUI app = new CrossPlatformGUI();
            app.setVisible(true);
        });
    }
}
