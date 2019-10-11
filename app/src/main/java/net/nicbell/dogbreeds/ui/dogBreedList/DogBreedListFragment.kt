package net.nicbell.dogbreeds.ui.dogBreedList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import net.nicbell.dogbreeds.BR
import net.nicbell.dogbreeds.R
import net.nicbell.dogbreeds.adapters.ListAdapter
import net.nicbell.dogbreeds.api.dog.DogBreed
import net.nicbell.dogbreeds.databinding.FragmentDogBreedListBinding
import net.nicbell.dogbreeds.ui.FragmentExtensions.observe
import net.nicbell.dogbreeds.ui.FragmentExtensions.observeEvent
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

        initRecycler()

        // Load breeds
        viewModel.loadDogBreeds()

        return binding.root
    }

    private fun observeViewModel() {
        observeEvent(viewModel.error) { error ->
            error?.run {
                val snackbar = Snackbar.make(binding.layCoordinator, this, Snackbar.LENGTH_INDEFINITE)
                snackbar.setAction(R.string.btn_retry) {
                    snackbar.dismiss()
                    viewModel.loadDogBreeds()
                }
                snackbar.show()
            }
        }

        observeEvent(viewModel.selectBreedCommand) { breed ->
            breed?.let {
                val args = DogBreedDetailsFragmentArgs.Builder(it.breed, null).build().toBundle()
                findNavController().navigate(R.id.action_dogBreedListFragment_to_dogBreedDetailsFragment, args)
            }
        }

        observeEvent(viewModel.selectSubBreedCommand) { subBreed ->
            subBreed?.let {
                val args = DogBreedDetailsFragmentArgs.Builder(it.breed, it.subBreed).build().toBundle()
                findNavController().navigate(R.id.action_dogBreedListFragment_to_dogBreedDetailsFragment, args)
            }
        }

        observe(viewModel.dogBreeds) { breeds ->
            breeds?.let { breedAdapter.update(breeds) }
        }
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
}