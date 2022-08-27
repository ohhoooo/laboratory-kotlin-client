package com.irlab.testappkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.irlab.testappkotlin.Fragment.AlertFragment
import com.irlab.testappkotlin.Fragment.CommunityFragment
import com.irlab.testappkotlin.Fragment.HomeFragment.HomeFragment
import com.irlab.testappkotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var homeFragment: HomeFragment
    lateinit var alertFragment: AlertFragment
    lateinit var communityFragment: CommunityFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //초기 화면 지정(Fragment)
        homeFragment = HomeFragment()
        alertFragment = AlertFragment()
        communityFragment = CommunityFragment()
        supportFragmentManager.beginTransaction().replace(R.id.constraint, homeFragment).commit()

        //바텀 네비게이션뷰 리스너(화면전환)
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.tab1 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.constraint, homeFragment).commit()
                    true
                }
                R.id.tab2 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.constraint, communityFragment).commit()
                    true
                }
                else -> {
                    supportFragmentManager.beginTransaction().replace(R.id.constraint, alertFragment).commit()
                    true
                }
            }
        }
    }
}