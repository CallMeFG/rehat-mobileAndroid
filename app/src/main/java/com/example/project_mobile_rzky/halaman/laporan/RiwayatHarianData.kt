package com.example.project_mobile_rzky.halaman.laporan

import android.content.Context

// Sumber data Laporan, dipakai bersama oleh LaporanFragment, RiwayatHarianFragment,
// PenyebabLelahFragment, dan LaporanMingguanActivity agar isinya selalu sama.
// Baris "Hari ini" dibaca dari Catatan Harian (SharedPreferences "rehat_data");
// tujuh hari sebelumnya adalah data contoh karena Rehat belum menyimpan riwayat per tanggal.
class RiwayatHarianData(private val context: Context) {

    fun ambilRiwayat(): List<RiwayatHarianModel> {
        val riwayat = mutableListOf<RiwayatHarianModel>()

        val dataPref = context.getSharedPreferences("rehat_data", Context.MODE_PRIVATE)
        if (dataPref.getBoolean("sudahIsiCatatan", false)) {
            riwayat.add(
                RiwayatHarianModel(
                    "Hari ini",
                    "dari Catatan Harian",
                    dataPref.getInt("energiAngka", 0),
                    dataPref.getString("energiStatus", "-").toString(),
                    dataPref.getString("lamaTidur", "-").toString(),
                    dataPref.getInt("menitTidur", 0),
                    dataPref.getString("bebanTugas", "-").toString(),
                    dataPref.getString("penyebabLelah", "Tidak ada").toString(),
                    dataPref.getString("perasaan", "-").toString()
                )
            )
        }

        // data contoh 7 hari sebelumnya (terbaru di atas)
        riwayat.add(RiwayatHarianModel("Sabtu", "14 Nov", 78, "Penuh", "7j 40m", 460, "0–1 tugas", "Tidak ada", "Istirahat penuh, sempat jalan pagi."))
        riwayat.add(RiwayatHarianModel("Jumat", "13 Nov", 58, "Cukup", "6j 10m", 370, "2–3 tugas", "Organisasi", "Rapat himpunan sampai sore."))
        riwayat.add(RiwayatHarianModel("Kamis", "12 Nov", 35, "Rendah", "4j 30m", 270, "4 tugas atau lebih", "Begadang", "Begadang menyelesaikan laporan praktikum."))
        riwayat.add(RiwayatHarianModel("Rabu", "11 Nov", 42, "Rendah", "4j 50m", 290, "4 tugas atau lebih", "Deadline", "Dua deadline jatuh di hari yang sama."))
        riwayat.add(RiwayatHarianModel("Selasa", "10 Nov", 55, "Cukup", "5j 40m", 340, "2–3 tugas", "Deadline", "Kuis dadakan, fokus terpecah."))
        riwayat.add(RiwayatHarianModel("Senin", "9 Nov", 64, "Cukup", "6j 30m", 390, "2–3 tugas", "Kuliah padat", "Kuliah pagi sampai sore tanpa jeda panjang."))
        riwayat.add(RiwayatHarianModel("Minggu", "8 Nov", 82, "Penuh", "8j 0m", 480, "0–1 tugas", "Tidak ada", "Libur, tidur cukup."))

        return riwayat
    }
}
