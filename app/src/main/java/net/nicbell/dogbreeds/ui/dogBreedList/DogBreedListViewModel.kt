package net.nicbell.dogbreeds.ui.dogBreedList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.nicbell.dogbreeds.api.DogApi
import net.nicbell.dogbreeds.api.dog.DogBreed
import net.nicbell.dogbreeds.api.dog.DogSubBreed
import net.nicbell.dogbreeds.viewModel.Event
import net.nicbell.dogbreeds.viewModel.ViewModelExtensions.handleApiCall

/**
 * Dog breed list view model.
 */
class DogBreedListViewModel(private val dogApi: DogApi) : ViewModel(), DogBreedListHandlers {

    // Live data, don't expose, mutable live data to the view
    // unless it's a form field :P
    private val _error = MutableLiveData<Event<String>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _selectBreedCommand = MutableLiveData<Event<DogBreed>>()
    private val _selectSubBreedCommand = MutableLiveData<Event<DogSubBreed>>()
    private val _dogBreeds = MutableLiveData<List<DogBreed>>()

    val error: LiveData<Event<String>>
        get() = _error

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val selectBreedCommand: LiveData<Event<DogBreed>>
        get() = _selectBreedCommand

    val selectSubBreedCommand: LiveData<Event<DogSubBreed>>
        get() = _selectSubBreedCommand

    val dogBreeds: LiveData<List<DogBreed>>
        get() = _dogBreeds


    //region handle UI Events

    override fun onBreedClick(breed: DogBreed) {
        _selectBreedCommand.postValue(Event(breed))
    }

    override fun onSubBreedClick(subBreed: DogSubBreed) {
        _selectSubBreedCommand.postValue(Event(subBreed))
    }

    //endregion


    /**
     * Load dog breeds from API and make LiveData available for UI.
     */
    fun loadDogBreeds() {
        _isLoading.postValue(true)

        viewModelScope.launch {
            handleApiCall(_error) {
                dogApi.getDogBreeds()
            }?.let {
                _dogBreeds.postValue(it.message)
            }
            _isLoading.postValue(false)
        }
    }
}
