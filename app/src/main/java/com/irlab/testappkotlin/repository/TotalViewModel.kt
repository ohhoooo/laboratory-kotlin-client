package com.irlab.testappkotlin.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.irlab.testappkotlin.network.*
import kotlinx.coroutines.flow.Flow

class TotalViewModel : ViewModel() {
    lateinit var service1: QuasarzoneService
    lateinit var service2: PpomppuService
    lateinit var service3: CoolenjoyService

    init {
        service1 = QuasarzoneInstance.getRetroInstance().create(QuasarzoneService::class.java)
        service2 = PpomppuInstance.getRetroInstance().create(PpomppuService::class.java)
        service3 = CoolenjoyInstance.getRetroInstance().create(CoolenjoyService::class.java)
    }

    fun getListData(): Flow<PagingData<ItemModel>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { TotalPagingSource(service1, service2, service3) }
        ).flow.cachedIn(viewModelScope)
    }
}