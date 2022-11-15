package com.irlab.testappkotlin.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.irlab.testappkotlin.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        val user = FirebaseAuth.getInstance().currentUser
        val mAuth = FirebaseAuth.getInstance()

        binding.toolbarSetting.title = "더 보기"
        binding.logoutButton.setOnClickListener {
            val intent = Intent(requireContext(), GoogleLogin::class.java)
            intent.putExtra("SignCode",2)
            startActivity(intent)
        }
    }
}