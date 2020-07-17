package com.globant.domain.service

import com.globant.domain.entity.PokemonSupertype
import com.globant.domain.util.Result

interface PokemonSupertypesService {
    fun getPokemonSupertypes(pokemonSupertypesResources: MutableMap<String, Int>): Result<List<PokemonSupertype>>
}