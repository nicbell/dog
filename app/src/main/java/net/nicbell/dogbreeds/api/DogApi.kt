package net.nicbell.dogbreeds.api

import net.nicbell.dogbreeds.api.dog.DogBreed
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApi {
    /**
     * Get a list of all dog breeds / sub breeds
     */
    @GET("breeds/list/all")
    suspend fun getDogBreeds(): ApiResponse<List<DogBreed>>

    /**
     * Get a list of all images for a dog breed
     */
    @GET("breed/{breed}/images")
    suspend fun getDogBreedImages(@Path("breed") breed: String): ApiResponse<List<String>>

    /**
     * Get a list of all images for a dog breed
     */
    @GET("breed/{breed}/{subBreed}/images")
    suspend fun getDogBreedImages(
        @Path("breed") breed: String,
        @Path("subBreed") subBreed: String
    ): ApiResponse<List<String>>
}