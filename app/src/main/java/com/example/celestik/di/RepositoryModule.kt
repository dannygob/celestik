package com.example.celestik.di

import com.example.celestik.data.dao.CelestikDao
import com.example.celestik.data.repository.DetectionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * RepositoryModule provides repository instances for dependency injection.
 * Repositories encapsulate data access logic and are injected into ViewModels and other components.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Provides a singleton instance of DetectionRepository.
     * This repository wraps CelestikDao and exposes high-level data operations.
     */
    @Provides
    @Singleton
    fun provideDetectionRepository(celestikDao: CelestikDao): DetectionRepository {
        return DetectionRepository(celestikDao)
    }
}
