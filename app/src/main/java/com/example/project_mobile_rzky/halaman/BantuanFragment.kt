package com.example.project_mobile_rzky.halaman

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.project_mobile_rzky.databinding.FragmentBantuanBinding

class BantuanFragment : Fragment() {

    // ViewBinding di Fragment (Modul P4 hlm 6, P10 hlm 11 | prak: TabCFragment.kt)
    private var _binding: FragmentBantuanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBantuanBinding.inflate(inflater, container, false)
        return binding.root
    }

    // prak: WebViewActivity.kt memakai @SuppressLint("SetJavaScriptEnabled")
    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // WebView (webViewClient, javaScriptEnabled, loadUrl)
        binding.webView.webViewClient = WebViewClient()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl("https://www.healing119.id")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
