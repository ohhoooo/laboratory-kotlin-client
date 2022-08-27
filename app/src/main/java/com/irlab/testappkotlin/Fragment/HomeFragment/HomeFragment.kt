package com.irlab.testappkotlin.Fragment.HomeFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import com.irlab.testappkotlin.MainActivity2
import com.irlab.testappkotlin.R
import com.irlab.testappkotlin.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //툴바 설정
        binding.toolbarHome.inflateMenu(R.menu.menu_top_home)
        binding.toolbarHome.title = "모든 게시글"
        binding.toolbarHome.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.search -> {
                    val intent = Intent(context, MainActivity2::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        return binding.root
    }
}