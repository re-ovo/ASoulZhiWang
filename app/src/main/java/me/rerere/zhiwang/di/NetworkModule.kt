package me.rerere.zhiwang.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.rerere.zhiwang.api.bilibili.BilibiliUtil
import me.rerere.zhiwang.api.zhiwang.ZhiWangService
import me.rerere.zhiwang.api.zuowen.ZuowenService
import me.rerere.zhiwang.repo.ZuowenRepo
import me.rerere.zhiwang.util.UserAgentInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

// User Agent
private const val USER_AGENT =
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val TIMEOUT = 3000L

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
        .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
        .callTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
        .addInterceptor(UserAgentInterceptor(USER_AGENT))
        .build()

    @Provides
    @Singleton
    fun provideBilibiliUtil(okHttpClient: OkHttpClient) = BilibiliUtil(okHttpClient)

    @ZhiwangRetrofit
    @Provides
    @Singleton
    fun provideZhiwangRetrofitClient(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://asoulcnki.asia")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideZhiwangService(@ZhiwangRetrofit retrofit: Retrofit): ZhiWangService = retrofit
        .create(ZhiWangService::class.java)

    @ZuowenRetrofit
    @Provides
    @Singleton
    fun provideZuowenRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://asoul.icu")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideZuowenService(@ZuowenRetrofit retrofit: Retrofit) : ZuowenService = retrofit
        .create(ZuowenService::class.java)

    @Provides
    @Singleton
    fun provideZhiwangRepo(zhiWangService: ZhiWangService, zuowenService: ZuowenService, bilibiliUtil: BilibiliUtil) = ZuowenRepo(zhiWangService, zuowenService, bilibiliUtil)
}

@Qualifier
annotation class ZhiwangRetrofit

@Qualifier
annotation class ZuowenRetrofit