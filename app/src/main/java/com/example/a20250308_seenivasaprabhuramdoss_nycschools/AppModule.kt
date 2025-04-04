package com.example.a20250308_seenivasaprabhuramdoss_nycschools

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
//Dagger App module it provide the instance for the following
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://data.cityofnewyork.us/resource/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build())
            .build()

    @Provides
    @Singleton
    fun provideSchoolApi(retrofit: Retrofit): SchoolApi = retrofit.create(SchoolApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetSchoolsUseCase(repository: SchoolRepository) = GetSchoolsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetSchoolDetailsUseCase(repository: SchoolRepository) = GetSchoolDetailsUseCase(repository)
}