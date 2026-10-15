# Pembuatan GUI Lintas-Platform Komprehensif dengan Java Swing
*(Studi Komparasi Antarmuka: Microsoft Windows vs Apple macOS vs Linux Ubuntu)*

---

## Bagian 1: Pendahuluan
Salah satu keunggulan utama dari bahasa pemrograman Java adalah portabilitasnya yang tinggi di bawah prinsip **"Write Once, Run Anywhere" (WORA)**. Namun, dalam konteks Rekayasa Perangkat Lunak dan Desain Antarmuka Pengguna (*UI/UX Design*), sekadar "bisa berjalan" saja tidak cukup. Pengguna di setiap platform mengharapkan aplikasi yang mereka gunakan terasa alami, konsisten, dan mematuhi panduan desain antarmuka (*Human Interface Guidelines*) di sistem operasi masing-masing.

Tugas Pertemuan 6 ini mengeksplorasi pembuatan aplikasi Java Swing yang secara otomatis mendeteksi lingkungan sistem operasinya (**Microsoft Windows**, **Apple macOS**, dan **Linux Ubuntu GNOME**) serta mengadaptasi berbagai komponen antarmuka, mulai dari tata letak *Menu Bar*, tombol pintasan (*shortcut keys*), palet warna & aksen fokus, hingga perilaku visual berbagai *widget* interaktif.

---

## Bagian 2: Deteksi OS dan Penyesuaian Kode

Aplikasi ini dirancang cerdas dengan mendeteksi properti sistem saat pertama kali dijalankan melalui `System.getProperty("os.name")`.

### 1. Deteksi Tiga Sistem Operasi
```java
String osName = System.getProperty("os.name").toLowerCase();

boolean isMac     = osName.contains("mac");
boolean isWindows = osName.contains("win");
boolean isLinux   = osName.contains("nix") || osName.contains("nux") || osName.contains("aix");
```

### 2. Mengaktifkan *System Look and Feel*
Secara default, Java Swing menggunakan tema bernama *Metal* atau *Ocean* yang terlihat kaku dan usang. Dengan memanggil:
```java
UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
```
Java Swing akan mendelegasikan proses rendering komponen ke mesin tema asli milik OS:
*   **Di Windows:** Memuat `com.sun.java.swing.plaf.windows.WindowsLookAndFeel`.
*   **Di macOS:** Memuat `com.apple.laf.AquaLookAndFeel`.
*   **Di Linux Ubuntu:** Memuat `com.sun.java.swing.plaf.gtk.GTKLookAndFeel` yang mengikuti tema desktop GNOME/Yaru.

### 3. Penempatan Menu Bar Khusus macOS (Global Screen Menu)
Di Windows dan Linux Ubuntu, *Menu Bar* secara konvensional menempel pada bagian atas jendela aplikasi (di bawah *title bar*). Namun pada macOS, menu wajib diletakkan di bagian paling atas layar monitor (*Global Screen Menu Bar*).
```java
if (isMac) {
    System.setProperty("apple.laf.useScreenMenuBar", "true");
    System.setProperty("apple.awt.application.name", "Demo GUI Multi-OS");
}
```

### 4. Adaptasi Tombol Pintasan (*Shortcut Keys*: Command vs Control)
*   **macOS:** Menggunakan tombol **Command / Meta (⌘)** sebagai modifier utama (`InputEvent.META_DOWN_MASK`), contoh: `Cmd + S` untuk Simpan, `Cmd + O` untuk Buka, dan `Cmd + Q` untuk Keluar.
*   **Windows & Linux Ubuntu:** Menggunakan tombol **Control (Ctrl)** (`InputEvent.CTRL_DOWN_MASK`), contoh: `Ctrl + S` dan `Ctrl + O`.

```java
int shortcutKeyMask = isMac ? InputEvent.META_DOWN_MASK : InputEvent.CTRL_DOWN_MASK;
menuItemSimpan.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcutKeyMask));
```

---

## Bagian 3: Analisis Komponen & Widget yang Berbeda Antar-OS

Program ini menampilkan berbagai *widget* interaktif dalam sebuah `JTabbedPane` untuk memperlihatkan bagaimana satu kode Java yang sama menghasilkan wujud visual yang sangat spesifik di setiap platform:

