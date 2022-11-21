package com.irlab.testappkotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.irlab.testappkotlin.ui.MainActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import java.util.Objects

class KakaoLogin : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        kakaoLogin()
    }

    private fun kakaoLogin() {
        val callback : (OAuthToken?, Throwable?) -> Unit = {token, error ->
            if(error != null) {
                Log.e("카카오 로그인", "카카오계정으로 로그인 실패", error)
            }else if(token != null) {
                Log.i("카카오 로그인", "카카오계정으로 로그인 성공 ${token.accessToken}")
                UserApiClient.instance.me { user, error ->
                    if(error != null) {
                        Log.e("loginInFirebase", "사용자 정보 가져오기 : Failure", error)
                    }else if(user != null) {
                        val scopes = mutableListOf<String>()
                        if(user.kakaoAccount!!.emailNeedsAgreement == true) scopes.add("account_email")

                        if(scopes.isEmpty()) {
                            val userEmail = Objects.requireNonNull(user.kakaoAccount!!.email)
                            val userID = Objects.requireNonNull(user.id.toString())
                            val userName = user.kakaoAccount!!.profile!!.nickname
                            loginFirebase(userEmail!!, userID, userName!!)
                        }else {
                            UserApiClient.instance.loginWithNewScopes(applicationContext, scopes) { token, error ->
                                if(error != null) {

                                } else {
                                    UserApiClient.instance.me { user, error ->
                                        if(error != null) {

                                        }else if(user!!.kakaoAccount!!.emailNeedsAgreement == false) {
                                            val userEmail = Objects.requireNonNull(user.kakaoAccount!!.email)
                                            val userID = Objects.requireNonNull(user.id.toString())
                                            val userName = user.kakaoAccount!!.profile!!.nickname
                                            loginFirebase(userEmail!!, userID, userName!!)
                                        }else if(user.kakaoAccount!!.emailNeedsAgreement == true) {
                                            UserApiClient.instance.logout {
                                                if(it != null) {

                                                }else {

                                                }
                                                return@logout
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Log.d("424","@45124")
        if(UserApiClient.instance.isKakaoTalkLoginAvailable(applicationContext)) {
            UserApiClient.instance.loginWithKakaoTalk(applicationContext) {token, error ->
                if(error != null) {
                    Log.e("카카오 로그인", "카카오톡으로 로그인 실패", error)

                    if(error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    UserApiClient.instance.loginWithKakaoAccount(applicationContext, callback = callback)
                }else if(token != null) {
                    Log.i("카카오 로그인", "카카오톡으로 로그인 성공 ${token.accessToken}")
                }
            }
        }else {
            UserApiClient.instance.loginWithKakaoAccount(applicationContext, callback = callback)
        }
    }

    private fun loginFirebase(userEmail: String, userID: String, userName: String) {
        mAuth.signInWithEmailAndPassword(userEmail, userID)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    Log.d("login in firebase", "success")
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(applicationContext, "성공했습니다.(카카오 로그인)", Toast.LENGTH_LONG).show()
                }else {
                    createIDInFirebase(userEmail, userID, userName)
                    Log.w("login in firebase", "failure")
                    Toast.makeText(applicationContext, "실패했습니다.(카카오 로그인)", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun createIDInFirebase(userEmail: String, userID: String, userName: String) {
        try {
            mAuth.createUserWithEmailAndPassword(userEmail, userID)
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        val user = mAuth.currentUser
                        val profileUpdate = UserProfileChangeRequest.Builder()
                            .setDisplayName(userName)
                            .build()
                        user!!.updateProfile(profileUpdate)
                            .addOnCompleteListener {
                                if(it.isSuccessful) {
                                    Log.d("makefirebaseId", "닉네임 설정 성공" + user.displayName)
                                }
                            }
                        loginFirebase(userEmail, userID, userName)
                    }
                }
        }catch (e: Exception) {
            Log.e("CreateIdFirebase", "사용자 생성 : Failure", e)
        }
    }
}