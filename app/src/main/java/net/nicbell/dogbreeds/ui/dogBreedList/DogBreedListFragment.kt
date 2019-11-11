package net.nicbell.dogbreeds.ui.dogBreedList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import io.uniflow.android.flow.onEvents
import io.uniflow.android.flow.onStates
import io.uniflow.core.flow.UIEvent
import io.uniflow.core.flow.UIState
import kotlinx.android.synthetic.main.fragment_dog_breed_list.view.*
import net.nicbell.dogbreeds.BR
import net.nicbell.dogbreeds.R
import net.nicbell.dogbreeds.adapters.ListAdapter
import net.nicbell.dogbreeds.api.dog.DogBreed
import net.nicbell.dogbreeds.databinding.FragmentDogBreedListBinding
import net.nicbell.dogbreeds.ui.dogBreedDetails.DogBreedDetailsFragmentArgs
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


/**
 * Fragment displaying a list of dog breeds / sub breeds
 */
class DogBreedListFragment : Fragment() {
    private val viewModel: DogBreedListViewModel by sharedViewModel()

    private lateinit var binding: FragmentDogBreedListBinding
    private lateinit var breedAdapter: ListAdapter<DogBreed>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDogBreedListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel
        observeViewModel()

        // Title
        activity?.setTitle(R.string.app_name)

        binding.item.title = "Hello From the View"
        binding.item.checky.isChecked = true

        initRecycler()

        // Load breeds
        viewModel.loadDogBreedsFlow()

        return binding.root
    }

    private fun observeViewModel() {
        onStates(viewModel) {
            when (it) {
                is UIState.Loading -> {
                    binding.recyclerBreeds.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                is UIState.Empty,
                is UIState.Failed -> {
                    binding.recyclerBreeds.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
                is DogBreedListState.LoadedDogBreeds -> {
                    binding.recyclerBreeds.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    breedAdapter.update(it.breeds)
                }
            }
        }

        onEvents(viewModel) { event ->
            when (val data = event.take()) {
                is UIEvent.Fail -> {
                    data.message?.let { showError(it) }
                }
                is DogBreedListEvent.NavigateToBreed -> {
                    val args = DogBreedDetailsFragmentArgs.Builder(data.breed.breed, null).build().toBundle()
                    findNavController().navigate(R.id.action_dogBreedListFragment_to_dogBreedDetailsFragment, args)
                }
                is DogBreedListEvent.NavigateToSubBreed -> {
                    val args = DogBreedDetailsFragmentArgs.Builder(data.subBreed.breed, data.subBreed.subBreed).build()
                        .toBundle()
                    findNavController().navigate(R.id.action_dogBreedListFragment_to_dogBreedDetailsFragment, args)
                }
            }
        }

//        observeEvent(viewModel.error) { error ->
//            error?.run {
//                showError(this)
//            }
//        }
//
//        observeEvent(viewModel.selectBreedCommand) { breed ->
//            breed?.let {
//                val args = DogBreedDetailsFragmentArgs.Builder(it.breed, null).build().toBundle()
//                findNavController().navigate(R.id.action_dogBreedListFragment_to_dogBreedDetailsFragment, args)
//            }
//        }
//
//        observeEvent(viewModel.selectSubBreedCommand) { subBreed ->
//            subBreed?.let {
//                val args = DogBreedDetailsFragmentArgs.Builder(it.breed, it.subBreed).build().toBundle()
//                findNavController().navigate(R.id.action_dogBreedListFragment_to_dogBreedDetailsFragment, args)
//            }
//        }
//
//        observe(viewModel.dogBreeds) { breeds ->
//            breeds?.let { breedAdapter.update(breeds) }
//        }
    }

    private fun initRecycler() {
        val itemBinder = object : ListAdapter.ItemBinder<DogBreed> {
            override fun bindItem(binding: ViewDataBinding, item: DogBreed) {
                binding.setVariable(BR.breed, item)
                binding.setVariable(BR.handlers, viewModel)
            }
        }

        breedAdapter = ListAdapter(R.layout.list_item_breed, itemBinder)
        binding.recyclerBreeds.setHasFixedSize(true)
        binding.recyclerBreeds.adapter = breedAdapter
    }

    private fun showError(message: String) {
        val snackbar = Snackbar.make(binding.layCoordinator, message, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.btn_retry) {
            snackbar.dismiss()
            viewModel.loadDogBreedsFlow()
        }
        snackbar.show()
    }
}