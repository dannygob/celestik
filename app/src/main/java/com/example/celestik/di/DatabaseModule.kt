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

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CelestikDatabase {
        return CelestikDatabase.getDatabase(context)
    }

    @Provides
    fun provideCelestikDao(database: CelestikDatabase): CelestikDao {
        return database.celestikDao()
    }
}
