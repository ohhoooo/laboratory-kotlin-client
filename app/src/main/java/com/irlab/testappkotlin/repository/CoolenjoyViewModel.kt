package com.irlab.testappkotlin.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.irlab.testappkotlin.network.*
import kotlinx.coroutines.flow.Flow

class CoolenjoyViewModel : ViewModel() {
    lateinit var retroService: CoolenjoyService

    init {
        retroService = CoolenjoyInstance.getRetroInstance().create(CoolenjoyService::class.java)
    }

    fun getListData(): Flow<PagingData<ItemModel>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { CoolenjoyPagingSource(retroService) }
        ).flow.cachedIn(viewModelScope)
    }
}