package vn.metamon.app.di.appcontext

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import vn.metamon.app.MetamonApplication

@AppContextScoped
@Component(modules = [ContextModule::class])
interface ContextComponent {
    fun context(): Context

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance liveApplication: MetamonApplication): ContextComponent
    }
}