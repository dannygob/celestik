package com.example.celestik.di

import android.content.Context
import com.example.celestik.data.dao.CelestikDao
import com.example.celestik.database.CelestikDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DatabaseModule provides Room database and DAO instances via Hilt dependency injection.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides a singleton instance of CelestikDatabase.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CelestikDatabase {
        return CelestikDatabase.getDatabase(context)
    }

    /**
     * Provides a singleton instance of CelestikDao from the database.
     */
    @Provides
    @Singleton
    fun provideCelestikDao(database: CelestikDatabase): CelestikDao {
        return database.CelestikDao()
    }
}
