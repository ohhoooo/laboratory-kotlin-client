package com.irlab.testappkotlin.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.irlab.testappkotlin.R
import com.irlab.testappkotlin.databinding.FragmentTabBinding

class TabFragment : Fragment() {

    private lateinit var binding: FragmentTabBinding
    private val tabTitleArray = arrayOf(
        "퀘이사존",
        "Tab2"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        val viewPager = binding.viewPager
        viewPager.adapter = ViewPagerAdapter(this)

        // 툴바
        binding.toolbarTab.inflateMenu(R.menu.menu_top_home) // 툴바 설정
        binding.toolbarTab.title = "모든 게시글" // 툴바 제목
        binding.toolbarTab.setOnMenuItemClickListener { // 툴바 리스너
            when (it.itemId) {
                R.id.search -> { // search 버튼을 눌렀을 때
                    val intent = Intent(context, MainActivity2::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()
    }
}