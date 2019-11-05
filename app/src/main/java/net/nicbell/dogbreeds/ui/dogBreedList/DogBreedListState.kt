package net.nicbell.dogbreeds.ui.dogBreedList

import io.uniflow.core.flow.UIState
import net.nicbell.dogbreeds.api.dog.DogBreed

class DogBreedListState : UIState() {
    data class LoadedDogBreeds(val breeds: List<DogBreed>) : UIState()
}