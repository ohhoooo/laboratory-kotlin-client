package com.irlab.testappkotlin.Fragment

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.irlab.testappkotlin.MainActivity2
import com.irlab.testappkotlin.R
import com.irlab.testappkotlin.databinding.FragmentCommunityBinding
import com.irlab.testappkotlin.databinding.FragmentHomeBinding

class CommunityFragment : Fragment() {

    lateinit var binding: FragmentCommunityBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)

        binding.button.setOnClickListener {
            val intent = Intent(context, MainActivity2::class.java)
            startActivity(intent)
        }
        return binding.root
    }
}