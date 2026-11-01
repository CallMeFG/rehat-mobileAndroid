package com.example.project_mobile_rzky.halaman

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.project_mobile_rzky.halaman.aturjeda.AturJedaActivity
import com.example.project_mobile_rzky.R

class BerandaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment dasar (inflate layout)
        return inflater.inflate(R.layout.fragment_beranda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // di Fragment, this diganti requireContext()
        val btnToAturJeda = view.findViewById<Button>(R.id.btnToAturJeda)
        btnToAturJeda.setOnClickListener {
            startActivity(Intent(requireContext(), AturJedaActivity::class.java))
        }
    }
}
