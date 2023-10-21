package com.miraelDev.vauma.data.remoteMediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.miraelDev.vauma.data.dataStore.tokenService.LocalTokenService
import com.miraelDev.vauma.data.local.AppDatabase
import com.miraelDev.vauma.data.local.models.initialSearch.InitialSearchRemoteKeys
import com.miraelDev.vauma.data.remote.ApiRoutes
import com.miraelDev.vauma.data.remote.NetworkHandler
import com.miraelDev.vauma.data.remote.dto.Response
import com.miraelDev.vauma.data.remote.dto.mapToInitialSearchModel
import com.miraelDev.vauma.domain.models.anime.AnimeInfo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.errors.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class InitialSearchRemoteMediator(
    private val appDatabase: AppDatabase,
    private val client: HttpClient,
    private val networkHandler: NetworkHandler,
    private val localTokenService: LocalTokenService

) : RemoteMediator<Int, AnimeInfo>() {


    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

        return if (System.currentTimeMillis() - (appDatabase.initialSearchRemoteKeysDao()
                .getCreationTime() ?: 0) < cacheTimeout
        ) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AnimeInfo>
    ): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {

                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)

                val nextKey = remoteKeys?.nextKey
                nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {

            if (!networkHandler.isConnected.value) {
                return MediatorResult.Error(IOException())
            }

            val bearerToken = localTokenService.getBearerToken()

            val apiResponse = client.get {
                url("${ApiRoutes.SEARCH_URL_ANIME_LIST_ROUTE}&page_num=$page&page_size=$PAGE_SIZE")
                headers {
                    append(HttpHeaders.Authorization, "Bearer $bearerToken")
                }
            }
                .body<Response>()

            val anime = apiResponse.results.map { it.mapToInitialSearchModel() }

            val endOfPaginationReached =
                anime.isEmpty() || (apiResponse.count?.compareTo(page * PAGE_SIZE) ?: 1) < 1

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.initialSearchRemoteKeysDao().clearRemoteKeys()
                    appDatabase.initialSearchDao().clearAllAnime()
                }
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1
                val remoteKeys = anime.map {
                    InitialSearchRemoteKeys(
                        animeId = it.id,
                        prevKey = prevKey,
                        currentPage = page,
                        nextKey = nextKey
                    )
                }

                appDatabase.initialSearchRemoteKeysDao().insertAll(remoteKeys)
                appDatabase.initialSearchDao()
                    .insertAll(anime.onEachIndexed { _, movie -> movie.page = page })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (error: Exception) {
            return MediatorResult.Error(error)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, AnimeInfo>): InitialSearchRemoteKeys? {

        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                appDatabase.initialSearchRemoteKeysDao().getRemoteKeyByAnimeId(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, AnimeInfo>): InitialSearchRemoteKeys? {

        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { movie ->
            appDatabase.initialSearchRemoteKeysDao().getRemoteKeyByAnimeId(movie.id)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, AnimeInfo>): InitialSearchRemoteKeys? {

        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { movie ->
            appDatabase.initialSearchRemoteKeysDao().getRemoteKeyByAnimeId(movie.id)
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}