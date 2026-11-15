package com.example.project_mobile_rzky.halaman.tutorial

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_mobile_rzky.databinding.ActivityTutorialLaporanBinding

class TutorialLaporanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTutorialLaporanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTutorialLaporanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // kode insets bawaan (pola sama dengan MainActivity)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // setup ViewPager dengan adapter
        val fragmentsList = listOf(
            Tutorial1Fragment(),
            Tutorial2Fragment(),
            Tutorial3Fragment()
        )
        val adapter = TutorialFragmentAdapter(this, fragmentsList)
        binding.tutorialLaporanViewPager.adapter = adapter

        // attachTo dipanggil SETELAH adapter dipasang
        binding.dotIndicator.attachTo(binding.tutorialLaporanViewPager)
    }
}