1.  **Icon Sistem Bawaan (`UIManager.getIcon`):**
    Aplikasi memuat ikon native dari sistem seperti `FileView.fileIcon`, `FileView.floppyDriveIcon`, dan `OptionPane.informationIcon`. Di Windows akan tampil ikon dokumen Windows, di Mac tampil ikon Finder/Aqua, dan di Ubuntu tampil ikon berkas bertema Yaru/Humanity.
2.  **Input Teks (`JTextField` & `JPasswordField`):**
    *   **macOS:** Memiliki sudut membulat halus dengan efek *glow ring* biru khas Aqua saat kursor aktif (*focus*).
    *   **Windows:** Menggunakan kotak dengan garis tepi tajam/tegas (*crisp border*).
    *   **Ubuntu GTK:** Memiliki aksen garis batas berwarna oranye/aubergine khas tema Yaru Ubuntu saat difokuskan.
3.  **Kotak Centang (`JCheckBox`) & Tombol Radio (`JRadioButton`):**
    *   **macOS:** Kotak centang berwarna biru Aqua terang dengan tanda centang tebal, dan tombol radio berbentuk lingkaran timbul.
    *   **Windows:** Kotak persegi putih dengan centang hitam/abu-abu standar Windows.
    *   **Ubuntu GTK:** Memiliki indikator centang dan titik radio berwarna oranye dengan gaya flat modern.
4.  **Penggeser Nilai (`JSlider`) & Indikator Progres (`JProgressBar`):**
    *   **macOS:** Batang progres berupa kapsul bulat dengan gradien biru Aqua yang mengilap (*glossy*).
    *   **Windows:** Batang progres datar berwarna hijau atau biru khas Windows.
    *   **Ubuntu GTK:** Menggunakan warna aksen oranye terang dengan latar abu-abu gelap.
5.  **Tombol Aksi (`JButton` & `JToggleButton`):**
    *   **macOS:** Tombol berbentuk lonjong membundar (*pill-shaped button*) dengan efek tekanan timbul.
    *   **Windows:** Tombol persegi panjang dengan *hover effect* biru muda.
    *   **Ubuntu GTK:** Tombol persegi berujung sedikit membulat dengan garis tepi tegas bernuansa abu-abu gelap/oranye.
6.  **Urutan Tombol Kotak Dialog (`JOptionPane`):**
    Jika tombol "Uji Dialog Native" ditekan:
    *   **Di Windows:** Urutan tombol adalah `[ OK ] [ Batal ]` (Aksi positif di sebelah kiri).
    *   **Di macOS & Ubuntu (GNOME):** Urutan tombol adalah `[ Batal ] [ OK ]` (Aksi positif diletakkan di sebelah paling kanan untuk mencegah pengguna salah menekan secara tidak sengaja).

---

## Bagian 4: Tabel Komparasi Teknis Tiga Platform

| Aspek / Karakteristik | 🪟 Microsoft Windows | 🍎 Apple macOS | 🐧 Linux Ubuntu (GNOME) |
| :--- | :--- | :--- | :--- |
| **Tema Look & Feel** | `WindowsLookAndFeel` | `AquaLookAndFeel` | `GTKLookAndFeel` |
| **Posisi Menu Bar** | Di dalam jendela aplikasi | Di atas layar (*Global Menu*) | Di dalam jendela aplikasi |
| **Modifier Shortcut** | `Ctrl` (Control Key) | `Cmd / ⌘` (Meta Key) | `Ctrl` (Control Key) |
| **Bentuk Tombol (`JButton`)** | Kotak tegas (*flat rectangular*) | Lonjong membundar (*pill-shaped*) | Sedikit melengkung (*Yaru style*) |
| **Warna Aksen Fokus** | Biru muda Windows | Biru muda menyala (*Aqua glow*) | Oranye khas Ubuntu (*Aubergine/Orange*) |
| **Urutan Tombol Dialog** | Positif di kiri (`[OK] [Cancel]`) | Positif di kanan (`[Cancel] [OK]`) | Positif di kanan (`[Cancel] [OK]`) |
| **Gaya Tab (`JTabbedPane`)** | Tab persegi modern | *Segmented Control* abu-abu | Tab GTK bergaris bawah oranye |

---

## Bagian 5: Perbandingan Tampilan Visual (Screenshot Placeholder)

