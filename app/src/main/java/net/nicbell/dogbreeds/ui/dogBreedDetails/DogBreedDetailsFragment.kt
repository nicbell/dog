package net.nicbell.dogbreeds.ui.dogBreedDetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import net.nicbell.dogbreeds.BR
import net.nicbell.dogbreeds.R
import net.nicbell.dogbreeds.adapters.ListAdapter
import net.nicbell.dogbreeds.databinding.FragmentDogBreedDetailsBinding
import net.nicbell.dogbreeds.ui.FragmentExtensions.observe
import net.nicbell.dogbreeds.ui.FragmentExtensions.observeEvent

/**
 * Fragment displaying a list of dog images.
 */
class DogBreedDetailsFragment : Fragment() {
    private val viewModel: DogBreedDetailsViewModel by activityViewModels()
    private val args: DogBreedDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentDogBreedDetailsBinding
    private lateinit var imageAdapter: ListAdapter<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDogBreedDetailsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel
        observeViewModel()

        // Title
        @SuppressLint("DefaultLocale")
        activity?.title = "${args.breed.capitalize()} ${(args.subBreed ?: "").capitalize()}".trim()

        initRecycler()

        // Load images
        viewModel.loadDogBreedsImages(args.breed, args.subBreed)

        return binding.root
    }

    private fun observeViewModel() {
        observeEvent(viewModel.error) { error ->
            error?.run {
                val snackbar = Snackbar.make(binding.layCoordinator, this, Snackbar.LENGTH_INDEFINITE)
                snackbar.setAction(R.string.btn_retry) {
                    snackbar.dismiss()
                    viewModel.loadDogBreedsImages(args.breed, args.subBreed)
                }
                snackbar.show()
            }
        }

        observe(viewModel.images) { images ->
            images?.let { imageAdapter.update(it) }
        }
    }

    private fun initRecycler() {
        val itemBinder = object : ListAdapter.ItemBinder<String> {
            override fun bindItem(binding: ViewDataBinding, item: String) {
                binding.setVariable(BR.url, item)
            }
        }

        imageAdapter = ListAdapter(R.layout.list_item_image, itemBinder)
        binding.recyclerImages.setHasFixedSize(true)
        binding.recyclerImages.adapter = imageAdapter
    }
}