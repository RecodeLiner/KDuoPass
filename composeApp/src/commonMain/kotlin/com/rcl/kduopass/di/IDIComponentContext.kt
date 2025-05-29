package com.rcl.kduopass.di

import com.arkivanov.decompose.GenericComponentContext

interface IDIComponentContext : GenericComponentContext<IDIComponentContext> {
    val appComponent: AppComponent
}