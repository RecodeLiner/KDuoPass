package com.rcl.kduopass.di

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ComponentContextFactory
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.statekeeper.StateKeeperOwner

class DIComponentContextImpl(
    componentContext: ComponentContext,
    override val appComponent: AppComponent,
) : IDIComponentContext,
    LifecycleOwner by componentContext,
    StateKeeperOwner by componentContext,
    InstanceKeeperOwner by componentContext,
    BackHandlerOwner by componentContext {

    override val componentContextFactory: ComponentContextFactory<IDIComponentContext> =
        ComponentContextFactory { lifecycle, stateKeeper, instanceKeeper, backHandler ->
            val baseCtx = componentContext.componentContextFactory(lifecycle, stateKeeper, instanceKeeper, backHandler)
            DIComponentContextImpl(baseCtx, appComponent)
        }
}