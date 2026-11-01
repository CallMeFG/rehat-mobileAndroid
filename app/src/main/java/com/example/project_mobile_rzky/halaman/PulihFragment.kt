package com.example.project_mobile_rzky.halaman

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.project_mobile_rzky.R
import com.example.project_mobile_rzky.databinding.FragmentPulihBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

// Ruang Pulih: latihan pemulihan energi dan checklist Mode Tidur.
// Latihan yang selesai dihitung sebagai sesi pulih di SharedPreferences "rehat_data".
class PulihFragment : Fragment() {

    // ViewBinding di Fragment (Modul P4 hlm 6, P10 hlm 11 | prak: TabCFragment.kt)
    private var _binding: FragmentPulihBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPulihBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tampilkanSesiPulih()

        // setiap kartu membuka langkah latihan
        binding.btnMulaiNapas.setOnClickListener {
            tampilkanLangkah(getString(R.string.pulih_napas_judul), getString(R.string.pulih_langkah_napas))
        }
        binding.cardPeregangan.setOnClickListener {
            tampilkanLangkah(getString(R.string.pulih_peregangan), getString(R.string.pulih_langkah_peregangan))
        }
        binding.cardPowerNap.setOnClickListener {
            tampilkanLangkah(getString(R.string.pulih_power_nap), getString(R.string.pulih_langkah_power_nap))
        }
        binding.cardJalanSantai.setOnClickListener {
            tampilkanLangkah(getString(R.string.pulih_jalan_santai), getString(R.string.pulih_langkah_jalan))
        }
        binding.cardSuaraHujan.setOnClickListener {
            tampilkanLangkah(getString(R.string.pulih_suara_hujan), getString(R.string.pulih_langkah_suara_hujan))
        }

        // Mode Tidur: singleSelection false -> checkedIds bisa berisi lebih dari satu id (Modul P9 hlm 9)
        binding.chipGroupModeTidur.setOnCheckedStateChangeListener { _, checkedIds ->
            val jumlahSelesai = checkedIds.size
            binding.tvProgresModeTidur.text = "$jumlahSelesai dari 3 persiapan tidur selesai"
            if (jumlahSelesai == 3) {
                Toast.makeText(requireContext(), "Semua persiapan selesai. Selamat beristirahat!", Toast.LENGTH_LONG).show()
            }
        }
    }

    // langkah latihan (MaterialAlertDialogBuilder, Modul P4 hlm 13 | prak: HomeFragment.kt)
    private fun tampilkanLangkah(judul: String, langkah: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(judul)
            .setMessage(langkah)
            .setNegativeButton("Nanti", null)
            .setPositiveButton("Selesai") { dialog, _ ->
                catatSesiPulih(judul)
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun catatSesiPulih(judul: String) {
        // putInt/getInt (SharedPreferences, Modul P6 hlm 7-8)
        val dataPref = requireContext().getSharedPreferences("rehat_data", Context.MODE_PRIVATE)
        val sesiPulih = dataPref.getInt("sesiPulih", 0) + 1
        val editor = dataPref.edit()
        editor.putInt("sesiPulih", sesiPulih)
        editor.apply()
        tampilkanSesiPulih()

        // Snackbar (Modul P4 hlm 13 | prak: HomeFragment.kt)
        Snackbar.make(binding.root, "$judul tercatat sebagai sesi pulih ke-$sesiPulih", Snackbar.LENGTH_LONG)
            .show()
    }

    private fun tampilkanSesiPulih() {
        val dataPref = requireContext().getSharedPreferences("rehat_data", Context.MODE_PRIVATE)
        val sesiPulih = dataPref.getInt("sesiPulih", 0)
        binding.tvRingkasanPulih.text = "Total sesi pulih: $sesiPulih"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
