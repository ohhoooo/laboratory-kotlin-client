package com.irlab.crawlingapp.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return TotalFragment()
            1 -> return QuasarzoneFragment()
            2 -> return PpomppuFragment()
        }
        return CoolenjoyFragment()
    }
}