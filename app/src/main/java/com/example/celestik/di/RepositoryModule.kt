package com.example.celestik.di

import com.example.celestik.data.dao.CelesticDao
import com.example.celestik.data.repository.DetectionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDetectionRepository(celesticDao: CelesticDao): DetectionRepository {
        return DetectionRepository(celesticDao)
    }
}
