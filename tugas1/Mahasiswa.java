package tugas1;

import java.util.Objects;

// Mendefinisikan class Mahasiswa sesuai permintaan tugas
public class Mahasiswa {
    // Atribut dasar
    private String nim;
    private String nama;
    
    // Flag khusus untuk mendemonstrasikan skenario dengan dan tanpa hashCode() dalam satu run
    private boolean gunakanHashCode;

    // Konstruktor untuk inisialisasi objek
    public Mahasiswa(String nim, String nama, boolean gunakanHashCode) {
        this.nim = nim;
        this.nama = nama;
        this.gunakanHashCode = gunakanHashCode;
    }

    // Meng-override equals() untuk membandingkan ISI dari objek (nim dan nama)
    @Override
    public boolean equals(Object obj) {
        // 1. Jika referensi memori sama, pasti objeknya sama
        if (this == obj) return true;
        // 2. Jika obj null atau berasal dari class yang berbeda, pasti tidak sama
        if (obj == null || getClass() != obj.getClass()) return false;
        // 3. Casting (mengubah tipe) obj menjadi Mahasiswa agar atributnya bisa diakses
        Mahasiswa mahasiswa = (Mahasiswa) obj;
        // 4. Membandingkan nim dan nama (return true jika keduanya persis sama)
        return Objects.equals(nim, mahasiswa.nim) && Objects.equals(nama, mahasiswa.nama);
    }

    // Meng-override hashCode() secara kondisional untuk keperluan demonstrasi dua skenario
    @Override
    public int hashCode() {
        if (gunakanHashCode) {
            // SKENARIO SOLUSI: Menghasilkan nilai hash berdasarkan atribut (nim dan nama)
            // Objek dengan nim dan nama yang sama akan memiliki nilai hash yang SAMA.
            return Objects.hash(nim, nama);
        } else {
            // SKENARIO MASALAH: Menggunakan hashCode() bawaan Java (Object.hashCode())
            // Walaupun isi datanya sama, nilai hash akan BERBEDA karena alamat memorinya berbeda.
            return super.hashCode();
        }
    }

    // Meng-override toString() agar mudah dibaca saat di-print ke console
    @Override
    public String toString() {
        return "Mahasiswa{nim='" + nim + "', nama='" + nama + "'}";
    }
}