*(Berikut adalah tempat untuk menyisipkan tangkapan layar hasil eksekusi program pada masing-masing sistem operasi)*

<div style="display: flex; justify-content: space-between; align-items: flex-start; gap: 15px; margin-top: 15px; margin-bottom: 20px;">
  
  <!-- KOLOM 1: WINDOWS -->
  <div style="flex: 1; text-align: center; border: 1px solid #ddd; padding: 10px; border-radius: 8px;">
    <h4>🪟 Microsoft Windows</h4>
    <!-- GANTI DENGAN FILE SCREENSHOT WINDOWS ANDA -->
    <p><i>Tangkapan Layar Windows:</i></p>
    <img src="placeholder_windows.png" alt="Tampilan di Windows" style="max-width: 100%; border: 1px solid #ccc; border-radius: 4px;" />
    <br><small>Menu Bar menempel di jendela, tombol kotak tegas, aksen biru.</small>
  </div>
  
  <!-- KOLOM 2: MACOS -->
  <div style="flex: 1; text-align: center; border: 1px solid #ddd; padding: 10px; border-radius: 8px;">
    <h4>🍎 Apple macOS</h4>
    <!-- GANTI DENGAN FILE SCREENSHOT MACOS ANDA -->
    <p><i>Tangkapan Layar macOS:</i></p>
    <img src="placeholder_macos.png" alt="Tampilan di macOS" style="max-width: 100%; border: 1px solid #ccc; border-radius: 4px;" />
    <br><small>Menu Bar di atas layar monitor, tombol membulat, efek Aqua glow.</small>
  </div>

  <!-- KOLOM 3: UBUNTU LINUX -->
  <div style="flex: 1; text-align: center; border: 1px solid #ddd; padding: 10px; border-radius: 8px;">
    <h4>🐧 Linux Ubuntu (GTK)</h4>
    <!-- GANTI DENGAN FILE SCREENSHOT UBUNTU ANDA -->
    <p><i>Tangkapan Layar Ubuntu:</i></p>
    <img src="placeholder_ubuntu.png" alt="Tampilan di Ubuntu" style="max-width: 100%; border: 1px solid #ccc; border-radius: 4px;" />
    <br><small>Gaya visual GTK Yaru, aksen warna oranye, tombol dialog [Batal] [OK].</small>
  </div>

</div>

> **Petunjuk untuk Presentasi:**  
> Simpan tangkapan layar Anda di folder `tugas2` dengan nama `placeholder_windows.png`, `placeholder_macos.png`, dan `placeholder_ubuntu.png`. Gambar akan otomatis muncul berdampingan di atas secara rapi.

---

## Bagian 6: Analisis User Experience (UX) dan Kesimpulan

Dari praktikum dan pengujian lintas platform ini, dapat ditarik beberapa kesimpulan penting:

1.  **Konsistensi dengan Ekosistem (*Ecosystem Consistency*):**  
    Pengguna komputer memiliki memori otot (*muscle memory*) yang terbentuk dari sistem operasi harian mereka. Memaksa pengguna Mac menekan `Ctrl+S` atau memaksa pengguna Windows mencari menu di atas layar monitor akan menciptakan rasa frustrasi. Penyesuaian otomatis ini membuat aplikasi terasa seperti aplikasi bawaan (*first-class citizen*).
2.  **Kekuatan Arsitektur Pluggable Look and Feel (PLAF) Java:**  
    Java Swing membuktikan kekuatannya melalui arsitektur PLAF. Pengembang hanya perlu menulis satu basis logika kode (*single codebase*), dan Swing secara dinamis memetakan komponen tersebut ke `WindowsLookAndFeel`, `AquaLookAndFeel`, atau `GTKLookAndFeel`.
3.  **Detail Kecil yang Berdampak Besar:**  
    Perbedaan tidak hanya sebatas warna, melainkan juga pada urutan tombol dialog konfirmasi, jenis font sistem (`Segoe UI` di Windows, `San Francisco` di Mac, dan `Ubuntu Font` di Linux), serta perilaku navigasi keyboard (*mnemonic* Alt vs Apple Shortcuts). Memperhatikan detail ini adalah pembeda antara aplikasi amatir dan aplikasi komersial berstandar profesional.
