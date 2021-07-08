package me.rerere.zhiwang.api.zuowen

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ZuowenService {
    @GET("/v/articles/q")
    suspend fun getZuowenList(@Query("pageNum") page: Int, @Query("pageSize") pageSize: Int) : ZuowenResponse

    @GET("/v/articles/{id}/html")
    suspend fun getZuowenContent(@Path("id") id: String) : ZuowenContent
}