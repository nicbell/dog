package net.nicbell.dogbreeds.ui.dogBreedList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.nicbell.dogbreeds.MainCoroutineRule
import net.nicbell.dogbreeds.api.ApiResponse
import net.nicbell.dogbreeds.api.DogApi
import net.nicbell.dogbreeds.api.dog.DogBreed
import net.nicbell.dogbreeds.api.dog.DogSubBreed
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.*
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * Just mocking various API responses and seeing if the live data
 * which would be available to the view is as expected.
 */
class DogBreedListViewModelTest  {
    companion object {
        @MockK
        private lateinit var dogApi: DogApi

        @BeforeClass
        @JvmStatic
        fun setup() {
            // things to execute once and keep around for the class
            MockKAnnotations.init(this)
        }
    }

    // Run tasks synchronously
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    // Sets the main coroutines dispatcher to a TestCoroutineScope for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    private lateinit var viewModel: DogBreedListViewModel


    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        // Start with a fresh view model each test
        viewModel = DogBreedListViewModel(dogApi)
    }

    @Test
    fun loadDogBreeds() {
        // Mock API response success
        val mockedBreeds = listOf(
            DogBreed("boxer", listOf()),
            DogBreed(
                "bulldog", listOf(
                    DogSubBreed("bulldog", "boston"),
                    DogSubBreed("bulldog", "english")
                )
            )
        )
        coEvery { dogApi.getDogBreeds() } returns ApiResponse(mockedBreeds, "success")

        // Execute function
        viewModel.loadDogBreeds()

        // Check live data
        val dogBreeds = viewModel.dogBreeds.value
        Assert.assertEquals(dogBreeds, mockedBreeds)
        Assert.assertEquals(dogBreeds!![0].name, "Boxer")
        Assert.assertEquals(dogBreeds[1].subBreeds[0].name, "Boston Bulldog")
    }

    @Test
    fun loadDogBreedsServerError() {
        // Mock API HttpException
        val serverError: Response<Any> = Response.error<Any>(500, ByteArray(0).toResponseBody(null))

        coEvery { dogApi.getDogBreeds() } throws HttpException(serverError)

        // Execute function
        viewModel.loadDogBreeds()

        // Check we have an error message
        Assert.assertNotNull(viewModel.error.value?.peekContent())

        // Check the error codes match
        Assert.assertTrue(
            viewModel.error.value?.getContentIfNotHandled()?.contains(serverError.code().toString()) ?: false
        )
    }

    @Test
    fun loadDogBreedsNetworkError() {
        //Mock API IOException
        coEvery { dogApi.getDogBreeds() } throws IOException()

        // Execute function
        viewModel.loadDogBreeds()

        // Check we have an error message
        Assert.assertNotNull(viewModel.error.value?.peekContent())
    }
}