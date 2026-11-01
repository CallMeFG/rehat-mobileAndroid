package com.example.project_mobile_rzky.halaman.aturjeda

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_mobile_rzky.R
import com.example.project_mobile_rzky.databinding.ActivityAturJedaBinding
import com.google.android.material.chip.Chip

class AturJedaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAturJedaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAturJedaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Toolbar + tombol back
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.atur_jeda_judul)
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.toolbar.setNavigationOnClickListener { finish() }

        // deteksi chip yang dipilih
        binding.chipGroupFilter.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedChipId = checkedIds.firstOrNull() // ambil id chip yang dipilih
            if (selectedChipId != null) {
                val chip = group.findViewById<Chip>(selectedChipId)
                Toast.makeText(this, "Durasi jeda: ${chip.text}", Toast.LENGTH_SHORT).show()
                // logika filter di sini
            }
        }

        // klik tombol GridLayout menampilkan Toast
        binding.btnMenuNapas.setOnClickListener {
            Toast.makeText(this, "Jeda: ${binding.btnMenuNapas.text}", Toast.LENGTH_SHORT).show()
        }
        binding.btnMenuPeregangan.setOnClickListener {
            Toast.makeText(this, "Jeda: ${binding.btnMenuPeregangan.text}", Toast.LENGTH_SHORT).show()
        }
        binding.btnMenuPowerNap.setOnClickListener {
            Toast.makeText(this, "Jeda: ${binding.btnMenuPowerNap.text}", Toast.LENGTH_SHORT).show()
        }
        binding.btnMenuJalan.setOnClickListener {
            Toast.makeText(this, "Jeda: ${binding.btnMenuJalan.text}", Toast.LENGTH_SHORT).show()
        }
        binding.btnMenuMusik.setOnClickListener {
            Toast.makeText(this, "Jeda: ${binding.btnMenuMusik.text}", Toast.LENGTH_SHORT).show()
        }
        binding.btnMenuJurnal.setOnClickListener {
            Toast.makeText(this, "Jeda: ${binding.btnMenuJurnal.text}", Toast.LENGTH_SHORT).show()
        }
    }
}
