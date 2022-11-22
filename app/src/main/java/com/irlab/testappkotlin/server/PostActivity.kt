package com.irlab.testappkotlin.server

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.irlab.testappkotlin.R
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PostActivity : AppCompatActivity() {

    private val sendURL = "http://203.232.193.162:5000/sendserver"
    private lateinit var mAuth : FirebaseAuth
    private lateinit var user : FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val btnSave = findViewById<Button>(R.id.button)
        val txtTitle = findViewById<EditText>(R.id.editTextTextPersonName)
        val txtContents = findViewById<EditText>(R.id.editTextTextMultiLine)
        mAuth = FirebaseAuth.getInstance()
        user = mAuth.currentUser!!

        btnSave.setOnClickListener {
            val savePostThread = SavePostThread(txtTitle.text, txtContents.text)
            savePostThread.start()
            finish()
        }
    }

    inner class SavePostThread(val title: Editable, private val contents: Editable) : Thread() {
        override fun run() {
            val date = Calendar.getInstance()
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            try {
                val client = OkHttpClient()
                val jsonInput = JSONObject()
                jsonInput.put("title", title)
                jsonInput.put("contents", contents)
                jsonInput.put("userName", "임시") // 로그인과 코드 병합할때 고쳐야됨
                jsonInput.put("time", formatter.format(date.time))
                val reqBody: RequestBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    jsonInput.toString()
                )
                val request = Request.Builder()
                    .post(reqBody)
                    .url(sendURL)
                    .build()
                client.newCall(request).execute()
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}