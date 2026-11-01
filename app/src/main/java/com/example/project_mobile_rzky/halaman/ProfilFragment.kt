package com.example.project_mobile_rzky.halaman

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.project_mobile_rzky.LoginActivity
import com.example.project_mobile_rzky.R
import com.example.project_mobile_rzky.databinding.FragmentProfilBinding
import com.example.project_mobile_rzky.halaman.panduan.PanduanActivity

// Profil: identitas pengguna, statistik pemakaian, Healing119, dan menu pengaturan.
class ProfilFragment : Fragment() {

    // ViewBinding di Fragment (Modul P4 hlm 6, P10 hlm 11 | prak: TabCFragment.kt)
    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!

    // data menu Profil (judul + keterangan) untuk SimpleAdapter
    private val menuProfil = listOf(
        mapOf("title" to "Target tidur", "desc" to "23.00 – 06.30 · ketuk untuk melihat tips tidur"),
        mapOf("title" to "Durasi fokus", "desc" to "25 menit fokus · 5 menit jeda · jeda panjang tiap 4 sesi"),
        mapOf("title" to "Panduan Rehat", "desc" to "Cara memakai Catatan Harian, Fokus, Pulih, dan Laporan"),
        mapOf("title" to "Bantuan & layanan", "desc" to "Healing119 · layanan dukungan kesehatan jiwa"),
        mapOf("title" to "Tentang Rehat", "desc" to "Versi 1.0 · asisten anti-burnout mahasiswa"),
        mapOf("title" to "Keluar", "desc" to "Logout dan kembali ke halaman Login")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SharedPreferences di dalam Fragment (requireContext(), Modul P6 hlm 20)
        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "Pengguna")
        binding.tvNama.text = username

        // statistik dari tab Fokus, Pulih, dan Catatan Harian (getInt / getBoolean)
        val dataPref = requireContext().getSharedPreferences("rehat_data", Context.MODE_PRIVATE)
        binding.tvJumlahSesiFokus.text = "${dataPref.getInt("sesiFokus", 0)}"
        binding.tvJumlahSesiPulih.text = "${dataPref.getInt("sesiPulih", 0)}"
        if (dataPref.getBoolean("sudahIsiCatatan", false)) {
            binding.tvStatusCatatan.text = getString(R.string.profil_catatan_sudah)
        }

        binding.cardHealing119.setOnClickListener {
            bukaBantuan()
        }

        // ListView + SimpleAdapter
        // key di arrayOf harus sama dengan key di mapOf; lalu id text1/text2 di simple_list_item_2
        val adapter = SimpleAdapter(
            requireContext(),
            menuProfil,
            android.R.layout.simple_list_item_2,
            arrayOf("title", "desc"),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )
        binding.listMenuProfil.adapter = adapter

        // klik item ListView: setiap baris punya aksi sendiri
        binding.listMenuProfil.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> tampilkanInfo("Target tidur 23.00 – 06.30",
                    "Tidur 7–8 jam menjaga baterai energi tetap penuh. Mulai Mode Tidur di tab Pulih pukul 22.30, " +
                            "matikan notifikasi media sosial, dan simpan HP jauh dari kasur.")
                1 -> tampilkanInfo("Durasi fokus",
                    "Rehat memakai pola 25 menit fokus lalu 5 menit jeda. Setelah 4 sesi, ambil jeda panjang 15 menit. " +
                            "Atur di tab Fokus lewat pilihan Fokus 25m, Jeda 5m, atau Jeda panjang 15m.")
                2 -> startActivity(Intent(requireContext(), PanduanActivity::class.java))
                3 -> bukaBantuan()
                4 -> tampilkanInfo("Tentang Rehat",
                    "Rehat membantu mahasiswa mencatat tidur, beban tugas, dan energi, lalu menyarankan kapan fokus, " +
                            "jeda, dan tidur. Rehat bukan pengganti psikolog atau psikiater; kondisi krisis diarahkan " +
                            "ke Healing119 (119 ext 8).")
                5 -> tampilkanDialogLogout()
            }
        }
    }

    private fun tampilkanInfo(judul: String, isi: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(judul)
            .setMessage(isi)
            .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    // WebView Healing119 di fragment_container milik MainActivity (Modul P6 hlm 17-18)
    private fun bukaBantuan() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, BantuanFragment())
            .addToBackStack(null)
            .commit()
    }

    // Logout dari Fragment: pola praktikum startActivity + requireActivity().finish()
    // (Modul P7 hlm 9-10 | prak: HomeFragment.kt btnLogout)
    private fun tampilkanDialogLogout() {
        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.logout_judul))
            .setMessage(getString(R.string.logout_pesan))
            .setPositiveButton(getString(R.string.ya)) { dialog, _ ->
                val editor = sharedPref.edit()
                editor.clear()
                editor.apply()
                dialog.dismiss()

                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }
            .setNegativeButton(getString(R.string.batal)) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
