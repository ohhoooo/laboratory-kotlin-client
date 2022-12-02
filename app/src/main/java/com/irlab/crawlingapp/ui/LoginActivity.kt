package com.irlab.crawlingapp.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import com.google.android.gms.common.SignInButton
import com.irlab.crawlingapp.KakaoLogin
import com.irlab.crawlingapp.R
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val googleButton = findViewById<SignInButton>(R.id.GsignInButton)
        val kakaoLoginButton = findViewById<Button>(R.id.kakaoLoginButton)

        googleButton.setOnClickListener {
            val intent = Intent(applicationContext, GoogleLogin::class.java)
            intent.putExtra("SignCode",1)
            startActivity(intent)
            finish()
        }

        kakaoLoginButton.setOnClickListener {
            val intent = Intent(applicationContext, KakaoLogin::class.java)
            startActivity(intent)
            finish()
        }

        getHashKey()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun getHashKey() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            val signatures = info.signingInfo.apkContentsSigners
            val md = MessageDigest.getInstance("SHA")
            for (signature in signatures) {
                val md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val key = String(Base64.encode(md.digest(), 0))
                Log.d("Hash key:", "!!!!!!!$key!!!!!!")
            }
        } catch(e: Exception) {
            Log.e("name not found", e.toString())
        }
    }
}