package com.globant.di

import androidx.room.Room
import com.globant.data.database.PokemonDatabase
import com.globant.data.database.PokemonTypeDatabaseImpl
import com.globant.data.service.PokemonTypesServiceImpl
import com.globant.domain.database.PokemonTypeDatabase
import com.globant.domain.service.PokemonTypesService
import com.globant.domain.usecase.GetPokemonTypesUseCase
import com.globant.domain.usecase.implementation.GetPokemonTypesUseCaseImpl
import org.koin.dsl.module

val serviceModule = module {
    single<PokemonTypesService> { PokemonTypesServiceImpl() }
}

val useCaseModule = module {
    single<GetPokemonTypesUseCase> { GetPokemonTypesUseCaseImpl(get(), get()) }
}

val databaseModule = module {
    single { Room.databaseBuilder(get(), PokemonDatabase::class.java, DATA_BASE_NAME).build() }
    single { get<PokemonDatabase>().pokemonDao() }
    single<PokemonTypeDatabase> { PokemonTypeDatabaseImpl(get()) }
}

private const val DATA_BASE_NAME = "pokemon_type_database"
