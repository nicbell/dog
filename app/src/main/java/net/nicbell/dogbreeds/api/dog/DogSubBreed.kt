package net.nicbell.dogbreeds.api.dog

import android.annotation.SuppressLint

data class DogSubBreed(val breed: String, val subBreed: String)  {
    val name: String
        @SuppressLint("DefaultLocale")
        get() = "${subBreed.capitalize()} ${breed.capitalize()}"
}