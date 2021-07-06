package me.rerere.zhiwang.api.zhiwang

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ZhiWangService {
    @POST("/v1/api/check")
    @Headers("Content-Type: application/json")
    suspend fun query(@Body content: Request) : Response
}