package com.miraeldev.search.domain.usecases.filterUseCase

import com.miraeldev.search.data.repository.SearchAnimeRepository
import me.tatarka.inject.annotations.Inject

@Inject
class AddToFilterListUseCase(val repository: SearchAnimeRepository) {
    suspend operator fun invoke(categoryId: Int, category: String) =
        repository.addToFilterList(categoryId, category)
}