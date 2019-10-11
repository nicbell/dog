package net.nicbell.dogbreeds.di

import net.nicbell.dogbreeds.ui.dogBreedDetails.DogBreedDetailsViewModel
import net.nicbell.dogbreeds.ui.dogBreedList.DogBreedListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * View models for Koin
 */
val viewModelModule = module {
    viewModel { DogBreedListViewModel(get()) }
    viewModel { DogBreedDetailsViewModel(get()) }
}