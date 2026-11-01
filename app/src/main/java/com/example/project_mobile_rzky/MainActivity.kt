/*
 * ALUR BACA (urutan membuka file)
 * 1. AndroidManifest.xml              : daftar Activity; SplashScreenActivity = LAUNCHER; izin INTERNET untuk WebView.
 * 2. SplashScreenActivity.kt          : delay(2000), baca isLogin, pilih Login atau Main, finish().
 * 3. LoginActivity.kt                 : cek username == password, simpan isLogin + username ("user_pref"), ke Main.
 * 4. MainActivity.kt (file ini)       : Toolbar, header bg_header_langit, BottomNavigationView + replaceFragment,
 *                                       Panduan sekali (tutorial_seen), option menu Bantuan/Logout.
 * 5. halaman/BerandaFragment.kt       : baterai energi + rekomendasi dari "rehat_data" (onResume), ikon Panduan.
 *    halaman/catatan/CatatanHarianActivity.kt : ChipGroup, GridLayout, TextInputLayout -> hitung energi, simpan.
 * 6. halaman/FokusFragment.kt         : timer fokus-jeda (lifecycleScope + delay + for downTo), putInt sesi.
 * 7. halaman/PulihFragment.kt         : kartu latihan (GridLayout + MaterialCardView), MaterialAlertDialog, Snackbar,
 *                                       Mode Tidur (ChipGroup multi-pilih).
 * 8. halaman/LaporanFragment.kt       : rata-rata + risiko burnout dari halaman/laporan/RiwayatHarianData.kt.
 *    halaman/laporan/LaporanMingguanActivity.kt : TabLayout + ViewPager2 + TabLayoutMediator, ikon & badge.
 *      - RiwayatHarianFragment.kt + RiwayatHarianAdapter.kt + item_riwayat_harian.xml : RecyclerView.
 *      - PenyebabLelahFragment.kt + PenyebabLelahAdapter.kt + item_penyebab_lelah.xml : ListView + Custom Adapter.
 * 9. halaman/ProfilFragment.kt        : statistik, Healing119, ListView + SimpleAdapter, logout dari Fragment.
 * 10. halaman/panduan/PanduanActivity.kt : ViewPager2 + SpringDotsIndicator, 4 Fragment langkah pemakaian.
 * 11. halaman/BantuanFragment.kt      : WebView Healing119.
 * Alur layar: Splash -> Login -> MainActivity (wadah) -> tab bawah mengganti Fragment di fragment_container.
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
import com.example.project_mobile_rzky.halaman.panduan.PanduanActivity

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

        // Panduan tampil otomatis sekali setelah login (Modul P9 latihan 7.5, key tutorial_seen)
        // setelah itu dibuka dari ikon Panduan di Beranda atau menu Profil
        val tutorialSeen = sharedPref.getBoolean("tutorial_seen", false)
        if (!tutorialSeen) {
            val editor = sharedPref.edit()
            editor.putBoolean("tutorial_seen", true)
            editor.apply()
            startActivity(Intent(this, PanduanActivity::class.java))
        }

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
