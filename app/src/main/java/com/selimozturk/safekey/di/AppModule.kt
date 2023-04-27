package com.selimozturk.safekey.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.selimozturk.safekey.R
import com.selimozturk.safekey.data.repository.PasswordRepositoryImpl
import com.selimozturk.safekey.data.source.PasswordDatabase
import com.selimozturk.safekey.domain.repository.PasswordRepository
import com.selimozturk.safekey.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePasswordDatabase(@ApplicationContext context: Context): PasswordDatabase {
        return Room.databaseBuilder(
            context,
            PasswordDatabase::class.java,
            PasswordDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePasswordRepository(db: PasswordDatabase): PasswordRepository {
        return PasswordRepositoryImpl(db.passwordDao)
    }

    @Provides
    @Singleton
    fun providePasswordUseCases(repository: PasswordRepository): PasswordUseCases {
        return PasswordUseCases(
            getPasswords = GetPasswords(repository),
            deletePasswordById = DeletePasswordById(repository),
            insertPassword = InsertPassword(repository),
            getPasswordById = GetPasswordById(repository),
            updatePassword = UpdatePassword(repository),
        )
    }

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }

}