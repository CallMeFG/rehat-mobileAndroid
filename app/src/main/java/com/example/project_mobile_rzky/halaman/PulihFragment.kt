package com.example.project_mobile_rzky.halaman

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.project_mobile_rzky.R

class PulihFragment : Fragment() {

    // data ListView (judul + deskripsi)
    private val dataListWithDesc = listOf(
        mapOf("title" to "Latihan napas 4-7-8", "desc" to "Tarik napas 4 detik, tahan 7, buang 8"),
        mapOf("title" to "Peregangan leher", "desc" to "Putar pelan ke kiri dan kanan selama 1 menit"),
        mapOf("title" to "Power nap 15 menit", "desc" to "Tidur singkat agar kepala kembali segar"),
        mapOf("title" to "Jalan santai", "desc" to "Keluar ruangan dan berjalan 10 menit"),
        mapOf("title" to "Minum air putih", "desc" to "Satu gelas untuk mengurangi lelah"),
        mapOf("title" to "Dengar musik tenang", "desc" to "Satu lagu tanpa membuka ponsel"),
        mapOf("title" to "Tulis tiga hal baik", "desc" to "Catat hal kecil yang berjalan lancar hari ini"),
        mapOf("title" to "Matikan layar", "desc" to "Istirahat mata 20 detik dari layar")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate layout fragment
        return inflater.inflate(R.layout.fragment_pulih, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ListView + SimpleAdapter
        // key di arrayOf harus sama dengan key di mapOf; lalu id text1/text2 di simple_list_item_2
        val listAktivitas = view.findViewById<ListView>(R.id.listAktivitas)
        val adapter = SimpleAdapter(
            requireContext(),
            dataListWithDesc,
            android.R.layout.simple_list_item_2,
            arrayOf("title", "desc"),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )
        listAktivitas.adapter = adapter

        // klik item ListView + Toast
        listAktivitas.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = dataListWithDesc[position]
            val title = selectedItem["title"]
            val desc = selectedItem["desc"]
            Toast.makeText(requireContext(), "Kamu memilih: $title ($desc)", Toast.LENGTH_SHORT).show()
        }
    }
}
