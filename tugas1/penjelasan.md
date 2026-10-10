# Kontrak `equals()` dan `hashCode()` Sebagai Satu Paket dalam Java

## Bagian 1: Pendahuluan
Dalam bahasa pemrograman Java, membandingkan dua objek tidak semudah membandingkan dua tipe data primitif (seperti `int` atau `boolean`). Kita diwajibkan menggunakan *method* `equals()` untuk mengecek kesamaan isi data dari dua objek yang berbeda di memori. 

Namun, ada satu aturan emas (Golden Rule) di Java: **"Jika Anda meng-override `equals()`, Anda HARUS meng-override `hashCode()`"**. Materi ini akan mendemonstrasikan secara langsung apa malapetaka yang terjadi jika kita melanggar aturan ini, khususnya ketika kita menggunakan *Collection* berbasis Hash seperti `HashSet` atau `HashMap`.

---

## Bagian 2: Konsep Dasar
Sebelum masuk ke skenario masalah, mari kita pahami peran masing-masing metode:

*   **`equals(Object obj)`**: Bertugas mengecek apakah dua objek "secara logis" adalah sama (misalnya, memiliki NIM dan Nama yang persis sama).
*   **`hashCode()`**: Mengembalikan angka *integer* yang merepresentasikan objek tersebut. Collection seperti `HashSet` menggunakan nilai ini untuk menentukan di "keranjang" (bucket) mana objek akan disimpan. 

**Cara Kerja `HashSet`:**
1.  Saat objek ditambahkan, `HashSet` memanggil `hashCode()` objek tersebut untuk mencari lokasi keranjang penyimpanannya.
2.  Jika keranjang kosong, objek langsung masuk.
3.  Jika di keranjang sudah ada objek lain, barulah `HashSet` memanggil `equals()` untuk memastikan apakah kedua objek tersebut benar-benar sama. Jika `equals()` bernilai `true`, objek baru akan ditolak (karena `HashSet` menolak duplikasi).

---

## Bagian 3: Skenario Masalah (Tanpa `hashCode`)
Bayangkan kita membuat kelas `Mahasiswa` dengan atribut `nim` dan `nama`. Kita **hanya** meng-override `equals()` agar objek dianggap sama jika `nim` dan `nama`-nya sama.

Lalu, kita membuat dua objek `Mahasiswa` dengan data persis sama dan memasukkannya ke dalam `HashSet`:

```java
Mahasiswa m1 = new Mahasiswa("12345", "Budi Santoso", false);
Mahasiswa m2 = new Mahasiswa("12345", "Budi Santoso", false);

setMasalah.add(m1);
setMasalah.add(m2);
```

**Apa yang terjadi?**
Karena kita **tidak** meng-override `hashCode()`, Java menggunakan `hashCode()` bawaan yang meng-generate nilai hash berdasarkan **alamat memori** objek. 
Karena `m1` dan `m2` adalah dua objek berbeda (dibuat dengan keyword `new`), alamat memorinya berbeda, sehingga nilai hash-nya berbeda!

`HashSet` melihat nilai hash yang berbeda ini dan langsung menempatkan `m1` dan `m2` di "keranjang" yang **berbeda**. Karena mereka ada di keranjang yang beda, `HashSet` **tidak pernah memanggil** `equals()`. Akibatnya, `HashSet` mengira mereka adalah dua data yang berbeda, dan **terjadilah duplikasi data**. (Padahal sifat dasar `Set` adalah tidak boleh ada duplikat).

---

## Bagian 4: Skenario Solusi
Untuk menyelesaikan masalah di atas, kita **harus** meng-override `hashCode()`. Kita harus memastikan bahwa objek yang memiliki data yang sama menghasilkan *integer* hash yang sama pula.

Di kelas `Mahasiswa`, kita gunakan *utility class* dari Java:

```java
@Override
public int hashCode() {
    return Objects.hash(nim, nama);
}
```

Sekarang kita coba lagi:
```java
Mahasiswa m3 = new Mahasiswa("67890", "Siti Aminah", true);
Mahasiswa m4 = new Mahasiswa("67890", "Siti Aminah", true);

setSolusi.add(m3);
setSolusi.add(m4);
```

**Proses yang berjalan (Yang Benar):**
1. `m3` ditambahkan. `hashCode()` dipanggil, menghasilkan nilai X. Masuk ke keranjang X.
2. `m4` ditambahkan. `hashCode()` dipanggil, menghasilkan nilai X juga (karena data sama).
3. `HashSet` melihat ada objek lain di keranjang X. Lalu ia memanggil `m3.equals(m4)`.
4. `equals()` mengembalikan `true`. `HashSet` berkata: *"Oh, ini objek yang sama!"*.
5. `m4` **ditolak**, dan tidak ada duplikasi data di dalam Set!

---

## Bagian 5: Kesimpulan
1. **Selalu paketkan keduanya:** Jangan pernah meng-override `equals()` tanpa `hashCode()` (begitu juga sebaliknya).
2. **Kesesuaian:** Jika `obj1.equals(obj2)` adalah `true`, maka `obj1.hashCode() == obj2.hashCode()` **wajib** `true`.
3. Mengabaikan aturan ini tidak akan membuat kode menjadi *error* atau *crash* saat di-compile, namun akan menghasilkan **logical bug (bug logika)** yang sangat sulit dicari, di mana data yang seharusnya tunggal malah menjadi duplikat, merusak integritas data program kita.
