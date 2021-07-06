package me.rerere.zhiwang.api.zuowen

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import me.rerere.zhiwang.repo.ZuowenRepo

private const val TAG = "ZuowenPageSource"

class ZuowenPageSource(
    private val zuowenRepo: ZuowenRepo
) : PagingSource<Int, ZuowenResponse.Article>() {
    override fun getRefreshKey(state: PagingState<Int, ZuowenResponse.Article>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ZuowenResponse.Article> {
        val page = params.key ?: 0
        Log.i(TAG, "loading page: $page, ${params.loadSize}")
        val data = zuowenRepo.getZuowen(pageNum = page, pageSize = params.loadSize)
        return if (data != null) {
            LoadResult.Page(
                prevKey = if (page <= 0) null else page - 1,
                nextKey = if(data.articles.size < params.loadSize) null else page + 1,
                data = data.articles
            )
        } else {
            LoadResult.Error(
                Exception("Failed to load zuowen list")
            )
        }
    }
}