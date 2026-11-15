package com.example.project_mobile_rzky.halaman.laporan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_mobile_rzky.R
import com.example.project_mobile_rzky.databinding.FragmentRiwayatHarianBinding

// Tab 1 Laporan Mingguan: daftar riwayat harian dengan RecyclerView
class RiwayatHarianFragment : Fragment() {

    private var _binding: FragmentRiwayatHarianBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRiwayatHarianBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val riwayatList = RiwayatHarianData(requireContext()).ambilRiwayat()

        // adapter menerima lambda: aksi klik ditentukan di Fragment
        val riwayatAdapter = RiwayatHarianAdapter(riwayatList) { hariDipilih ->
            tampilkanDetail(hariDipilih)
        }
        // layoutManager dan adapter wajib dipasang (Modul P10 hlm 15)
        binding.rvRiwayatHarian.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = riwayatAdapter
        }
    }

    // detail satu hari + saran sesuai status energi
    private fun tampilkanDetail(hari: RiwayatHarianModel) {
        val saran = when (hari.status) {
            "Rendah" -> "Kurangi target tugas besok, tidur sebelum 23.00, dan mulai hari dengan Napas 4-7-8 di tab Pulih."
            "Cukup" -> "Pertahankan jeda 5 menit setiap selesai 25 menit fokus agar energi tidak turun."
            else -> "Hari yang baik. Pertahankan jam tidur dan pola jeda seperti hari ini."
        }
        val pesan = "Baterai energi: ${hari.energi}% (${hari.status})\n" +
                "Lama tidur: ${hari.tidur}\n" +
                "Beban tugas: ${hari.tugas}\n" +
                "Penyebab lelah: ${hari.penyebab}\n" +
                "Catatan: ${hari.catatan}\n\n" +
                "Saran: $saran"

        AlertDialog.Builder(requireContext())
            .setTitle("${hari.hari}, ${hari.tanggal}")
            .setMessage(pesan)
            .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
