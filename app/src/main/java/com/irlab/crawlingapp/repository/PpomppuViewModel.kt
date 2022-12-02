package com.irlab.crawlingapp.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.irlab.crawlingapp.network.*
import kotlinx.coroutines.flow.Flow

class PpomppuViewModel : ViewModel() {
    lateinit var retroService: PpomppuService

    init {
        retroService = PpomppuInstance.getRetroInstance().create(PpomppuService::class.java)
    }

    fun getListData(): Flow<PagingData<ItemModel>> {
        return Pager(
            config = PagingConfig(pageSize = 15),
            pagingSourceFactory = { PpomppuPagingSource(retroService) }
        ).flow.cachedIn(viewModelScope)
    }
}