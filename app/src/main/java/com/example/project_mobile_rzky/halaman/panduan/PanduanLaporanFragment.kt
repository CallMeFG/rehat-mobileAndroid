package com.example.project_mobile_rzky.halaman.panduan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.project_mobile_rzky.databinding.FragmentPanduanLaporanBinding

// Panduan langkah 4 dari 4 (Tutorial Screen, Modul P9 hlm 14 | prak: Message/Tutorial1Fragment.kt)
class PanduanLaporanFragment : Fragment() {

    // ViewBinding karena halaman ini punya tombol (Modul P4 hlm 6, P10 hlm 11)
    private var _binding: FragmentPanduanLaporanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPanduanLaporanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // tombol Mulai di halaman terakhir menutup Panduan (Modul P9 latihan 7.4)
        binding.btnMulaiPakaiRehat.setOnClickListener {
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
