package com.globant.pokemontcgapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.globant.domain.entity.PokemonType
import com.globant.pokemontcgapp.adapter.PokemonTypesAdapter
import com.globant.pokemontcgapp.databinding.FragmentPokemonTypeLayoutBinding
import com.globant.pokemontcgapp.util.Data
import com.globant.pokemontcgapp.util.Event
import com.globant.pokemontcgapp.util.Status
import com.globant.pokemontcgapp.util.getColumnsByOrientation
import com.globant.pokemontcgapp.viewmodel.PokemonTypeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PokemonTypeFragment : Fragment() {

    private val pokemonTypeViewModel by viewModel<PokemonTypeViewModel>()
    private lateinit var binding: FragmentPokemonTypeLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPokemonTypeLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pokemonTypeViewModel.getPokemonTypesLiveData().observe(::getLifecycle, ::updateUI)
        pokemonTypeViewModel.getPokemonTypes()
    }

    override fun onPause() {
        binding.pokemonTypeRecyclerView.visibility = View.GONE
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        pokemonTypeViewModel.getPokemonTypes()
    }

    private fun updateUI(data: Event<Data<List<PokemonType>>>) {
        val pokemonTypesData = data.getContentIfNotHandled()
        when (pokemonTypesData?.status) {
            Status.LOADING -> binding.pokemonTypeProgressBar.visibility = View.VISIBLE
            Status.SUCCESS -> pokemonTypesData.data?.let { showPokemonTypes(it) }
            Status.ERROR -> showPokemonTypesError(pokemonTypesData.error?.message)
        }
    }

    private fun showPokemonTypes(pokemonTypes: List<PokemonType>) {
        val pokemonTypesAdapter = PokemonTypesAdapter(pokemonTypes)

        binding.pokemonTypeProgressBar.visibility = View.GONE
        binding.pokemonTypeRecyclerView.apply {
            layoutManager =
                GridLayoutManager(context, resources.configuration.getColumnsByOrientation(COLUMNS_PORTRAIT, COLUMNS_LANDSCAPE))
            adapter = pokemonTypesAdapter
            visibility = View.VISIBLE
        }
    }

    private fun showPokemonTypesError(error: String?) {
        binding.pokemonTypeProgressBar.visibility = View.GONE
        error?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

    companion object {
        private const val COLUMNS_PORTRAIT = 4
        private const val COLUMNS_LANDSCAPE = 6
    }
}