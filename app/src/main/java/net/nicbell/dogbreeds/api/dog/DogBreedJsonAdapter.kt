package net.nicbell.dogbreeds.api.dog

import com.squareup.moshi.FromJson

class DogBreedJsonAdapter {
    @FromJson
    fun dogBreedsFromJson(dogBreedsJson: Map<String, List<String>>): List<DogBreed> {
        return dogBreedsJson.map {
            DogBreed(it.key, it.value.map { subBreed -> DogSubBreed(it.key, subBreed) })
        }
    }
}

