package com.app.examen.data.di

import com.app.examen.data.repository.SudokuRepositoryImpl
import com.app.examen.domain.repository.SudokuRepository
import com.app.examen.network.SudokuApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.api-ninjas.com/v1/") // Asegúrate que esté correcto
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSudokuApi(retrofit: Retrofit): SudokuApi {
        return retrofit.create(SudokuApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSudokuRepository(api: SudokuApi): SudokuRepository {
        return SudokuRepositoryImpl(api)
    }
}
