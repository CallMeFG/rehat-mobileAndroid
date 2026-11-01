package com.example.project_mobile_rzky.halaman.panduan

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_mobile_rzky.databinding.ActivityPanduanBinding

// Panduan Rehat: 4 halaman cara memakai fitur, digeser dengan ViewPager2 + titik indikator
// (Modul P9 hlm 16-19 | prak: Message/TutorialMessageActivity.kt)
class PanduanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPanduanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPanduanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // urutan halaman = urutan alur pengguna di grand design
        val fragmentsList = listOf(
            PanduanCatatanFragment(),
            PanduanFokusFragment(),
            PanduanPulihFragment(),
            PanduanLaporanFragment()
        )
        val adapter = PanduanPagerAdapter(this, fragmentsList)
        binding.vpPanduan.adapter = adapter

        // attachTo dipanggil SETELAH adapter dipasang (Modul P9 hlm 19)
        binding.dotPanduan.attachTo(binding.vpPanduan)
    }
}
