package net.nicbell.dogbreeds.ui.dogBreedList

import net.nicbell.dogbreeds.api.dog.DogBreed
import net.nicbell.dogbreeds.api.dog.DogSubBreed

interface DogBreedListHandlers {
    /**
     * Handle dog breed clicked
     */
    fun onBreedClick(breed: DogBreed)

    /**
     * Handle dog sub breed clicked
     */
    fun onSubBreedClick(subBreed: DogSubBreed)
}