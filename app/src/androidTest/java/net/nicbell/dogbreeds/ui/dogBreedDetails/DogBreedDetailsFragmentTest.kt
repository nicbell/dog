package net.nicbell.dogbreeds.ui.dogBreedDetails

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import net.nicbell.dogbreeds.R
import net.nicbell.dogbreeds.adapters.ListAdapter
import net.nicbell.dogbreeds.api.ApiResponse
import net.nicbell.dogbreeds.api.DogApi
import net.nicbell.dogbreeds.api.dog.DogBreed
import net.nicbell.dogbreeds.api.dog.DogSubBreed
import net.nicbell.dogbreeds.ui.dogBreedList.DogBreedListFragment
import org.junit.BeforeClass
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module


class DogBreedDetailsFragmentTest {
    companion object {
        @MockK
        private lateinit var dogApi: DogApi

        // Use a module with our mocked API for koin because our view model gets
        // API instance via DI
        private val networkModule = module {
            single { dogApi }
        }

        @BeforeClass
        @JvmStatic
        fun setup() {
            // things to execute once and keep around for the class
            MockKAnnotations.init(this)
            unloadKoinModules(networkModule)
            loadKoinModules(networkModule)

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
        }
    }

    @Test
    fun navigationToDetails() {
        // Create a mock NavController
        val mockNavController = mockk<NavController>(relaxed = true) {}

        // Create DogBreedListFragment
        val dogBreedList = launchFragmentInContainer<DogBreedListFragment>()

        dogBreedList.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        // Click list item
        onView(withId(R.id.recyclerBreeds))
            .perform(actionOnItemAtPosition<ListAdapter.ViewHolder<DogBreed>>(0, click()))

        // Verify that performing a click prompts the correct navigation action
        verify {
            mockNavController.navigate(R.id.action_dogBreedListFragment_to_dogBreedDetailsFragment, any())
        }
    }
}