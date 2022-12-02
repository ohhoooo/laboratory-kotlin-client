package com.irlab.crawlingapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.irlab.crawlingapp.R
import kotlin.properties.Delegates

class GoogleLogin : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var SignCode by Delegates.notNull<Int>()
    private val RC_SIGN_IN = 9001
    private var tokenId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)

        SignCode = intent.getIntExtra("SignCode", 0)
        if(SignCode == 0) Log.d("GoogleLogin", "SignCode is 0")
        else if(SignCode == 1) googleLogin()
        else if(SignCode == 2) {
            signOut(mAuth)
            startActivity(Intent(applicationContext , LoginActivity::class.java))
            finish()
        }
    }

    private fun googleLogin() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                task.getResult(ApiException::class.java)?.let { account ->
                    tokenId = account.idToken
                    Log.d("26652421421","@32")
                    if(tokenId != null && tokenId != "") {
                        firebaseAuthWithGoogle(tokenId.toString())
                    }
                }
            }catch (e: ApiException) {
                Log.w("GoogleActivity","Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    Toast.makeText(applicationContext,"성공",Toast.LENGTH_LONG).show()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }else Toast.makeText(applicationContext, "실패", Toast.LENGTH_LONG).show()
                finish()
            }
    }

    private fun signOut(mAuth: FirebaseAuth) {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener {
                mAuth.signOut()
                Toast.makeText(applicationContext, "로그아웃", Toast.LENGTH_LONG).show()
            }
    }
}