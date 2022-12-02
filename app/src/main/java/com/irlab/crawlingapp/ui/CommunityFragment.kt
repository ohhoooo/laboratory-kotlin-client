package com.irlab.crawlingapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.irlab.crawlingapp.R
import com.irlab.crawlingapp.databinding.FragmentCommunityBinding
import com.irlab.crawlingapp.server.ItemList
import com.irlab.crawlingapp.server.PostActivity
import com.irlab.crawlingapp.server.PostAdapter
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CommunityFragment : Fragment() {

    lateinit var binding: FragmentCommunityBinding
    lateinit var handler: Handler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        binding.toolbar2.inflateMenu(R.menu.menu_community_top) // 툴바 설정
        binding.toolbar2.title = "커뮤니티 게시글" // 툴바 제목
        binding.toolbar2.setOnMenuItemClickListener { // 툴바 리스너
            when (it.itemId) {
                R.id.post -> { // post 버튼
                    val intent = Intent(context, PostActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        binding.recyclerView2.layoutManager = LinearLayoutManager(context)
        binding.recyclerView2.setHasFixedSize(true)
        binding.recyclerView2.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                val getBundle = msg.data.get("list")!!
                binding.recyclerView2.adapter = PostAdapter(getBundle as ArrayList<ItemList>)
            }
        }

        val a = RequestThread()
        a.start()

        binding.swipe2.setOnRefreshListener {
            val a = RequestThread()
            a.start()
            binding.swipe2.isRefreshing = false
        }
    }

    inner class RequestThread : Thread() {
        override fun run() {
            try {
                Log.d("133323123","@222")
                val putBundle = Bundle()
                val a = arrayListOf<ItemList>()
                val getURL = "http://203.232.193.162:5000/dbtoweb"
                var lines = ""
                val url = URL(getURL) // 웹서버 URL 저장
                val conn = url.openConnection() as HttpURLConnection // 웹서버에 연결
                if (conn != null) { // 만약 연결이 되었을 경우
                    conn.connectTimeout = 10000 // 10초 동안 기다린 후 응답이 없으면 종료
                    conn.requestMethod = "GET" // GET 메소드 : 웹 서버로 부터 리소스를 가져온다.
                    conn.doInput = true // 서버에서 온 데이터를 입력받을 수 있는 상태인가? true
                    conn.doOutput = true // 서버에서 온 데이터를 출력할 수 있는 상태인가? true
                    val resCode = conn.responseCode // 응답 코드를 리턴 받는다.
                    if (resCode == HttpURLConnection.HTTP_OK) { // 만약 응답 코드가 200(=OK)일 경우
                        val reader = BufferedReader(InputStreamReader(conn.inputStream))
                        // BufferedReader() : 엔터만 경계로 인식하고 받은 데이터를 String 으로 고정, Scanner 에 비해 빠름!
                        // InputStreamReader() : 지정된 문자 집합 내의 문자로 인코딩
                        // getInputStream() : url 에서 데이터를 읽어옴
                        var line: String? // 웹에서 가져올 데이터를 저장하기위한 변수
                        while (true) {
                            line = reader.readLine() // readLine() : 한 줄을 읽어오는 함수
                            if (line == null) // 만약 읽어올 줄이 없으면 break
                                break
                            lines += line
                        }

                        val json = JSONArray(lines)
                        for(i in 0 until json.length()) {
                            val jsonObject = json.getJSONObject(json.length() -1 -i)
                            Log.d("22323323213",json.length().toString())
                            Log.d("22323323213",jsonObject.getString("title"))

                            a.add(
                                ItemList(
                                    jsonObject.getString("title"),
                                    jsonObject.getString("username"),
                                    jsonObject.getString("likenum"),
                                    jsonObject.getString("postnum"),
                                    jsonObject.getString("contents"),
                                    jsonObject.getString("posttime")
                                )
                            )
                            Log.d("2323213",jsonObject.getString("title"))
                        }
                        putBundle.putParcelableArrayList("list", a)
                        val msg = handler!!.obtainMessage()
                        msg.data = putBundle
                        handler!!.sendMessage(msg)
                        reader.close() // 입력이 끝남
                    }
                    conn.disconnect() // DB연결 해제
                }

            } catch (e: Exception) { //예외 처리
                e.printStackTrace() // printStackTrace() : 에러 메세지의 발생 근원지를 찾아서 단계별로 에러를 출력
            }
        }
    }
}