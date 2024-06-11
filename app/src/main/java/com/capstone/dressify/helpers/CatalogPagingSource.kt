package com.capstone.dressify.helpers

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.dressify.data.remote.api.ApiService
import com.capstone.dressify.data.remote.response.CatalogResponse
import com.capstone.dressify.data.remote.response.ClothingItemsItem

class CatalogPagingSource(private val apiService: ApiService): PagingSource<Int, ClothingItemsItem>() {
    override fun getRefreshKey(state: PagingState<Int, ClothingItemsItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ClothingItemsItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData: CatalogResponse = apiService.getProducts(position, params.loadSize)

            LoadResult.Page(
                data = responseData.clothingItems,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.clothingItems.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}