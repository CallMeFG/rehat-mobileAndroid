package com.example.project_mobile_rzky.halaman.catatan

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_mobile_rzky.R
import com.example.project_mobile_rzky.databinding.ActivityCatatanHarianBinding
import com.google.android.material.chip.Chip

// Catatan Harian: pengguna mengisi tidur, beban tugas, energi, dan penyebab lelah.
// Hasilnya (baterai energi + rekomendasi) disimpan ke SharedPreferences "rehat_data"
// lalu dibaca oleh BerandaFragment.
class CatatanHarianActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatatanHarianBinding

    // nilai awal sama dengan chip yang android:checked="true" di layout
    private var labelTidur = "5–7 jam"
    private var poinTidur = 25
    private var labelTugas = "2–3 tugas"
    private var poinTugas = 15
    private var labelEnergi = "Biasa"
    private var poinEnergi = 20
    private var penyebabDipilih = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCatatanHarianBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Toolbar + tombol back (Modul P5 hlm 2 | prak: NinthActivity.kt, TenthActivity.kt)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.catatan_judul)
            subtitle = getString(R.string.catatan_subjudul)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        // ChipGroup: deteksi chip yang dipilih (Modul P9 hlm 9 | prak: NinthActivity.kt)
        binding.chipGroupTidur.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedChipId = checkedIds.firstOrNull()
            if (selectedChipId != null) {
                val chip = group.findViewById<Chip>(selectedChipId)
                labelTidur = chip.text.toString()
                poinTidur = when (selectedChipId) {
                    R.id.chipTidurKurang -> 10
                    R.id.chipTidurSedang -> 25
                    else -> 40
                }
            } else {
                labelTidur = ""
            }
        }

        binding.chipGroupTugas.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedChipId = checkedIds.firstOrNull()
            if (selectedChipId != null) {
                val chip = group.findViewById<Chip>(selectedChipId)
                labelTugas = chip.text.toString()
                poinTugas = when (selectedChipId) {
                    R.id.chipTugasRingan -> 25
                    R.id.chipTugasSedang -> 15
                    else -> 5
                }
            } else {
                labelTugas = ""
            }
        }

        binding.chipGroupEnergi.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedChipId = checkedIds.firstOrNull()
            if (selectedChipId != null) {
                val chip = group.findViewById<Chip>(selectedChipId)
                labelEnergi = chip.text.toString()
                poinEnergi = when (selectedChipId) {
                    R.id.chipEnergiLelah -> 5
                    R.id.chipEnergiBiasa -> 20
                    else -> 35
                }
            } else {
                labelEnergi = ""
            }
        }

        // GridLayout: tombol penyebab lelah (Modul P9 hlm 12-13)
        binding.btnPenyebabDeadline.setOnClickListener { pilihPenyebab(binding.btnPenyebabDeadline.text.toString()) }
        binding.btnPenyebabBegadang.setOnClickListener { pilihPenyebab(binding.btnPenyebabBegadang.text.toString()) }
        binding.btnPenyebabOrganisasi.setOnClickListener { pilihPenyebab(binding.btnPenyebabOrganisasi.text.toString()) }
        binding.btnPenyebabKuliah.setOnClickListener { pilihPenyebab(binding.btnPenyebabKuliah.text.toString()) }
        binding.btnPenyebabPribadi.setOnClickListener { pilihPenyebab(binding.btnPenyebabPribadi.text.toString()) }
        binding.btnPenyebabTidakAda.setOnClickListener { pilihPenyebab(binding.btnPenyebabTidakAda.text.toString()) }

        binding.btnSimpanCatatan.setOnClickListener {
            simpanCatatan()
        }
    }

    // menandai penyebab lelah yang dipilih
    private fun pilihPenyebab(nama: String) {
        penyebabDipilih = nama
        binding.tvPenyebabDipilih.text = "Dipilih: $nama"
    }

    private fun simpanCatatan() {
        // semua pertanyaan chip wajib dijawab
        if (labelTidur.isEmpty() || labelTugas.isEmpty() || labelEnergi.isEmpty()) {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.catatan_belum_lengkap_judul))
                .setMessage(getString(R.string.catatan_belum_lengkap_pesan))
                .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
                .show()
            return
        }

        // baterai energi = poin tidur + poin tugas + poin energi (maksimal 100)
        val skor = poinTidur + poinTugas + poinEnergi

        // perkiraan lama tidur (menit) untuk rata-rata di Laporan
        val menitTidur = when (poinTidur) {
            10 -> 270  // kurang dari 5 jam, dianggap 4j 30m
            25 -> 360  // 5-7 jam, dianggap 6j
            else -> 465 // lebih dari 7 jam, dianggap 7j 45m
        }

        val status: String
        val pesan: String
        if (skor < 45) {
            status = "Rendah"
            pesan = "Energimu menipis. Ambil jeda di tab Pulih sebelum lanjut belajar."
        } else if (skor < 70) {
            status = "Cukup"
            pesan = "Masih aman, tapi sempatkan jeda setiap selesai satu sesi fokus."
        } else {
            status = "Penuh"
            pesan = "Energi penuh. Waktu yang pas untuk mengerjakan tugas terberat."
        }

        var tugasTerdekat = binding.etTugasTerdekat.text.toString().trim()
        if (tugasTerdekat.isEmpty()) {
            tugasTerdekat = "Belum diisi"
        }
        var perasaan = binding.etPerasaan.text.toString().trim()
        if (perasaan.isEmpty()) {
            perasaan = "-"
        }
        if (penyebabDipilih.isEmpty()) {
            penyebabDipilih = "Tidak ada"
        }

        // rekomendasi 1: aktivitas sesuai status energi
        val rekomendasiJudul: String
        val rekomendasiIsi: String
        when (status) {
            "Rendah" -> {
                rekomendasiJudul = "Napas 4-7-8 · 3 menit"
                rekomendasiIsi = "Buka tab Pulih dan tenangkan tubuh dulu sebelum membuka tugas."
            }
            "Cukup" -> {
                rekomendasiJudul = "Peregangan · 5 menit"
                rekomendasiIsi = "Lakukan setelah satu sesi fokus 25 menit selesai."
            }
            else -> {
                rekomendasiJudul = "Sesi Fokus 25 menit"
                rekomendasiIsi = "Buka tab Fokus dan kerjakan \"$tugasTerdekat\" selagi energi penuh."
            }
        }

        // rekomendasi 2: jam tidur sesuai lama tidur semalam
        val tidurJudul: String
        val tidurIsi: String
        if (poinTidur == 10) {
            tidurJudul = "Tidur lebih awal, pukul 22.30"
            tidurIsi = "Semalam tidurmu kurang dari 5 jam. Redupkan layar 30 menit sebelumnya."
        } else {
            tidurJudul = "Tidur pukul 23.00"
            tidurIsi = "Jaga jam tidur agar baterai energi besok tetap stabil."
        }

        // simpan ke SharedPreferences (Modul P6 hlm 7-8 | prak: LoginActivity.kt)
        // file "rehat_data" terpisah dari "user_pref" agar catatan tidak terhapus saat logout
        val dataPref = getSharedPreferences("rehat_data", MODE_PRIVATE)
        val editor = dataPref.edit()
        editor.putBoolean("sudahIsiCatatan", true)
        editor.putInt("energiAngka", skor) // angka untuk Laporan (putInt)
        editor.putInt("menitTidur", menitTidur)
        editor.putString("energiPersen", "$skor%")
        editor.putString("energiStatus", status)
        editor.putString("energiPesan", pesan)
        editor.putString("lamaTidur", labelTidur)
        editor.putString("bebanTugas", labelTugas)
        editor.putString("rasaEnergi", labelEnergi)
        editor.putString("tugasTerdekat", tugasTerdekat)
        editor.putString("perasaan", perasaan)
        editor.putString("penyebabLelah", penyebabDipilih)
        editor.putString("rekomendasiJudul", rekomendasiJudul)
        editor.putString("rekomendasiIsi", rekomendasiIsi)
        editor.putString("tidurJudul", tidurJudul)
        editor.putString("tidurIsi", tidurIsi)
        editor.apply()

        Toast.makeText(this, "Catatan tersimpan. Baterai energi: $skor%", Toast.LENGTH_SHORT).show()
        finish() // kembali ke Beranda
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
