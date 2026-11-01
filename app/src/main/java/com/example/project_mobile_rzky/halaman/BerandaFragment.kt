package com.example.project_mobile_rzky.halaman

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.project_mobile_rzky.R
import com.example.project_mobile_rzky.databinding.FragmentBerandaBinding
import com.example.project_mobile_rzky.halaman.catatan.CatatanHarianActivity
import com.example.project_mobile_rzky.halaman.panduan.PanduanActivity

// Beranda: menampilkan baterai energi, ringkasan hari ini, dan rekomendasi
// dari data Catatan Harian (SharedPreferences "rehat_data").
class BerandaFragment : Fragment() {

    // ViewBinding di Fragment (Modul P4 hlm 6, P10 hlm 11 | prak: TabCFragment.kt)
    private var _binding: FragmentBerandaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBerandaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // di Fragment, this diganti requireContext()
        binding.btnIsiCatatan.setOnClickListener {
            startActivity(Intent(requireContext(), CatatanHarianActivity::class.java))
        }

        // ikon Panduan di Toolbar (prak: Message/MessageFragment.kt "//pertemuan 9")
        setHasOptionsMenu(true)
    }

    // onResume dipanggil lagi saat kembali dari CatatanHarianActivity,
    // jadi baterai energi langsung diperbarui (Lifecycle, Modul P4 hlm 18-21)
    override fun onResume() {
        super.onResume()
        tampilkanRingkasan()
    }

    private fun tampilkanRingkasan() {
        val dataPref = requireContext().getSharedPreferences("rehat_data", Context.MODE_PRIVATE)
        val sudahIsiCatatan = dataPref.getBoolean("sudahIsiCatatan", false)

        // waktu fokus diisi oleh tab Fokus (putInt/getInt)
        val menitFokus = dataPref.getInt("menitFokus", 0)
        binding.tvNilaiFokus.text = "$menitFokus menit"

        if (sudahIsiCatatan) {
            binding.tvPersenEnergi.text = dataPref.getString("energiPersen", "-")
            binding.tvStatusEnergi.text = dataPref.getString("energiStatus", "-")
            binding.tvPesanEnergi.text = dataPref.getString("energiPesan", "-")
            binding.tvNilaiTidur.text = dataPref.getString("lamaTidur", "-")
            binding.tvNilaiTugas.text = dataPref.getString("bebanTugas", "-")

            val tugasTerdekat = dataPref.getString("tugasTerdekat", "Belum diisi")
            binding.tvTugasTerdekat.text = "Tugas terdekat: $tugasTerdekat"

            binding.tvRekomendasiJudul.text = dataPref.getString("rekomendasiJudul", "-")
            binding.tvRekomendasiIsi.text = dataPref.getString("rekomendasiIsi", "-")
            binding.tvTidurJudul.text = dataPref.getString("tidurJudul", "-")
            binding.tvTidurIsi.text = dataPref.getString("tidurIsi", "-")

            binding.btnIsiCatatan.text = getString(R.string.beranda_btn_perbarui_catatan)
        }
        // jika belum isi catatan, teks bawaan dari layout (strings.xml) tetap tampil
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.beranda_panduan_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_panduan -> {
                startActivity(Intent(requireContext(), PanduanActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
