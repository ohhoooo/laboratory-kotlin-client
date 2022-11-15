package com.irlab.testappkotlin.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.irlab.testappkotlin.network.*
import kotlinx.coroutines.flow.Flow
import retrofit2.create

class QuasarzoneViewModel : ViewModel() {
    lateinit var retroService: QuasarzoneService

    init {
        retroService = QuasarzoneInstance.getRetroInstance().create(QuasarzoneService::class.java)
    }

    fun getListData(): Flow<PagingData<ItemModel>> {
        return Pager(
            config = PagingConfig(pageSize = 15), // 각 페이지에 로드해야 하는 항목 수
            pagingSourceFactory = { PagingSource(retroService) } // PagingSource 인스턴스 생성
        ).flow.cachedIn(viewModelScope)
    }
}
/*
1. cachedIn() : 데이터 스트림을 공유 가능하게 하며 제공된 CoroutineScope 을 사용하여 로드된 데이터를 저장 ( ex_ viewModelScope)
2. Pager 객체는 PagingSource 객체에서 load() 메소드를 호출하여 LoadParams 객체를 제공하고 반환되는 LoadResult 객체를 수신
3. PagingConfig 클래스는 로드 대기 시간, 초기 로드의 크기 요청등 PagingSource 에서 콘텐츠를 로드하는 방법에 관한 옵션을 설정
 */