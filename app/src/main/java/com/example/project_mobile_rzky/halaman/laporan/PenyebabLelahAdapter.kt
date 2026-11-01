package com.example.project_mobile_rzky.halaman.laporan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.project_mobile_rzky.databinding.ItemPenyebabLelahBinding

// Custom Adapter ListView (Modul P7 hlm 17-18 | prak: MessageAdapter.kt)
class PenyebabLelahAdapter(
    context: Context,
    private val penyebabList: List<PenyebabLelahModel>
) : ArrayAdapter<PenyebabLelahModel>(context, 0, penyebabList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // satu baris = satu inflate item_penyebab_lelah.xml
        val binding = ItemPenyebabLelahBinding.inflate(LayoutInflater.from(context), parent, false)
        val view = binding.root
        val data = penyebabList[position]

        binding.tvNamaPenyebab.text = data.penyebab
        binding.tvJumlahPenyebab.text = data.jumlah
        binding.tvSaranPenyebab.text = data.saran

        // klik baris dipasang di getView, sebelum return view
        view.setOnClickListener {
            Toast.makeText(context, "${data.penyebab}: ${data.saran}", Toast.LENGTH_LONG).show()
        }

        return view
    }
}
