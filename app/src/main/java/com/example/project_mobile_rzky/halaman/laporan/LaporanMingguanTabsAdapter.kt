package com.example.project_mobile_rzky.halaman.laporan

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

// adapter tab Laporan Mingguan (Modul P10 hlm 5-6 | prak: TenthTabsAdapter.kt)
class LaporanMingguanTabsAdapter(activity: FragmentActivity) :
    FragmentStateAdapter(activity) {

    // jumlah tab harus sama dengan jumlah case di createFragment (Modul P10 hlm 6)
    override fun getItemCount(): Int = 2

    // Fragment yang ditampilkan berdasarkan posisi tab
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RiwayatHarianFragment()
            1 -> PenyebabLelahFragment()
            else -> throw IllegalStateException("Posisi tidak valid")
        }
    }
}
