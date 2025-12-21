package com.holparb.notemark.core.datasync.di

import com.holparb.notemark.core.datasync.data.DataSyncImpl
import com.holparb.notemark.core.datasync.data.DataSyncRepositoryImpl
import com.holparb.notemark.core.datasync.domain.DataSync
import com.holparb.notemark.core.datasync.domain.DataSyncRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataSyncModule = module {
    singleOf(::DataSyncRepositoryImpl) bind DataSyncRepository::class
    singleOf(::DataSyncImpl) bind DataSync::class
}