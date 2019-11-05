package net.nicbell.dogbreeds.ui.dogBreedList

import io.uniflow.core.flow.UIEvent
import net.nicbell.dogbreeds.api.dog.DogBreed
import net.nicbell.dogbreeds.api.dog.DogSubBreed

sealed class DogBreedListEvent : UIEvent() {
    data class NavigateToBreed(val breed: DogBreed) : DogBreedListEvent()
    data class NavigateToSubBreed(val subBreed: DogSubBreed) : DogBreedListEvent()
}