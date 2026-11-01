package com.example.project_mobile_rzky.halaman.panduan

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

// adapter ViewPager2 Panduan (Modul P9 hlm 16 | prak: Message/TutorialFragmentAdapter.kt)
class PanduanPagerAdapter(
    activity: FragmentActivity,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
