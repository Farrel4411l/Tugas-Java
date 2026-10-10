# Pembuatan GUI Lintas-Platform dengan Java Swing

## Bagian 1: Pendahuluan
Salah satu slogan paling terkenal dari bahasa pemrograman Java adalah **"Write Once, Run Anywhere"** (Tulis sekali, jalankan di mana saja). Namun, membuat aplikasi dapat *berjalan* di mana saja tidak berarti aplikasi tersebut akan *terasa* senatural aplikasi bawaan (native) di setiap sistem operasi.

Dalam desain Antarmuka Pengguna (GUI), Windows dan macOS memiliki perbedaan *User Experience* (UX) yang sangat mencolok (misalnya posisi *Menu Bar* dan penggunaan tombol pintasan/ *shortcut*). Proyek ini mendemonstrasikan bagaimana kita bisa menulis satu kode program Java Swing, yang dengan cerdas dapat beradaptasi dan menyesuaikan tampilannya secara otomatis tergantung di OS mana ia dijalankan.

---

## Bagian 2: Deteksi OS dan Penyesuaian Kode
Untuk membuat aplikasi kita "sadar" dengan lingkungannya, kita perlu mendeteksi Sistem Operasi saat aplikasi pertama kali dijalankan. Berikut adalah penyesuaian logika yang dilakukan pada kode:

**1. Mengaktifkan System Look and Feel**
Secara bawaan, Java Swing menggunakan tema "Metal" yang terlihat kuno dan sama di semua OS. Untuk mengubahnya menjadi tema asli bawaan OS, kita menggunakan sintaks:
```java
UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
```
Ini membuat tombol dan jendela terlihat seperti aplikasi Windows sungguhan saat di Windows, dan menggunakan tema *Aqua* saat dijalankan di macOS.

**2. Memindahkan Posisi Menu Bar (Khusus macOS)**
Di Windows, Menu File menempel langsung pada bagian bawah judul jendela aplikasi. Namun di macOS, standar desainnya adalah menggunakan **Global Screen Menu Bar** (Menu berada di ujung paling atas layar monitor, terpisah dari jendela).
Kita menyesuaikan ini dengan kode:
```java
String osName = System.getProperty("os.name");
boolean isMac = osName.toLowerCase().contains("mac");

if (isMac) {
    System.setProperty("apple.laf.useScreenMenuBar", "true");
}
```

**3. Penyesuaian Shortcut Keys (Command vs Control)**
Pengguna Windows terbiasa menyimpan file dengan `Ctrl + S`. Sedangkan pengguna macOS menggunakan `Cmd (Command) + S`. Memaksa pengguna Mac memakai `Ctrl` akan sangat merusak *User Experience*. Kode kita secara otomatis mengubah *modifier key* ini berdasarkan OS yang terdeteksi:
```java
int shortcutKeyMask = isMac ? InputEvent.META_DOWN_MASK : InputEvent.CTRL_DOWN_MASK;
menuItemSimpan.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcutKeyMask));
```

---

## Bagian 3: Analisis Antarmuka Pengguna (UI)
Dengan menggunakan strategi adaptasi di atas, aplikasi Java Swing kita kini tidak lagi terasa kaku.
*   Pada platform **Windows**, aplikasi akan menghormati batas jendela. Segala aktivitas interaksi berpusat di dalam `JFrame`.
*   Pada platform **macOS**, aplikasi akan menyatu dengan ekosistem Mac, di mana Menu berpindah ke atas layar (Status bar Mac), dan *shortcut* menjadi familiar dengan jari pengguna Mac (menggunakan tombol *Command* `⌘`).

---

## Bagian 4: Perbandingan Tampilan macOS dan Windows
*(Di bawah ini adalah perbandingan nyata ketika aplikasi yang sama dijalankan pada OS yang berbeda)*

<div style="display: flex; justify-content: space-between; align-items: flex-start; gap: 20px;">
  <div style="flex: 1; text-align: center;">
    <b>Tampilan Windows 🪟</b><br><br>
    <!-- GANTI DENGAN GAMBAR SCREENSHOT WINDOWS ANDA -->
    ![Tampilan Windows](placeholder_windows.png)
  </div>
  
  <div style="flex: 1; text-align: center;">
    <b>Tampilan macOS 🍎</b><br><br>
    <!-- GANTI DENGAN GAMBAR SCREENSHOT MACOS ANDA -->
    ![Tampilan macOS](placeholder_macos.png)
  </div>
</div>

> **Catatan Presenter:** Pada gambar di atas, perhatikan letak tulisan menu "File". Di gambar Windows, menu berada tepat di bawah judul "Demo GUI Lintas Platform". Sementara di macOS, menu File tersebut berada di baris atas layar sistem terpisah dari jendela putih.

---

## Bagian 5: Kesimpulan
Membuat aplikasi *Cross-Platform* bukan sekadar membuat aplikasi yang "bisa dibuka" tanpa *error*, melainkan juga soal **empati terhadap kebiasaan pengguna (User Experience/UX)**. 

Melalui deteksi sederhana `System.getProperty("os.name")` di Java, kita dapat mengakomodasi perbedaan *keyboard shortcut* (Cmd vs Ctrl), penempatan navigasi (*Global Menu Bar*), dan tema visual (*System Look and Feel*). Pendekatan ini memastikan aplikasi kita terasa bagaikan warga asli (*first-class citizen*) di sistem operasi manapun ia dijalankan.
