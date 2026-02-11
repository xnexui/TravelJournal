package com.example.traveljournal.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.traveljournal.data.local.TripDataStore
import com.example.traveljournal.data.repository.TripRepositoryImpl
import com.example.traveljournal.domain.repository.TripRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


private val Context.dataStore by preferencesDataStore(name = "travel_journal")

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideTripDataStore(
        @ApplicationContext context: Context
    ): TripDataStore {
        return TripDataStore(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTripRepository(
        impl: TripRepositoryImpl
    ): TripRepository
}