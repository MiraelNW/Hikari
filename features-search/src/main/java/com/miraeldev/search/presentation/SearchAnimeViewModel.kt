//package com.miraeldev.search.presentation
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import androidx.paging.cachedIn
//import com.miraeldev.extensions.mergeWith
//import com.miraeldev.search.domain.usecases.filterUseCase.ClearAllFiltersUseCase
//import com.miraeldev.search.domain.usecases.filterUseCase.GetFilterListUseCase
//import com.miraeldev.search.domain.usecases.searchUseCase.GetSearchHistoryListUseCase
//import com.miraeldev.search.domain.usecases.searchUseCase.GetSearchNameUseCase
//import com.miraeldev.search.domain.usecases.searchUseCase.GetSearchResultsUseCase
//import com.miraeldev.search.domain.usecases.searchUseCase.LoadInitialListUseCase
//import com.miraeldev.search.domain.usecases.searchUseCase.SaveNameInAnimeSearchHistoryUseCase
//import com.miraeldev.search.domain.usecases.searchUseCase.SearchAnimeByNameUseCase
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.collections.immutable.persistentListOf
//import kotlinx.coroutines.flow.MutableSharedFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.flow.filter
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.onEach
//import kotlinx.coroutines.flow.onStart
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class SearchAnimeViewModel @Inject constructor(
//    private val getFilterListUseCase: GetFilterListUseCase,
//
//    private val searchAnimeByNameUseCase: SearchAnimeByNameUseCase,
//
//    private val getSearchResults: GetSearchResultsUseCase,
//    private val loadInitialList: LoadInitialListUseCase,
//
//    private val clearAllFiltersInFilterRepository: ClearAllFiltersUseCase,
//
//    private val saveNameInAnimeSearchHistoryUseCase: SaveNameInAnimeSearchHistoryUseCase,
//
//    private val getSearchHistoryListUseCase: GetSearchHistoryListUseCase,
//
//    private val getSearchNameUseCase: GetSearchNameUseCase,
//) : ViewModel() {
//
//
//    private val initialState = getSearchResults()
//        .map {
//            val result = it.cachedIn(viewModelScope)
//            SearchAnimeScreenState.InitialList(result = result) as SearchAnimeScreenState
//        }
//
//    val screenState = searchResult
//        .onStart { loadInitialList() }
//        .mergeWith(showSearchHistoryFlow)
//        .mergeWith(initialState)
//        .stateIn(
//            viewModelScope,
//            SharingStarted.Lazily,
//            SearchAnimeScreenState.EmptyList
//        )
//
//
//    init {
//        getSearchName()
//    }
//
//
//    private fun getSearchName() {
//        viewModelScope.launch {
//            getSearchNameUseCase()
//                .filter { it.isNotEmpty() }
//                .collectLatest { name ->
//                    searchResult.emit(
//                        SearchAnimeScreenState.SearchResult(
//                            result = searchAnimeByNameUseCase(name).cachedIn(viewModelScope)
//                        )
//                    )
//                    updateSearchTextState(name)
//                }
//        }
//    }
//
//    fun searchAnimeByName(name: String) {
//        viewModelScope.launch {
//            searchResult.emit(
//                SearchAnimeScreenState.SearchResult(
//                    result = searchAnimeByNameUseCase(name).cachedIn(viewModelScope)
//                )
//            )
//            saveNameInAnimeSearchHistoryUseCase(name)
//        }
//    }
//}