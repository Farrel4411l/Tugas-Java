package tugas1;

import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        // ========================================================================
        // SKENARIO 1: MASALAH (HANYA MENG-OVERRIDE EQUALS() TANPA HASHCODE())
        // ========================================================================
        System.out.println("=== Skenario Masalah: Tanpa override hashCode() ===");
        
        // Membuat HashSet untuk menyimpan koleksi objek Mahasiswa unik
        HashSet<Mahasiswa> setMasalah = new HashSet<>();
        
        // Membuat dua objek berbeda di memori, tetapi memiliki data (nim dan nama) yang persis sama.
        // Argumen 'false' mematikan implementasi hashCode() yang benar.
        Mahasiswa m1 = new Mahasiswa("12345", "Budi Santoso", false);
        Mahasiswa m2 = new Mahasiswa("12345", "Budi Santoso", false);
        
        // Menambahkan kedua objek ke dalam HashSet
        setMasalah.add(m1);
        setMasalah.add(m2);
        
        // Menampilkan isi HashSet
        // BUKTI MASALAH: Karena hashcode berbeda, HashSet mengira mereka diletakkan di "keranjang" yang berbeda.
        // Walaupun equals() mengembalikan true, HashSet tidak sempat mengecek equals() jika hash-nya berbeda.
        // Akibatnya, terjadi duplikasi data di dalam Set!
        System.out.println("Isi HashSet Masalah (Harusnya 1, tapi malah " + setMasalah.size() + "):");
        for (Mahasiswa m : setMasalah) {
            System.out.println("- " + m);
        }
        System.out.println();
        
        
        // ========================================================================
        // SKENARIO 2: SOLUSI (MENG-OVERRIDE EQUALS() DAN HASHCODE() BERSAMAAN)
        // ========================================================================
        System.out.println("=== Skenario Solusi: Dengan override hashCode() ===");
        
        // Membuat HashSet baru untuk pengujian solusi
        HashSet<Mahasiswa> setSolusi = new HashSet<>();
        
        // Membuat dua objek dengan data sama.
        // Argumen 'true' menyalakan implementasi hashCode() yang menggunakan Objects.hash(nim, nama).
        Mahasiswa m3 = new Mahasiswa("67890", "Siti Aminah", true);
        Mahasiswa m4 = new Mahasiswa("67890", "Siti Aminah", true);
        
        // Menambahkan kedua objek ke dalam HashSet
        setSolusi.add(m3);
        setSolusi.add(m4);
        
        // Menampilkan isi HashSet
        // BUKTI SOLUSI: Karena hashcode sama, HashSet akan meletakkannya di "keranjang" yang sama.
        // Lalu HashSet mengecek equals(). Karena equals() true, HashSet tahu ini barang yang sama.
        // Objek kedua akan ditolak/diabaikan, sehingga Set tetap unik.
        System.out.println("Isi HashSet Solusi (Sesuai harapan, jumlahnya " + setSolusi.size() + "):");
        for (Mahasiswa m : setSolusi) {
            System.out.println("- " + m);
        }
    }
}
