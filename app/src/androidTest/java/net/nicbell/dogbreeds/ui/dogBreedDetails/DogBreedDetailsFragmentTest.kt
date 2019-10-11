package net.nicbell.dogbreeds.ui.dogBreedDetails

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import net.nicbell.dogbreeds.R
import net.nicbell.dogbreeds.api.ApiResponse
import net.nicbell.dogbreeds.api.DogApi
import net.nicbell.dogbreeds.ui.dogBreedDetails.RecyclerViewItemCountAssertion.Companion.withItemCount
import org.junit.BeforeClass
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module

/**
 * Testing breed details fragment.
 */
class DogBreedDetailsFragmentTest {
    companion object {
        @MockK
        private lateinit var dogApi: DogApi

        // Use a module with our mocked API for koin because our view model gets
        // API instance via DI
        private val networkModule = module {
            single { dogApi }
        }

        private const val breed = "bulldog"
        private const val subBreed = "french"
        private val apiSuccess = ApiResponse(
            listOf(
                "https://images.unsplash.com/photo-1558015346-c000df8119a8?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=3151&q=80"
            ), "success"
        )


        @BeforeClass
        @JvmStatic
        fun setup() {
            // things to execute once and keep around for the class
            MockKAnnotations.init(this)
            unloadKoinModules(networkModule)
            loadKoinModules(networkModule)

            // Use our fake data
            coEvery { dogApi.getDogBreedImages(breed, subBreed) } returns apiSuccess
        }
    }

    /**
     * Test images loaded into recycler
     */
    @Test
    fun showImages() {
        // Create DogBreedListFragment
        val args = DogBreedDetailsFragmentArgs.Builder(breed, subBreed).build().toBundle()
        launchFragmentInContainer<DogBreedDetailsFragment>(args)

        // Verify API is called
        coVerify {
            dogApi.getDogBreedImages(breed, subBreed)
        }

        onView(withId(R.id.recyclerImages)).check(withItemCount(apiSuccess.message.size))
    }
}