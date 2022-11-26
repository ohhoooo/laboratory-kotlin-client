package com.irlab.testappkotlin.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return QuasarzoneFragment()
            1 -> return PpomppuFragment()
        }
        return CoolenjoyFragment()
    }
}