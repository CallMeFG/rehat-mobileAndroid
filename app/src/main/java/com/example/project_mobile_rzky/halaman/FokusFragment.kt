package com.example.project_mobile_rzky.halaman

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.project_mobile_rzky.R

class FokusFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment dasar (inflate layout).
        return inflater.inflate(R.layout.fragment_fokus, container, false)
    }
}