package vn.metamon.app.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import vn.metamon.app.MetamonApplication
import vn.metamon.data.di.DataComponent

@AppScoped
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ViewModelModule::class,
        ActivityBindingModule::class,
        AppModule::class,
    ],
    dependencies = [DataComponent::class]
)
interface AppComponent : AndroidInjector<MetamonApplication> {

    @Component.Factory
    interface Factory {
        fun create(
            dataComponent: DataComponent
        ): AppComponent
    }
}