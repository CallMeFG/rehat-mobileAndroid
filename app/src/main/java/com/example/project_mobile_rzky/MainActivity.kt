/*
 * ALUR BACA (urutan membuka file)
 * 1. AndroidManifest.xml            : daftar Activity; SplashScreenActivity = LAUNCHER; izin INTERNET untuk WebView.
 * 2. SplashScreenActivity.kt        : onCreate -> delay(2000), baca isLogin, pilih Login atau Main, finish().
 * 3. LoginActivity.kt               : btnLogin.setOnClickListener -> cek username == password, simpan isLogin, ke Main, finish().
 * 4. MainActivity.kt (file ini)     : wadah. onCreate -> Toolbar, BottomNavigationView + replaceFragment, menu, dialog logout.
 * 5. halaman/BerandaFragment.kt, FokusFragment.kt : Fragment paling dasar (onCreateView + inflate).
 * 6. halaman/PulihFragment.kt       : ListView + SimpleAdapter (onViewCreated).
 * 7. halaman/LaporanFragment.kt     : ListView + Custom Adapter -> LaporanModel.kt, LaporanAdapter.kt (getView), res/layout/item_laporan.xml.
 * 8. halaman/ProfilFragment.kt      : baca SharedPreferences di dalam Fragment (requireContext()).
 * 9. halaman/BantuanFragment.kt     : WebView, dibuka dari option menu "Bantuan".
 * Alur layar: Splash -> Login -> MainActivity (wadah) -> tab bawah mengganti Fragment (isi) di fragment_container.
 */
package com.example.project_mobile_rzky

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.project_mobile_rzky.databinding.ActivityMainBinding
import com.example.project_mobile_rzky.halaman.BantuanFragment
import com.example.project_mobile_rzky.halaman.BerandaFragment
import com.example.project_mobile_rzky.halaman.FokusFragment
import com.example.project_mobile_rzky.halaman.LaporanFragment
import com.example.project_mobile_rzky.halaman.ProfilFragment
import com.example.project_mobile_rzky.halaman.PulihFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // window insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // Toolbar (title + subtitle)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.app_name)
            subtitle = getString(R.string.toolbar_subtitle)
        }

        // baca SharedPreferences (getString)
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
        val username = sharedPref.getString("username", "Pengguna")
        binding.tvSapaan.text = "Halo, $username"

        replaceFragment(BerandaFragment(), addToBackStack = false)

        // Bottom navigation + ganti Fragment
        // R.id.navXxx harus sama dengan android:id di res/menu/bottom_nav_menu.xml
        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navBeranda -> {
                    replaceFragment(BerandaFragment(), addToBackStack = false)
                    true
                }
                R.id.navFokus -> {
                    replaceFragment(FokusFragment(), addToBackStack = false)
                    true
                }
                R.id.navPulih -> {
                    replaceFragment(PulihFragment(), addToBackStack = false)
                    true
                }
                R.id.navLaporan -> {
                    replaceFragment(LaporanFragment(), addToBackStack = false)
                    true
                }
                R.id.navProfil -> {
                    replaceFragment(ProfilFragment(), addToBackStack = false)
                    true
                }
                else -> false // false jika item tidak dikenal
            }
        }
    }

    // replaceFragment
    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
        if (addToBackStack) transaction.addToBackStack(null)
        transaction.commit()
    }

    // Option menu (inflate)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Option menu (klik item)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuBantuan -> {
                replaceFragment(BantuanFragment())
                true
            }
            R.id.menuLogout -> {
                tampilkanDialogLogout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Logout (AlertDialog + hapus SharedPreferences)
    private fun tampilkanDialogLogout() {
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.logout_judul))
            .setMessage(getString(R.string.logout_pesan))
            .setPositiveButton(getString(R.string.ya)) { dialog, _ ->
                // hapus data login
                val editor = sharedPref.edit()
                editor.clear()
                editor.apply()
                dialog.dismiss()

                // kembali ke login
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton(getString(R.string.batal)) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
