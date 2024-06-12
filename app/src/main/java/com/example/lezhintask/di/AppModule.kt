package com.example.lezhintask.di

import android.content.Context
import com.example.data.database.AppDatabase
import com.example.data.network.KakaoService
import com.example.data.repository.MainRepositoryImpl
import com.example.domain.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMainRepositoryImpl(@ApplicationContext context: Context): MainRepository =
        MainRepositoryImpl(context, getKakaoService(), getAppDatabase(context))

    private fun getKakaoService(): KakaoService {
        return Retrofit.Builder().baseUrl("https://dapi.kakao.com/v2/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(KakaoService::class.java)
    }

    private fun getAppDatabase(context: Context): AppDatabase = AppDatabase.getInstance(context)
}