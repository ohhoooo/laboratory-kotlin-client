package com.irlab.crawlingapp.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.irlab.crawlingapp.databinding.FragmentQuasarzoneBinding
import com.irlab.crawlingapp.repository.QuasarzoneViewModel
import kotlinx.android.synthetic.main.fragment_quasarzone.*
import kotlinx.coroutines.flow.collectLatest

class QuasarzoneFragment : Fragment() {

    lateinit var binding: FragmentQuasarzoneBinding
    lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuasarzoneBinding.inflate(inflater, container, false)
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
            recyclerViewAdapter = RecyclerViewAdapter()
            binding.recyclerView.adapter = recyclerViewAdapter
        }
    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this).get(QuasarzoneViewModel::class.java)
        lifecycleScope.launchWhenCreated {
            viewModel.getListData().collectLatest {
                recyclerViewAdapter.submitData(it)
            }
        }
    }
}
/*
1. Flow는 Coroution상에서 Reactive한 프로그래밍을 할 수 있도록 만들어진 데이터 파이프 라인
2. getListData() : Flow 타입의 객체 반환
3. collectLatest : collectLatest의 인자로 들어가는 it은 flow에서 발행된 데이터를 순차적으로 받아
                   suspend fun을 수행한다.

   * collect vs collectLatest
    collect : 새로운 데이터가 들어오더라도 이전 데이터의 처리가 끝난 후에 새로운 데이터를 처리
    collectLatest : 새로운 데이터가 들어오면 이전 데이터의 처리를 강제 종료시키고 새로운 데이터를 처리
 */