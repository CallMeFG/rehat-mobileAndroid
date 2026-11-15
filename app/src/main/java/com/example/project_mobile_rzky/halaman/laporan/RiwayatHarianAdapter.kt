package com.example.project_mobile_rzky.halaman.laporan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_mobile_rzky.databinding.ItemRiwayatHarianBinding

// Adapter RecyclerView riwayat harian
class RiwayatHarianAdapter(
    private val riwayatList: List<RiwayatHarianModel>,
    private val onItemClick: (RiwayatHarianModel) -> Unit
) : RecyclerView.Adapter<RiwayatHarianAdapter.RiwayatHarianViewHolder>() {

    inner class RiwayatHarianViewHolder(val binding: ItemRiwayatHarianBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatHarianViewHolder {
        val binding = ItemRiwayatHarianBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RiwayatHarianViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RiwayatHarianViewHolder, position: Int) {
        val item = riwayatList[position]
        with(holder.binding) {
            tvHariTanggal.text = "${item.hari}, ${item.tanggal}"
            tvEnergiHarian.text = "${item.energi}%"
            tvStatusHarian.text = "Energi ${item.status}"
            tvDetailHarian.text = "Tidur ${item.tidur} · ${item.tugas} · Penyebab lelah: ${item.penyebab}"
            tvCatatanHarian.text = "Catatan: ${item.catatan}"
            root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun getItemCount(): Int = riwayatList.size
}
