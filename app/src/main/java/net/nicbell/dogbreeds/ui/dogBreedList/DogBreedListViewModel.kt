package net.nicbell.dogbreeds.ui.dogBreedList

import io.uniflow.android.flow.AndroidDataFlow
import io.uniflow.core.flow.UIEvent
import io.uniflow.core.flow.UIState
import net.nicbell.dogbreeds.api.DogApi
import net.nicbell.dogbreeds.api.dog.DogBreed
import net.nicbell.dogbreeds.api.dog.DogSubBreed

/**
 * Dog breed list view model.
 */
class DogBreedListViewModel(private val dogApi: DogApi) : AndroidDataFlow(), DogBreedListHandlers {

    // Live data, don't expose, mutable live data to the view
    // unless it's a form field :P
//    private val _error = MutableLiveData<Event<String>>()
//    private val _isLoading = MutableLiveData<Boolean>()
//    private val _selectBreedCommand = MutableLiveData<Event<DogBreed>>()
//    private val _selectSubBreedCommand = MutableLiveData<Event<DogSubBreed>>()
//    private val _dogBreeds = MutableLiveData<List<DogBreed>>()
//
//    val error: LiveData<Event<String>>
//        get() = _error
//
//    val isLoading: LiveData<Boolean>
//        get() = _isLoading
//
//    val selectBreedCommand: LiveData<Event<DogBreed>>
//        get() = _selectBreedCommand
//
//    val selectSubBreedCommand: LiveData<Event<DogSubBreed>>
//        get() = _selectSubBreedCommand
//
//    val dogBreeds: LiveData<List<DogBreed>>
//        get() = _dogBreeds


    //region handle UI Events

    override fun onBreedClick(breed: DogBreed) = withState {
        sendEvent(DogBreedListEvent.NavigateToBreed(breed))
    }

    override fun onSubBreedClick(subBreed: DogSubBreed) = withState {
        sendEvent(DogBreedListEvent.NavigateToSubBreed(subBreed))
    }

    //endregion


    /**
     * Load dog breeds from API and make LiveData available for UI.
     */
//    fun loadDogBreeds() {
//        setState { UIState.Loading }
//        _isLoading.postValue(true)
//
//        viewModelScope.launch {
//            handleApiCall(_error) {
//                dogApi.getDogBreeds()
//            }?.let {
//                _dogBreeds.postValue(it.message)
//            }
//            _isLoading.postValue(false)
//        }
//    }

    /**
     * Load dog breeds from API and make LiveData available for UI.
     */
    fun loadDogBreedsFlow() = stateFlow({
        setState { UIState.Loading }

        val result = dogApi.getDogBreeds()
        when {
            result.message.isEmpty() -> setState(UIState.Empty)
            result.message.any() -> setState(DogBreedListState.LoadedDogBreeds(result.message))
        }
    }, { error ->
        sendEvent(UIEvent.Fail(error.message, error))
        UIState.Failed(error = error)
    })
}
