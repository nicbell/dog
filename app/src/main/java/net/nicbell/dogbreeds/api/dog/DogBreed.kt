package net.nicbell.dogbreeds.api.dog

import android.annotation.SuppressLint

data class DogBreed(val breed: String, val subBreeds: List<DogSubBreed>) {
    val name: String
        @SuppressLint("DefaultLocale")
        get() = breed.capitalize()
}