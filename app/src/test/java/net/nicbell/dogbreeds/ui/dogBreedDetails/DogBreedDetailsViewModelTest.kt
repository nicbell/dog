package net.nicbell.dogbreeds.ui.dogBreedDetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.nicbell.dogbreeds.MainCoroutineRule
import net.nicbell.dogbreeds.api.ApiResponse
import net.nicbell.dogbreeds.api.DogApi
import org.junit.*


class DogBreedDetailsViewModelTest {
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

    private lateinit var viewModel: DogBreedDetailsViewModel


    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        // Start with a fresh view model each test
        viewModel = DogBreedDetailsViewModel(dogApi)
    }

    @Test
    fun loadDogBreedsImages() {
        // Mock API response success
        val mockedImages = listOf(
            "http://abc.com/test.jpg",
            "http://abc.com/test2.jpg"
        )
        coEvery { dogApi.getDogBreedImages("hound") } returns ApiResponse(mockedImages, "success")

        // Execute function
        viewModel.loadDogBreedsImages("hound")

        // Check live data
        Assert.assertEquals(viewModel.images.value, mockedImages)
    }
}