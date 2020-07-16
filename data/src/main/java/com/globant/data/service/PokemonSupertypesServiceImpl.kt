package com.globant.data.service

import com.globant.data.mapper.PokemonSupertypeMapper
import com.globant.data.service.api.PokemonTCGApi
import com.globant.domain.entity.PokemonSupertype
import com.globant.domain.service.PokemonSupertypesService
import com.globant.domain.util.Result

class PokemonSupertypesServiceImpl : PokemonSupertypesService {
    private val api = ServiceGenerator()
    private val mapper = PokemonSupertypeMapper()

    override fun getPokemonSupertypesFromAPI(pokemonSupertypesResources: MutableMap<String, Int>): Result<List<PokemonSupertype>> {
        try {
            val callResponse = api.createService(PokemonTCGApi::class.java).getPokemonSupertypes()
            val response = callResponse.execute()
            if (response.isSuccessful)
                response.body()?.supertypes?.let {
                    mapper.transform(it, pokemonSupertypesResources)
                }?.let {
                    return Result.Success(it)
                }
        } catch (e: Exception) {
            return Result.Failure(Exception(NOT_FOUND))
        }
        return Result.Failure(Exception(NOT_FOUND))
    }

    companion object {
        private const val NOT_FOUND = "Pokemon supertypes not found"
    }
}
