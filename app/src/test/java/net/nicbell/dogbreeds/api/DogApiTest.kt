package net.nicbell.dogbreeds.api

import com.squareup.moshi.Moshi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import net.nicbell.dogbreeds.MainCoroutineRule
import net.nicbell.dogbreeds.api.dog.DogBreedJsonAdapter
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.io.File

/**
 * Dog API test, the API calls are simple what we're actually testing
 * here is JSON deserialization and adapters.
 */
class DogApiTest {

    // Sets the main coroutines dispatcher to a TestCoroutineScope for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var dogApi: DogApi

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()

        val moshi = Moshi
            .Builder()
            .add(DogBreedJsonAdapter())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        dogApi = retrofit.create()
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun getDogBreeds() {
        mockHttpResponse("breeds.json", 200)

        runBlocking {
            val response = dogApi.getDogBreeds()
            Assert.assertEquals(response.status, "success")
            Assert.assertEquals(response.message[0].breed, "boxer")
        }
    }

    @Test
    fun getDogBreedImages() {
        mockHttpResponse("images.json", 200)

        runBlocking {
            val response = dogApi.getDogBreedImages("hound")
            Assert.assertEquals(response.status, "success")
            Assert.assertEquals(response.message.count(), 3)
        }
    }

    /**
     * Mock server response so we can isolate testing our API calls.
     */
    @Suppress("SameParameterValue")
    private fun mockHttpResponse(fileName: String, responseCode: Int) = mockWebServer.enqueue(
        MockResponse()
            .setResponseCode(responseCode)
            .setBody(getJson("api-response/$fileName"))
    )

    /**
     * Get test JSON
     */
    private fun getJson(path: String): String {
        val uri = javaClass.classLoader!!.getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }
}