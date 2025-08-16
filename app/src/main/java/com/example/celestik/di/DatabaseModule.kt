package com.example.celestik.di

import android.content.Context
import com.example.celestik.data.dao.CelesticDao
import com.example.celestik.database.CelesticDatabase
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
    fun provideDatabase(@ApplicationContext context: Context): CelesticDatabase {
        return CelesticDatabase.getDatabase(context)
    }

    @Provides
    fun provideCelesticDao(database: CelesticDatabase): CelesticDao {
        return database.celesticDao()
    }
}
