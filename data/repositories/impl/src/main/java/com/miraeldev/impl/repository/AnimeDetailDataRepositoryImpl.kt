package com.miraeldev.impl.repository

import com.miraeldev.anime.AnimeInfo
import com.miraeldev.api.AnimeDetailDataRepository
import com.miraeldev.api.VideoPlayerDataRepository
import com.miraeldev.data.mapper.AnimeModelsMapper
import com.miraeldev.data.remote.dto.AnimeInfoDto
import com.miraeldev.data.remote.dto.toAnimeDetailInfo
import com.miraeldev.local.AppDatabase
import com.miraeldev.local.dao.FavouriteAnimeDao
import com.miraeldev.models.result.ResultAnimeDetail
import com.miraeldev.result.FailureCauses
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import me.tatarka.inject.annotations.Inject

@Inject
class AnimeDetailDataRepositoryImpl(
    private val videoPlayerDataRepository: VideoPlayerDataRepository,
    private val favouriteAnimeDao: FavouriteAnimeDao,
    private val animeModelsMapper: AnimeModelsMapper,
    private val appNetworkClient: com.miraeldev.api.AppNetworkClient,
    private val appDatabase: AppDatabase
) : AnimeDetailDataRepository {

    private val _animeDetail = MutableSharedFlow<ResultAnimeDetail>()

    override fun getAnimeDetail(): SharedFlow<ResultAnimeDetail> = _animeDetail.asSharedFlow()

    override suspend fun loadAnimeDetail(animeId: Int) {
        val userId = appDatabase.userDao().getUser()?.id ?: return
        val response = appNetworkClient.searchAnimeById(animeId, userId.toInt())
        if (response.status.isSuccess()) {
            val animeList = response.body<AnimeInfoDto>().toAnimeDetailInfo()
            videoPlayerDataRepository.loadVideoPlayer(animeList)
            _animeDetail.emit(
                ResultAnimeDetail.Success(animeList = listOf(animeList))
            )
        } else {
            _animeDetail.emit(
                ResultAnimeDetail.Failure(failureCause = FailureCauses.BadServer)
            )
        }
    }

    override suspend fun selectAnimeItem(isSelected: Boolean, animeInfo: AnimeInfo) {

        val response = appNetworkClient.selectAnimeItem(isSelected, animeInfo)

        if (!response.status.isSuccess()) return

        if (isSelected) {
            favouriteAnimeDao.insertFavouriteAnimeItem(
                animeModelsMapper.mapAnimeInfoToAnimeDbModel(animeInfo)
            )
        } else {
            favouriteAnimeDao.deleteFavouriteAnimeItem(animeInfo.id)
        }
    }
}