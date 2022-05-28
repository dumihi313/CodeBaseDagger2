package vn.metamon.app.di.appcontext

import android.content.Context
import dagger.Module
import dagger.Provides
import vn.metamon.app.MetamonApplication

@Module
class ContextModule {
    @Provides
    @AppContextScoped
    fun provideContext(application: MetamonApplication): Context = application.applicationContext
}