package net.nicbell.dogbreeds.di

import android.app.Application
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import net.nicbell.dogbreeds.R
import net.nicbell.dogbreeds.api.DogApi
import net.nicbell.dogbreeds.api.dog.DogBreedJsonAdapter
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.*

/**
 * Network module for DI,
 * I wouldn't usually have these types of functions directly in here but I don't want to spend too long..
 */
val networkModule = module {

    /**
     * Create retrofit
     */
    fun retrofit(application: Application): Retrofit {
        val moshi = Moshi
            .Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter())
            .add(DogBreedJsonAdapter())
            .build()

        return Retrofit.Builder()
            .baseUrl(application.getString(R.string.api_base))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    single { retrofit(androidApplication()) }
    single { get<Retrofit>().create<DogApi>() }
}