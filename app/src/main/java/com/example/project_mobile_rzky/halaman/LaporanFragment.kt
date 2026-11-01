package com.example.project_mobile_rzky.halaman

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.project_mobile_rzky.R
import com.example.project_mobile_rzky.databinding.FragmentLaporanBinding
import com.example.project_mobile_rzky.halaman.laporan.LaporanMingguanActivity
import com.example.project_mobile_rzky.halaman.laporan.RiwayatHarianData

// Laporan: rata-rata tidur dan energi, total fokus, dan risiko burnout minggu ini.
class LaporanFragment : Fragment() {

    // ViewBinding di Fragment (Modul P4 hlm 6, P10 hlm 11 | prak: TabCFragment.kt)
    private var _binding: FragmentLaporanBinding? = null
    private val binding get() = _binding!!

    private var statusRisiko = "Rendah"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLaporanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hitungRingkasan()

        binding.btnCekBurnout.setOnClickListener {
            tampilkanCekBurnout()
        }

        binding.btnLihatLaporanHarian.setOnClickListener {
            startActivity(Intent(requireContext(), LaporanMingguanActivity::class.java))
        }
    }

    private fun hitungRingkasan() {
        val riwayatList = RiwayatHarianData(requireContext()).ambilRiwayat()

        // jumlahkan energi dan tidur, hitung hari tidur < 6 jam (for, Modul P2 hlm 8)
        var totalEnergi = 0
        var totalMenitTidur = 0
        var hariTidurKurang = 0
        for (hari in riwayatList) {
            totalEnergi += hari.energi
            totalMenitTidur += hari.menitTidur
            if (hari.menitTidur < 360) {
                hariTidurKurang += 1
            }
        }
        val rataEnergi = totalEnergi / riwayatList.size
        val rataMenitTidur = totalMenitTidur / riwayatList.size

        binding.tvRataTidur.text = "${rataMenitTidur / 60}j ${rataMenitTidur % 60}m"
        binding.tvRataEnergi.text = "$rataEnergi%"

        val dataPref = requireContext().getSharedPreferences("rehat_data", Context.MODE_PRIVATE)
        val menitFokus = dataPref.getInt("menitFokus", 0)
        binding.tvTotalFokus.text = "${menitFokus / 60}j ${menitFokus % 60}m"

        // aturan risiko burnout sederhana (if - else if - else, Modul P2 hlm 6)
        if (rataEnergi < 50 || hariTidurKurang >= 4) {
            statusRisiko = "Tinggi"
        } else if (rataEnergi < 65 || hariTidurKurang >= 2) {
            statusRisiko = "Sedang"
        } else {
            statusRisiko = "Rendah"
        }
        binding.tvStatusRisiko.text = statusRisiko
        binding.tvAlasanRisiko.text = "Dari ${riwayatList.size} hari tercatat, tidur di bawah 6 jam selama " +
                "$hariTidurKurang hari dan rata-rata baterai energi $rataEnergi%."
    }

    // cek burnout: gabungan risiko mingguan dan energi hari ini
    private fun tampilkanCekBurnout() {
        val dataPref = requireContext().getSharedPreferences("rehat_data", Context.MODE_PRIVATE)
        val energiHariIni = dataPref.getString("energiStatus", "belum diisi")

        if (statusRisiko == "Tinggi" || energiHariIni == "Rendah") {
            AlertDialog.Builder(requireContext())
                .setTitle("Perlu istirahat serius")
                .setMessage("Risiko burnout minggu ini $statusRisiko dan energi hari ini $energiHariIni. " +
                        "Kurangi target tugas, tidur sebelum 23.00, dan jangan memendam sendiri. " +
                        "Healing119 bisa dihubungi gratis di 119 ext 8.")
                .setPositiveButton("Buka Healing119") { dialog, _ ->
                    dialog.dismiss()
                    bukaBantuan()
                }
                .setNegativeButton(getString(R.string.batal)) { dialog, _ -> dialog.dismiss() }
                .show()
        } else {
            AlertDialog.Builder(requireContext())
                .setTitle("Hasil cek burnout")
                .setMessage("Risiko burnout minggu ini $statusRisiko dan energi hari ini $energiHariIni. " +
                        "Pertahankan jeda setiap 25 menit fokus dan Mode Tidur pukul 23.00.")
                .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    // ganti isi fragment_container milik MainActivity dengan BantuanFragment (Modul P6 hlm 17-18)
    private fun bukaBantuan() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, BantuanFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
