package com.example.project_mobile_rzky.halaman

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.project_mobile_rzky.R
import com.example.project_mobile_rzky.databinding.FragmentFokusBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Fokus: timer fokus 25 menit – jeda 5 menit, jeda panjang setiap 4 sesi.
// Sesi yang selesai disimpan ke SharedPreferences "rehat_data" untuk Beranda dan Profil.
class FokusFragment : Fragment() {

    // ViewBinding di Fragment (Modul P4 hlm 6, P10 hlm 11 | prak: TabCFragment.kt)
    private var _binding: FragmentFokusBinding? = null
    private val binding get() = _binding!!

    // mode aktif: "fokus", "jeda", atau "jedaPanjang"
    private var mode = "fokus"
    private var durasiDetik = 25 * 60
    private var timerBerjalan = false
    // nomor timer: mencegah dua perulangan berjalan bersamaan jika Berhenti lalu Mulai ditekan cepat
    private var nomorTimer = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFokusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tampilkanSesi()

        // tugas terdekat dari Catatan Harian
        val dataPref = requireContext().getSharedPreferences("rehat_data", Context.MODE_PRIVATE)
        val tugasTerdekat = dataPref.getString("tugasTerdekat", "Belum diisi")
        binding.tvTugasSedang.text = "Sedang dikerjakan: $tugasTerdekat"

        // pilih mode lewat ChipGroup (Modul P9 hlm 9 | prak: NinthActivity.kt)
        binding.chipGroupModeFokus.setOnCheckedStateChangeListener { _, checkedIds ->
            val selectedChipId = checkedIds.firstOrNull()
            if (selectedChipId != null) {
                when (selectedChipId) {
                    R.id.chipModeFokus -> {
                        mode = "fokus"
                        durasiDetik = 25 * 60
                    }
                    R.id.chipModeJeda -> {
                        mode = "jeda"
                        durasiDetik = 5 * 60
                    }
                    else -> {
                        mode = "jedaPanjang"
                        durasiDetik = 15 * 60
                    }
                }
                // ganti mode = timer lama dihentikan dan waktu diulang
                timerBerjalan = false
                tampilkanWaktu(durasiDetik)
                binding.tvStatusTimer.text = getString(R.string.fokus_status_siap)
            }
        }

        binding.btnMulaiTimer.setOnClickListener {
            if (timerBerjalan) {
                Toast.makeText(requireContext(), "Timer sedang berjalan", Toast.LENGTH_SHORT).show()
            } else {
                mulaiTimer()
            }
        }

        binding.btnBerhentiTimer.setOnClickListener {
            timerBerjalan = false
            tampilkanWaktu(durasiDetik)
            binding.tvStatusTimer.text = getString(R.string.fokus_status_berhenti)
        }
    }

    // hitung mundur: lifecycleScope.launch + delay (Modul P6 hlm 11-12 | prak: SplashScreen.kt)
    // digabung dengan for + downTo + break (Modul P2 hlm 5, 8, 10)
    private fun mulaiTimer() {
        timerBerjalan = true
        nomorTimer = nomorTimer + 1
        val nomorIni = nomorTimer
        if (mode == "fokus") {
            binding.tvStatusTimer.text = getString(R.string.fokus_status_fokus)
        } else {
            binding.tvStatusTimer.text = getString(R.string.fokus_status_jeda)
        }

        lifecycleScope.launch {
            for (detik in durasiDetik downTo 0) {
                // berhenti jika tombol Berhenti / ganti mode ditekan, atau timer baru sudah dimulai
                if (!timerBerjalan || nomorIni != nomorTimer) break
                tampilkanWaktu(detik)
                if (detik == 0) {
                    timerBerjalan = false
                    timerSelesai()
                } else {
                    delay(1000) // tunggu 1 detik
                }
            }
        }
    }

    // menampilkan detik sebagai menit:detik, contoh 1500 -> 25:00
    private fun tampilkanWaktu(totalDetik: Int) {
        val menit = totalDetik / 60
        val detik = totalDetik % 60
        if (detik < 10) {
            binding.tvSisaWaktu.text = "$menit:0$detik"
        } else {
            binding.tvSisaWaktu.text = "$menit:$detik"
        }
    }

    private fun timerSelesai() {
        if (mode == "fokus") {
            // simpan angka dengan putInt/getInt (SharedPreferences, Modul P6 hlm 7-8)
            val dataPref = requireContext().getSharedPreferences("rehat_data", Context.MODE_PRIVATE)
            val sesiFokus = dataPref.getInt("sesiFokus", 0) + 1
            val menitFokus = dataPref.getInt("menitFokus", 0) + 25
            val editor = dataPref.edit()
            editor.putInt("sesiFokus", sesiFokus)
            editor.putInt("menitFokus", menitFokus)
            editor.apply()
            tampilkanSesi()

            // setiap 4 sesi, sarankan jeda panjang
            val pesan = if (sesiFokus % 4 == 0) {
                "Kamu sudah fokus 4 sesi berturut-turut. Pilih Jeda panjang 15m lalu ketuk Mulai."
            } else {
                "Satu sesi fokus selesai. Pilih Jeda 5m lalu ketuk Mulai, dan coba salah satu saran di bawah."
            }
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.fokus_selesai_judul))
                .setMessage(pesan)
                .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
                .show()
        } else {
            Toast.makeText(requireContext(), "Jeda selesai. Pilih Fokus 25m untuk sesi berikutnya.", Toast.LENGTH_LONG).show()
        }
        binding.tvStatusTimer.text = getString(R.string.fokus_status_siap)
        tampilkanWaktu(durasiDetik)
    }

    // "Sesi X dari 4" dan total sesi
    private fun tampilkanSesi() {
        val dataPref = requireContext().getSharedPreferences("rehat_data", Context.MODE_PRIVATE)
        val sesiFokus = dataPref.getInt("sesiFokus", 0)
        val menitFokus = dataPref.getInt("menitFokus", 0)
        val sesiKe = (sesiFokus % 4) + 1
        binding.tvSesiKe.text = "Sesi $sesiKe dari 4"
        binding.tvRingkasanFokus.text = "Total: $sesiFokus sesi fokus · $menitFokus menit"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timerBerjalan = false // hentikan perulangan timer sebelum binding dikosongkan
        _binding = null
    }
}
