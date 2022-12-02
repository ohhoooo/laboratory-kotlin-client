package com.irlab.crawlingapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import com.irlab.crawlingapp.R

class PreferenceFragment: PreferenceFragmentCompat() {

    private lateinit var logout: PreferenceScreen

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey)

        logout = findPreference("logout")!!

        logout.setOnPreferenceClickListener {
            val intent = Intent(requireContext(), GoogleLogin::class.java)
            intent.putExtra("SignCode",2)
            startActivity(intent)
            return@setOnPreferenceClickListener true
            // Kotlin에서 return@label 구문은 이 명령문이 반환하는 여러 중첩 함수 중에서 어떤 함수를 지정하는 데 사용됩니다.
        }
    }
}