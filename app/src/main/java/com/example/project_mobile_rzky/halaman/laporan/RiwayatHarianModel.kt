package com.example.project_mobile_rzky.halaman.laporan

// data class satu hari catatan untuk RecyclerView
data class RiwayatHarianModel(
    val hari: String,
    val tanggal: String,
    val energi: Int,        // baterai energi 0-100
    val status: String,     // Rendah / Cukup / Penuh
    val tidur: String,      // lama tidur untuk ditampilkan
    val menitTidur: Int,    // lama tidur dalam menit untuk menghitung rata-rata
    val tugas: String,
    val penyebab: String,
    val catatan: String
)
