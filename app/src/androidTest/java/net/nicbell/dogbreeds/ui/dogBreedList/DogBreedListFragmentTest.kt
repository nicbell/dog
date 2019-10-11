package net.nicbell.dogbreeds.ui.dogBreedList

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
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
import org.junit.BeforeClass
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module

class DogBreedListFragmentTest {
    companion object {
        @MockK
        private lateinit var dogApi: DogApi

        // Use a module with our mocked API for koin because our view model gets
        // API instance via DI
        private val networkModule = module {
            single { dogApi }
        }

        private val apiSuccess = ApiResponse(
            listOf(
                DogBreed("boxer", listOf()),
                DogBreed(
                    "bulldog", listOf(
                        DogSubBreed("bulldog", "boston"),
                        DogSubBreed("bulldog", "english")
                    )
                )
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
            coEvery { dogApi.getDogBreeds() } returns apiSuccess
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
        Espresso.onView(ViewMatchers.withId(R.id.recyclerBreeds))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ListAdapter.ViewHolder<DogBreed>>(
                    0,
                    ViewActions.click()
                )
            )

        // Verify that performing a click prompts the correct navigation action
        verify {
            mockNavController.navigate(R.id.action_dogBreedListFragment_to_dogBreedDetailsFragment, any())
        }
    }
}