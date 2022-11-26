package com.irlab.testappkotlin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.irlab.testappkotlin.R
import com.irlab.testappkotlin.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.fragment_ppomppu.view.recyclerView
import kotlinx.android.synthetic.main.fragment_tab.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tabFragment: TabFragment
    private lateinit var communityFragment: CommunityFragment
    private lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기 화면 지정(Fragment)
        tabFragment = TabFragment()
        communityFragment = CommunityFragment()
        settingsFragment = SettingsFragment()

        supportFragmentManager.beginTransaction().add(R.id.constraint, tabFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.constraint, settingsFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.constraint, communityFragment).commit()
        supportFragmentManager.beginTransaction().hide(settingsFragment).commit()
        supportFragmentManager.beginTransaction().hide(communityFragment).commit()

        // 바텀 네비게이션뷰 리스너(화면전환)
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.tab1 -> {
                    if(binding.bottomNavigation.selectedItemId == R.id.tab1) {
                        when (tabFragment.viewPager.currentItem) {
                            0 -> {
                                tabFragment.viewPager.recyclerView.scrollToPosition(0)
                            }
                            1 -> {
                                tabFragment.viewPager.recyclerView.scrollToPosition(0)
                            }
                            2 -> {
                                tabFragment.viewPager.recyclerView.scrollToPosition(0)
                            }
                        }
                    }else {
                        supportFragmentManager.beginTransaction().hide(settingsFragment).commit()
                        supportFragmentManager.beginTransaction().hide(communityFragment).commit()
                        supportFragmentManager.beginTransaction().show(tabFragment).commit()
                    }
                    true
                }
                R.id.tab2 -> {
                    supportFragmentManager.beginTransaction().hide(settingsFragment).commit()
                    supportFragmentManager.beginTransaction().hide(tabFragment).commit()
                    supportFragmentManager.beginTransaction().show(communityFragment).commit()
                    true
                }
                else -> {
                    supportFragmentManager.beginTransaction().hide(communityFragment).commit()
                    supportFragmentManager.beginTransaction().hide(tabFragment).commit()
                    supportFragmentManager.beginTransaction().show(settingsFragment).commit()
                    true
                }
            }
        }
    }
}