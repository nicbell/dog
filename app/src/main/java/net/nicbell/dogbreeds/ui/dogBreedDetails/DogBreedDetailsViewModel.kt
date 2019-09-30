package net.nicbell.dogbreeds.ui.dogBreedDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.nicbell.dogbreeds.api.DogApi
import net.nicbell.dogbreeds.viewModel.Event
import net.nicbell.dogbreeds.viewModel.ViewModelExtensions.handleApiCall
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Dog breed details view model.
 */
class DogBreedDetailsViewModel : ViewModel(), KoinComponent {

    private val dogApi: DogApi by inject()

    // Live data, don't expose, mutable live data to the view
    // unless it's a form field :P
    private val _error = MutableLiveData<Event<String>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _images = MutableLiveData<List<String>>()

    val error: LiveData<Event<String>>
        get() = _error

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val images: LiveData<List<String>>
        get() = _images


    /**
     * Load dog breeds images from API and make LiveData available for UI.
     */
    fun loadDogBreedsImages(breed: String, subBreed: String? = null) {
        _isLoading.postValue(true)

        viewModelScope.launch {
            handleApiCall(_error) {
                if (subBreed != null)
                    dogApi.getDogBreedImages(breed, subBreed)
                else
                    dogApi.getDogBreedImages(breed)
            }?.let {
                _images.postValue(it.message)
            }
            _isLoading.postValue(false)
        }
    }
}