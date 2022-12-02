package com.irlab.crawlingapp.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.irlab.crawlingapp.databinding.FragmentTotalBinding
import com.irlab.crawlingapp.repository.TotalViewModel
import kotlinx.android.synthetic.main.fragment_quasarzone.*
import kotlinx.coroutines.flow.collectLatest

class TotalFragment : Fragment() {

    lateinit var binding: FragmentTotalBinding
    lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTotalBinding.inflate(inflater, container, false)
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
            recyclerViewAdapter = RecyclerViewAdapter()
            binding.recyclerView.adapter = recyclerViewAdapter
        }
    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this).get(TotalViewModel::class.java)
        lifecycleScope.launchWhenCreated {
            viewModel.getListData().collectLatest {
                recyclerViewAdapter.submitData(it)
            }
        }
    }
}