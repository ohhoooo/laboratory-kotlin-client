package com.irlab.crawlingapp.server

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.irlab.crawlingapp.R

class ReadPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_post)

        val title = findViewById<TextView>(R.id.rtitle)
        val likeNum = findViewById<TextView>(R.id.rlikenum)
        val userName = findViewById<TextView>(R.id.ruserName)
        val postTime = findViewById<TextView>(R.id.rpostTime)
        val contents = findViewById<TextView>(R.id.rcontents)

        val intent = intent

        title.text = intent.getStringExtra("제목")
        likeNum.text = intent.getStringExtra("좋아요 수")
        userName.text = intent.getStringExtra("작성자")
        postTime.text = intent.getStringExtra("작성일")
        contents.text = intent.getStringExtra("내용")
    }
}