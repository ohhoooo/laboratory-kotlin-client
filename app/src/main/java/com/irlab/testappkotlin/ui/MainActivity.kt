package com.irlab.testappkotlin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.irlab.testappkotlin.R
import com.irlab.testappkotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var alertFragment: SettingsFragment
    private lateinit var communityFragment: CommunityFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기 화면 지정(Fragment)
        homeFragment = HomeFragment()
        alertFragment = SettingsFragment()
        communityFragment = CommunityFragment()
        supportFragmentManager.beginTransaction().add(R.id.constraint, alertFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.constraint, communityFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.constraint, homeFragment).commit()
        supportFragmentManager.beginTransaction().hide(alertFragment).commit()
        supportFragmentManager.beginTransaction().hide(communityFragment).commit()

        // 바텀 네비게이션뷰 리스너(화면전환)
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.tab1 -> {
                    if(binding.bottomNavigation.selectedItemId == R.id.tab1) {
                        homeFragment.binding.recyclerView.scrollToPosition(0)
                    }else {
                        supportFragmentManager.beginTransaction().hide(alertFragment).commit()
                        supportFragmentManager.beginTransaction().hide(communityFragment).commit()
                        supportFragmentManager.beginTransaction().show(homeFragment).commit()
                    }
                    true
                }
                R.id.tab2 -> {
                    supportFragmentManager.beginTransaction().hide(alertFragment).commit()
                    supportFragmentManager.beginTransaction().hide(homeFragment).commit()
                    supportFragmentManager.beginTransaction().show(communityFragment).commit()
                    true
                }
                else -> {
                    supportFragmentManager.beginTransaction().hide(communityFragment).commit()
                    supportFragmentManager.beginTransaction().hide(homeFragment).commit()
                    supportFragmentManager.beginTransaction().show(alertFragment).commit()
                    true
                }
            }
        }
    }
}