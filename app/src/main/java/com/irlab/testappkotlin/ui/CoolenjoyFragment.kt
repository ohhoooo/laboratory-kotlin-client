package com.irlab.testappkotlin.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.irlab.testappkotlin.databinding.FragmentCoolenjoyBinding
import com.irlab.testappkotlin.repository.CoolenjoyViewModel
import kotlinx.android.synthetic.main.fragment_quasarzone.*
import kotlinx.coroutines.flow.collectLatest

class CoolenjoyFragment : Fragment() {

    lateinit var binding: FragmentCoolenjoyBinding
    lateinit var recyclerViewAdapter: CoolenjoyRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCoolenjoyBinding.inflate(inflater, container, false)
        init()
        initRecyclerView()
        initViewModel()
        return binding.root
    }

    private fun init() {
        // 화면 새로고침
        binding.swipe.setOnRefreshListener {
            setUpSwipeRefresh()
        }
    }

    // 화면 새로고침
    // 구글링해서 나온 방법들이나 detach, attach 로는 해결이 안되서 생각한 방법
    // 실제 출시된 어플들이랑 동작방식이 비슷한거 같다
    private fun setUpSwipeRefresh() {
        initRecyclerView()
        initViewModel()
        binding.swipe.isRefreshing = false // 스레드 종료 시, 리프레시 종료
    }

    private fun initRecyclerView() {
        recyclerView.apply {
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.setHasFixedSize(true)
            binding.recyclerView.addItemDecoration( // item 간 구분선이나 여백을 설정할 때
                CustomDecoration(3f, 30f, Color.GRAY)
            )
            recyclerViewAdapter = CoolenjoyRecyclerViewAdapter()
            binding.recyclerView.adapter = recyclerViewAdapter
        }
    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this).get(CoolenjoyViewModel::class.java)
        lifecycleScope.launchWhenCreated {
            viewModel.getListData().collectLatest {
                recyclerViewAdapter.submitData(it)
            }
        }
    }
}