package com.example.noginger.di

import android.app.Application
import com.example.noginger.Feature.MemberList.MemberDataSource
import com.example.noginger.Feature.MemberList.MemberDataSourceImpl
import com.example.noginger.db.Member.MemberDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSqlDriver(app: Application): SqlDriver {
        return AndroidSqliteDriver(
            schema = MemberDatabase.Schema,
            context = app,
            name = "person.db"
        )
    }

    @Provides
    @Singleton
    fun providePersonDataSource(driver: SqlDriver): MemberDataSource {
        return MemberDataSourceImpl(MemberDatabase(driver))
    }

}