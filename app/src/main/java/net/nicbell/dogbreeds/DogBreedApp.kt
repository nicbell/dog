package net.nicbell.dogbreeds

import android.app.Application
import net.nicbell.dogbreeds.di.networkModule
import net.nicbell.dogbreeds.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DogBreedApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // start Koin!
        startKoin {
            androidContext(this@DogBreedApp)
            modules(listOf(networkModule, viewModelModule))
        }
    }
}