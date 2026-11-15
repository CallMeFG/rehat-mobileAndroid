package com.example.project_mobile_rzky.halaman.laporan

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_mobile_rzky.R
import com.example.project_mobile_rzky.databinding.ActivityLaporanMingguanBinding
import com.google.android.material.tabs.TabLayoutMediator

// Laporan Mingguan: TabLayout + ViewPager2 + TabLayoutMediator
class LaporanMingguanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaporanMingguanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLaporanMingguanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Toolbar + tombol back
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.laporan_mingguan_judul)
            subtitle = getString(R.string.laporan_mingguan_subjudul)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        // jumlah hari di riwayat untuk badge angka
        val jumlahHari = RiwayatHarianData(this).ambilRiwayat().size

        // 1. Inisialisasi adapter
        val tabsAdapter = LaporanMingguanTabsAdapter(this)
        // 2. Pasang adapter ke ViewPager2
        binding.vpLaporanMingguan.adapter = tabsAdapter
        // 3. Hubungkan TabLayout dan ViewPager2
        TabLayoutMediator(binding.tabLaporanMingguan, binding.vpLaporanMingguan) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.tab_riwayat_harian)
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.ic_tab_riwayat)
                    // badge dengan angka: jumlah hari tercatat
                    val badge = tab.getOrCreateBadge()
                    badge.isVisible = true
                    badge.number = jumlahHari
                }
                1 -> {
                    tab.text = getString(R.string.tab_penyebab_lelah)
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.ic_tab_penyebab)
                    // badge tanpa angka (titik): ada saran yang perlu dibaca
                    val badge = tab.getOrCreateBadge()
                    badge.isVisible = true
                }
            }
        }.attach()
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
