package com.example.project_mobile_rzky.halaman

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.project_mobile_rzky.databinding.ItemLaporanBinding

// Custom Adapter
class LaporanAdapter(
    context: Context,
    private val laporan: List<LaporanModel>
) : ArrayAdapter<LaporanModel>(context, 0, laporan) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // satu baris = satu inflate item_laporan.xml
        val binding = ItemLaporanBinding.inflate(LayoutInflater.from(context), parent, false)
        val view = binding.root
        val data = laporan[position]

        binding.tvHari.text = data.hari
        binding.tvRingkasan.text = data.ringkasan
        binding.tvSkor.text = data.skor

        // klik baris dipasang di getView, sebelum return view
        view.setOnClickListener {
            Toast.makeText(context, "${data.hari}: skor ${data.skor}", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
