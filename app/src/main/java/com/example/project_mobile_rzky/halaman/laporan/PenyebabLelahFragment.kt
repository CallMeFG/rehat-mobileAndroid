package com.example.project_mobile_rzky.halaman.laporan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.project_mobile_rzky.databinding.FragmentPenyebabLelahBinding

// Tab 2 Laporan Mingguan: penyebab lelah terbanyak dengan ListView + Custom Adapter (Modul P7 hlm 16-18)
class PenyebabLelahFragment : Fragment() {

    private var _binding: FragmentPenyebabLelahBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPenyebabLelahBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // hitung berapa hari setiap penyebab muncul di riwayat (for + when, Modul P2 hlm 7-8)
        var deadline = 0
        var begadang = 0
        var organisasi = 0
        var kuliah = 0
        var pribadi = 0
        val riwayatList = RiwayatHarianData(requireContext()).ambilRiwayat()
        for (hari in riwayatList) {
            when (hari.penyebab) {
                "Deadline" -> deadline += 1
                "Begadang" -> begadang += 1
                "Organisasi" -> organisasi += 1
                "Kuliah padat" -> kuliah += 1
                "Urusan pribadi" -> pribadi += 1
            }
        }

        val penyebabList = listOf(
            PenyebabLelahModel("Deadline", "$deadline hari", "Pecah tugas besar menjadi bagian kecil dan kerjakan satu bagian per sesi fokus."),
            PenyebabLelahModel("Begadang", "$begadang hari", "Pakai Mode Tidur di tab Pulih 30 menit sebelum pukul 23.00."),
            PenyebabLelahModel("Organisasi", "$organisasi hari", "Sisakan satu malam tanpa rapat setiap minggu untuk istirahat."),
            PenyebabLelahModel("Kuliah padat", "$kuliah hari", "Ambil Peregangan 5 menit di sela pergantian kelas."),
            PenyebabLelahModel("Urusan pribadi", "$pribadi hari", "Ceritakan ke teman atau hubungi Healing119 (119 ext 8) bila terasa berat.")
        )

        // ListView + Custom Adapter
        val adapter = PenyebabLelahAdapter(requireContext(), penyebabList)
        binding.listPenyebabLelah.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
