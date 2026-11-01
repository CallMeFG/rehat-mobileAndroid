package com.example.project_mobile_rzky.halaman

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.example.project_mobile_rzky.R
import com.example.project_mobile_rzky.halaman.tutorial.TutorialLaporanActivity

class LaporanFragment : Fragment() {

    // data untuk Custom Adapter.
    private val laporanList = listOf(
        LaporanModel("Senin", "Tidur cukup, fokus 3 sesi", "82"),
        LaporanModel("Selasa", "Tugas menumpuk, jeda kurang", "64"),
        LaporanModel("Rabu", "Peregangan rutin, energi stabil", "76"),
        LaporanModel("Kamis", "Begadang, energi turun", "55"),
        LaporanModel("Jumat", "Power nap membantu", "70"),
        LaporanModel("Sabtu", "Istirahat penuh", "90"),
        LaporanModel("Minggu", "Persiapan kuliah, cukup tenang", "78"),
        LaporanModel("Rata-rata", "Risiko burnout rendah", "73")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate layout fragment
        return inflater.inflate(R.layout.fragment_laporan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // option menu di Toolbar, tombol Info membuka tutorial.
        // Toolbar sudah ada di MainActivity, jadi setSupportActionBar tidak diulang di sini.
        requireActivity().addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.laporan_toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_tutorial -> {
                        startActivity(Intent(requireContext(), TutorialLaporanActivity::class.java))
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // ListView + Custom Adapter
        val listLaporan = view.findViewById<ListView>(R.id.listLaporan)
        val adapter = LaporanAdapter(requireContext(), laporanList)
        listLaporan.adapter = adapter
    }
}
