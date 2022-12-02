package com.irlab.crawlingapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.irlab.crawlingapp.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar) // 툴바 등록
        supportActionBar?.setTitle("") // 제목 설정
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로 가기 버튼 활성화
    }
}